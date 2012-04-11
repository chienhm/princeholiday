package com.morntea.web.family;

import java.util.ArrayList;
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
	private static final Logger log = Logger.getLogger(GusetbookService.class
			.getName());
	
	public void updateMember(Long id, Long fatherId, String name, Date birthday, Date lunarbirthday, boolean gender, int zipai, String motherName, String phone, String address, String comment) {
        
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
	
    public void addMember(Long fatherId, String name, Date birthday, Date lunarbirthday, boolean gender, int zipai, String motherName, String phone, String address, String comment) {    
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
	
	
	@SuppressWarnings("unchecked")
	public List<Member> getMembers() {
		List<Member> members = new ArrayList<Member>();
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
	    List<Member> members = getMembers();
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
	    json = "{" + getDescendants(members, root) + "}";
	    return json;
	}
	
	private String getDescendants(List<Member> members, Member root) {
	    if(root==null)return "";
	    String sons = "id: \""+root.getId()+"\", name: \""+root.getName()+"\", data: {gender:"+root.isGender()+", comment:\""+root.getComment()+"\"},  children: [";

	    boolean hasChild = false;
        for(Member m : members) {
            if(m.getFatherId()!=null && m.getFatherId().equals(root.getId())) {
    	        if(!hasChild) {
    	            sons += "{";
    	            hasChild = true;
    	        } else {
    	            sons += "}, {";
    	        }
	            sons += getDescendants(members, m);
	        }
	    }
        if(hasChild) {
            sons += "}";
        }
        sons += "]";
	    return sons;
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
	}

    public void addMember(String name, String gender, String zipai, String comment) {
        Long fatherId = -1L;
        Date birthday = null;
        Date lunarbirthday = null;
        String motherName = null;
        String address = null;
        this.addMember(fatherId , name, birthday , lunarbirthday, gender.equals("0"), Integer.parseInt(zipai)+101, motherName , null, address , comment);
        
    }
}
