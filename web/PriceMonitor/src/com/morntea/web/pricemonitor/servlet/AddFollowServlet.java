package com.morntea.web.pricemonitor.servlet;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.morntea.web.pricemonitor.data.Follow;
import com.morntea.web.pricemonitor.data.Product;
import com.morntea.web.pricemonitor.data.User;
import com.morntea.web.pricemonitor.PMFactory;
import com.morntea.web.pricemonitor.ProductFetcher;
import com.morntea.web.pricemonitor.ProductFetcherFactory;

public class AddFollowServlet extends HttpServlet {
    
    private static final long serialVersionUID = 9068678736799733159L;

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {       
        HttpSession session = req.getSession(false);
        
        // user did not log in
        if (session == null) {
            return;
        }
        
        User user = (User) req.getAttribute("user");
        if (user == null) {
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

        if (products.size() > 0) { // product exist
            product = products.get(0);
        } else { // product does not exist
            product = new Product(type, pid, null, null);
            ProductFetcher fetcher = ProductFetcherFactory
                    .createPriceFetcher(product);

            // invalid type
            if (fetcher == null) { 
                return;
            }
            
            product = fetcher.fetchInfo(product);
            
            // fetch product error
            if (product == null) {
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
            return;
        }

        Follow follow = new Follow(user.getUserId(),
                product.getProductId(), level);
        pm.makePersistent(follow);
    }
}
