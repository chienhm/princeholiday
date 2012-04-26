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
	
	public void updateMember(Long id, Long fatherId, String name, Date birthday, Date lunarbirthday, Date deathday, boolean gender, int zipai, String motherName, String phone, String address, String comment) {
        
	    Member member;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        member = (Member)pm.getObjectById(Member.class, id);
        if(member!=null) {
            member.setFatherId(fatherId);
            if(name!=null && !name.isEmpty()) {
                member.setName(name);
            }
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
	
    public void addMember(Long fatherId, String name, Date birthday, Date lunarbirthday, Date deathday, boolean gender, int zipai, String motherName, String phone, String address, String comment) {    
        if(name!=null && !name.isEmpty()) {
            PersistenceManager pm = PMF.get().getPersistenceManager();
            Member member = new Member();
            member.setFatherId(fatherId);
            member.setName(name);
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
        this.addMember(fatherId , name, birthday , lunarbirthday, deathday, gender.equals("0"), Integer.parseInt(zipai)+101, motherName , null, address , comment);
        
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
	
	public String getJson(Long rootId) {
	    String json;
	    List<Member> members = getAllMembers();
	    Member root = getRoot(rootId);
	    json = "{" + getDescendants(members, root) + "}";
	    id = 0;
	    return json;
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
    public String getDescendantList(List<Member> members, Member m) {
        String out = "<li"
            + (m.isGender() ? " class='male" : " class='female")
            + ((m.getDeathday()!=null && m.getDeathday().compareTo(new Date())<0) ? " dead'" : "'")
            + " id='m"+m.getId()+"'>" + m.getName();
        List<Member> desendants = getDescendant(members, m);
        if(desendants.size()>0) {
            out += "<ul>";
            for(Member sd : desendants) {
                out += getDescendantList(members, sd);
            }
            out += "</ul>";
        }
        out += "</li>";
        return out;
    }
    
	private String getDescendants(List<Member> members, Member root) {
	    if(root==null)return "";
	    String sons = "id: \""+(id++)+"\", name: \""+root.getName()+"\", data: {id:"+root.getId()+", gender:"+root.isGender()+", comment:\""+root.getComment()+"\"},  children: [";

	    boolean hasChild = false;
        List<Member> descendants = getDescendant(members, root);
        for(Member m : descendants) {
            if(!hasChild) {
                sons += "{";
                hasChild = true;
            } else {
                sons += "}, {";
            }
            sons += getDescendants(members, m);
        }
        if(hasChild) {
            sons += "}";
        }
        sons += "]";
	    return sons;
	}
	
	public List<Member> getDescendant(List<Member> allMembers, Member father) {
	    List<Member> descendants = new ArrayList<Member>();
	    for(Member m : allMembers) {
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
}
