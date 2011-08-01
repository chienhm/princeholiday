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

public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = -2080564157261801652L;
    
    private static final Logger logger = Logger.getLogger(RegisterServlet.class.getName());
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        
        PersistenceManager pm = PMFactory.getPersistenceManager();
        Query userQuery = pm.newQuery("select from " + User.class.getName()
                                      + " where username == " + username);
        
        @SuppressWarnings("unchecked")
        List<User> users = (List<User>) userQuery.execute();
        
        if(users == null || users.size() == 0) {
            User user = new User(username, password, email);
            try {
                pm.makePersistent(user);
                //set cookies
                resp.sendRedirect(Page.SUCCESS);
            } catch (Exception e) {
                logger.severe(username
                              + ": Can not register the user: "
                              + e.toString() + "!\n");
                resp.sendRedirect(Page.ERROR);
            } finally {
                pm.close();
            }
        } else {
            resp.sendRedirect(Page.ERROR);
        }
    }
}
