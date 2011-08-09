package com.morntea.web.pricemonitor.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.morntea.web.pricemonitor.data.Follow;
import com.morntea.web.pricemonitor.data.Product;
import com.morntea.web.pricemonitor.data.User;
import com.morntea.web.pricemonitor.ErrorCode;
import com.morntea.web.pricemonitor.PMFactory;
import com.morntea.web.pricemonitor.ProductFetcher;
import com.morntea.web.pricemonitor.ProductFetcherFactory;

public class AddFollowServlet extends HttpServlet {
    
    private static final long serialVersionUID = 9068678736799733159L;
    
    public static final Logger logger = Logger.getLogger(AddFollowServlet.class.getName());

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter respWriter = resp.getWriter();
        
        HttpSession session = req.getSession(false);
        
        // user did not log in
        if (session == null) {
        	respWriter.print("ErrorCode=" + ErrorCode.ADDFOLLOW_USER_NOT_LOGIN);
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
        	respWriter.print("ErrorCode=" + ErrorCode.ADDFOLLOW_USER_NOT_LOGIN);
            return;
        }
        
        String type = req.getParameter("type");
        String pid = req.getParameter("pid");
        String levelStr = req.getParameter("level");
        Integer level = Integer.parseInt(levelStr);

        PersistenceManager pm = PMFactory.getPersistenceManager();

        // check if the product is in data store
        Query productQuery = pm.newQuery("select from "
                + Product.class.getName() + " where type == '" + type
                + "' and pid == '" + pid + "'");
        @SuppressWarnings("unchecked")
        List<Product> products = (List<Product>) productQuery.execute();
        Product product = null;

        try {
	        if (products.size() > 0) { // product exist
	            product = products.get(0);
	        } else { // product does not exist
	            product = new Product(type, pid, null, null);
	            ProductFetcher fetcher = ProductFetcherFactory
	                    .createPriceFetcher(product);
	
	            // invalid type
	            if (fetcher == null) {
	            	respWriter.print("ErrorCode=" + ErrorCode.ADDFOLLOW_INVALID_PRODUCT_TYPE);
	                return;
	            }
	            
	            product = fetcher.fetchInfo(product);
	            
	            // fetch product error
	            if (product == null) {
	            	respWriter.print("ErrorCode=" + ErrorCode.ADDFOLLOW_FETCH_PRODUCT_ERROR);
	                return;
	            }
	            
	            pm.makePersistent(product);
	        }
	        
	        // check if the follow is in data store
	        
	        Query followQuery = pm.newQuery("select from " + Follow.class.getName()
	                + " where userId == '" + user.getUserId()
	                + "' and productID == '" + product.getProductId() + "'");
	        @SuppressWarnings("unchecked")
	        List<Follow> follows = (List<Follow>) followQuery.execute();
	
	        if (follows.size() > 0) { // follow exist
	        	respWriter.print("ErrorCode=" + ErrorCode.ADDFOLLOW_FOLLOW_EXIST);
	            return;
	        }
	
	        Follow follow = new Follow(user.getUserId(),
	                product.getProductId(), level);
	        pm.makePersistent(follow);
	        
	        respWriter.print("ErrorCode=" + ErrorCode.ADDFOLLOW_SUCCESS);
        } catch (Exception e) {
        	respWriter.print("ErrorCode=" + ErrorCode.ADDFOLLOW_INTERNAL_ERROR);
        	pm.close();
        }
    }
}
