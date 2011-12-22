package com.morntea.web.pricemonitor.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Email;
import com.morntea.web.pricemonitor.data.Condition;
import com.morntea.web.pricemonitor.data.ProductItem;
import com.morntea.web.pricemonitor.service.ProductService;

public class AddItemServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public static final Logger logger = Logger.getLogger(AddItemServlet.class.getName());

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	String url = req.getParameter("url");
    	int type = Integer.parseInt(req.getParameter("type"));
    	String para = req.getParameter("para");
    	String email = req.getParameter("email");
    	
		ProductService ps = new ProductService(); //factory TODO
    	ProductItem item = ps.getItemByUrl(url);
    	if(item == null) {
    		item = new ProductItem(url);
    	}
    	
		//item.setPrice(10);
		List<Condition> cl = item.getConditions();
		if(cl==null) {
			cl = new ArrayList<Condition>();
			
			Condition c = new Condition();
			c.setType(type);
			c.setParameter(para);
			c.setEmail(new Email(email));
			cl.add(c);
			
			item.setConditions(cl);
	
			ps.load(item);
			ps.save();
		} else {
			for(Condition c : cl) {
				if(c.getType()==type) {
					logger.log(Level.WARNING, "type exists.");
					break;
				}
			}
		}
		ps.close();
		resp.sendRedirect("monitor.jsp");
    }
}
