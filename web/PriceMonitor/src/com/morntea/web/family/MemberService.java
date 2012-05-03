package com.morntea.web.family;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.morntea.web.guestbook.GusetbookService;
import com.morntea.web.guestbook.PMF;

public class MemberService {
    /* Work around. id is used for JavaScript node id hash to order the siblings */
    private static int id = 0;
	private static final Logger log = Logger.getLogger(GusetbookService.class
			.getName());
	private List<Member> members;
	
	public void updateMember(Long id, Long fatherId, String name, String byname, Date birthday, Date lunarbirthday, Date deathday, boolean gender, int zipai, String motherName, String phone, String address, String comment) {
        
	    Member member;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        member = (Member)pm.getObjectById(Member.class, id);
        if(member!=null) {
            member.setFatherId(fatherId);
            if(name!=null && !name.isEmpty()) {
                member.setName(name);
            }
            member.setByname(byname);
            member.setGender(gender);
            member.setGeneration(zipai);
            member.setMotherName(motherName);
            member.setComment(comment);
            member.setBirthday(birthday);
            member.setLunarBirthday(lunarbirthday);
            member.setDeathday(deathday);
            member.setPhone(phone);
            member.setAddress(address);
            try {
                pm.makePersistent(member);
            } catch (Exception e) {
                log.log(Level.SEVERE, e.getMessage());
                e.printStackTrace();
            } finally {
                pm.close();
            }
        }
    }
	
    public void addMember(Long fatherId, String name, String byname, Date birthday, Date lunarbirthday, Date deathday, boolean gender, int zipai, String motherName, String phone, String address, String comment) {    
        if(name!=null && !name.isEmpty()) {
            PersistenceManager pm = PMF.get().getPersistenceManager();
            Member member = new Member();
            member.setFatherId(fatherId);
            member.setName(name);
            member.setByname(byname);
            member.setGender(gender);
            member.setGeneration(zipai);
            member.setMotherName(motherName);
            member.setComment(comment);
            member.setBirthday(birthday);
            member.setLunarBirthday(lunarbirthday);
            member.setDeathday(deathday);
            member.setPhone(phone);
            member.setAddress(address);
            try {
                pm.makePersistent(member);
            } catch (Exception e) {
                log.log(Level.SEVERE, e.getMessage());
                e.printStackTrace();
            } finally {
                pm.close();
            }
            members = null;
        }
    }

    
    public void delMember(Long id) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Member m = pm.getObjectById(Member.class, id);
        try {
            if(m!=null) {
                pm.deletePersistent(m);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
        } finally {
            pm.close();
        }
        members = null;
    }

    // for data initialize
    public void addMember(String name, String gender, String zipai, String comment) {
        Long fatherId = -1L;
        Date birthday = null;
        Date lunarbirthday = null;
        Date deathday = null;
        String motherName = null;
        String address = null;
        this.addMember(fatherId , name, null, birthday , lunarbirthday, deathday, gender.equals("0"), Integer.parseInt(zipai)+101, motherName , null, address , comment);
        
    }
    
    public Member getMember(Long id) {
        List<Member> members = getAllMembers();
        for(Member m : members) {
            if(m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }
    
    public List<Member> getSiblings(Member person) {
        List<Member> siblings = new ArrayList<Member>();
        List<Member> members = getAllMembers();
        for(Member m : members) {
            if(person.getFatherId()!=null 
                    && !person.getFatherId().equals(-1L)
                    && m.getFatherId().equals(person.getFatherId())
                    && !m.getId().equals(person.getId())) {
                siblings.add(m);
            }
        }
        return siblings;
    }
	
	public List<Member> getDescendant(Member father) {
        List<Member> members = getAllMembers();
        List<Member> descendants = new ArrayList<Member>();
        for(Member m : members) {
            if(m.getFatherId()!=null && m.getFatherId().equals(father.getId())) {
                descendants.add(m);
            }
        }
        Collections.sort(descendants, new Comparator<Member>(){
            @Override
            public int compare(Member m1, Member m2) {
                if(m1!=null && m2!=null) {
                    Date b1 = m1.getLunarBirthday();
                    Date b2 = m2.getLunarBirthday();
                    if(b1!=null && b2!=null) {
                        return b1.compareTo(b2);                        
                    }
                }
                return 0;
            }
        });
        return descendants;
    }

	public List<Member> getMemberByName(String name) {
        List<Member> members = getAllMembers();
        List<Member> persons = new ArrayList<Member>();
        for(Member m : members) {
            if(m.getName().equals(name)) {
                persons.add(m);
            }
        }
        return persons;
	}
	
    @SuppressWarnings("unchecked")
	public List<Member> getAllMembers() {
	    if(members!=null) {
	        return members;
	    }
		members = new ArrayList<Member>();
        HashMap<Long, Member> hm = new HashMap<Long, Member>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
    		Query query = pm.newQuery(Member.class);
    		query.setOrdering("generation asc");
    		members = (List<Member>) query.execute();
    	} finally {
            //pm.close();
        }
    	for(Member m : members) {
    	    hm.put(m.getId(), m);
    	}
        for(Member m : members) {
            Member father = hm.get(m.getFatherId());
            if(father!=null) {
                List<Long> descendants = father.getDescendants();
                descendants.add(m.getId());
                father.setDescendants(descendants);
            }
        }
		return members;		
	}
    
    public List<String> getSpouse(Member person) {
        List<String> spouses = new ArrayList<String>();

        List<Member> descendants = getDescendant(person);
        for(Member m : descendants) {
            if(!spouses.contains(m.getMotherName())) {
                spouses.add(m.getMotherName());
            }
        }
        return spouses;
    }

    // get the member as root if rootId specified, otherwise select the first root of the forest
	public Member getRoot(Long rootId) {
        List<Member> members = getAllMembers();
        Member root = null;
	    for(Member m : members) {
            if(rootId!=null) {
                if(m.getId().equals(rootId)) {
                    root = m;
                    break;
                }
            } else {
                if(m.getFatherId()==null || m.getFatherId().equals(-1L)) {
                    root = m;
                    break;
                }
            }
        }
	    return root;
	}

    // used for org chart
    public String getDescendantList(Member m) {
        String out = "<li"
            + (m.isGender() ? " class='male" : " class='female")
            + ((m.getDeathday()!=null && m.getDeathday().compareTo(new Date())<0) ? " dead'" : "'")
            + " id='m"+m.getId()+"'>" 
            + "<a href='person.jsp?id="+m.getId()+"' target='_blank'>" 
            + m.getName()
            + "</a>";
        List<Member> desendants = getDescendant(m);
        if(desendants.size()>0) {
            out += "<ul>";
            for(Member sd : desendants) {
                out += getDescendantList(sd);
            }
            out += "</ul>";
        }
        out += "</li>";
        return out;
    }

    // used for family tree
    public String getJson(Long rootId) {
        String json;
        List<Member> members = getAllMembers();
        Member root = getRoot(rootId);
        json = "{" + getDescendantString(members, root) + "}";
        id = 0;
        return json;
    }
    
	private String getDescendantString(List<Member> members, Member root) {
	    if(root==null)return "";
	    String sons = "id: \""+(id++)+"\", name: \""+root.getName()+"\", data: {id:"+root.getId()+", gender:"+root.isGender()+", comment:\""+root.getComment()+"\"},  children: [";

	    boolean hasChild = false;
        List<Member> descendants = getDescendant(root);
        for(Member m : descendants) {
            if(!hasChild) {
                sons += "{";
                hasChild = true;
            } else {
                sons += "}, {";
            }
            sons += getDescendantString(members, m);
        }
        if(hasChild) {
            sons += "}";
        }
        sons += "]";
	    return sons;
	}
	
}
