package com.morntea.web.pricemonitor.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.morntea.web.pricemonitor.PMFactory;
import com.morntea.web.pricemonitor.Page;
import com.morntea.web.pricemonitor.data.User;

public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = -3242897912168290853L;

    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        PersistenceManager pm = PMFactory.getPersistenceManager();
        Query userQuery = pm.newQuery("select from " 
                + User.class.getName() + " where username == " 
                + username);
        
        @SuppressWarnings("unchecked")
        List<User> users = (List<User>) userQuery.execute();
        
        if (users.size() > 0) {
            User user = users.get(0);
            if (user.isPassword(password)) {
                // login successfully
                HttpSession session = req.getSession();
                session.setAttribute("user", user);
                
                logger.info(user.getUsername() + ": Loged in!");
                
                String message = new String("Welcome, " + user.getUsername() + "!");
                Cookie messageCookie = new Cookie("message", message);
                Cookie usernameCooke = new Cookie("username", user.getUsername());
                resp.addCookie(messageCookie);
                resp.addCookie(usernameCooke);
                resp.sendRedirect(Page.SUCCESS);
                return;
            }
        }
        // error
        String message = new String("Sorry, the username or password is incorrect!");
        Cookie messageCookie = new Cookie("message", message);
        Cookie usernameCooke = new Cookie("username", "");
        resp.addCookie(messageCookie);
        resp.addCookie(usernameCooke);
        resp.sendRedirect(Page.ERROR);
    }
}
