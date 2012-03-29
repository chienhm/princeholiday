package com.morntea.web.family;

import java.util.ArrayList;
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

	public void addMember(String name, Long fatherId) {
		Member member = new Member();
		member.setName(name);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		if(fatherId!=-1) {
			member.setFatherId(fatherId);
		}
		
		try {
			pm.makePersistent(member);
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		} finally {
			pm.close();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Member> getMembers() {
		List<Member> members = new ArrayList<Member>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Member.class);
		members = (List<Member>) query.execute();
		return members;		
	}
}
