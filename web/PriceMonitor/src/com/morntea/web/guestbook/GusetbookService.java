package com.morntea.web.guestbook;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class GusetbookService {
	private static final Logger log = Logger.getLogger(GusetbookService.class.getName());
	public void addMessage(String content, int appId) {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        Date date = new Date();
		Guestbook greeting = new Guestbook(user, content, date, appId);

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(greeting);
        } catch(Exception e) {
			log.log(Level.SEVERE, e.getMessage());
        }finally {
            pm.close();
        }
	}
}
