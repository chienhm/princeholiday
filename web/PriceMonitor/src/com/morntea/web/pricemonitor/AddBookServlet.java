package com.morntea.web.pricemonitor;

import java.io.IOException;
import javax.jdo.PersistenceManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.morntea.web.pricemonitor.data.Product;

public class AddBookServlet extends HttpServlet {

    private static final long serialVersionUID = -2274196995667077150L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        PersistenceManager pm = PMFactory.get().getPersistenceManager();
        Product product = new Product("JD_BOOK", "10056155", null, null);
        JDBookProductFetcher fetcher = new JDBookProductFetcher();
        product = fetcher.fetchInfo(product);
        
        if (product == null) {
            
        }
        
        try {
            pm.makePersistent(product);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
    }
}

  
