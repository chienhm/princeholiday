package com.morntea.web.guestbook;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


public class SoundBookGuestbookServlet extends HttpServlet {
	private static final long serialVersionUID = 2584137171752358810L;
	
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
    	doGet(req, resp);
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if(user==null) {
            resp.sendRedirect("/seacrystal/guestbook.jsp");  
            return;
        }
        String content = req.getParameter("content");
        String action = req.getParameter("action");
        GusetbookService gs = new GusetbookService();
        if(action.equals("del")) {
        	Long id = Long.parseLong(req.getParameter("id"));
        	gs.delMessage(id, 1);
        } else if (action.equals("add")) {
            gs.addMessage(content, 1, req.getRemoteAddr());
        }
        resp.sendRedirect("/seacrystal/guestbook.jsp");    	
    }
}
