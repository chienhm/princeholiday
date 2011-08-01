package com.morntea.web.pricemonitor;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        
        if((users == null)
         || (users.size() != 1)
         || (!users.get(0).isPassword(password))) {
            resp.sendRedirect(Page.ERROR);
        } else {
            // set cookies
            resp.sendRedirect(Page.SUCCESS);
        }
    }
}
