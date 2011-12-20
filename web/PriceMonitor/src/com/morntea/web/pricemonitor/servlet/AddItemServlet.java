package com.morntea.web.pricemonitor.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	ProductItem item = new ProductItem("http://www.homevv.com/vvshopProductView/pid-20927.jhtml");
		//item.setPrice(10);
		List<Condition> cl = new ArrayList<Condition>();
		
		Condition c = new Condition();
		c.setType(4);
		c.setEmail(new Email("5794560@qq.com"));
		cl.add(c);
		
		item.setConditions(cl);

		ProductService pm = new ProductService();
		pm.load(item);
		pm.save();
    }
}
