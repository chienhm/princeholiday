package com.morntea.web.pricemonitor;

import java.io.IOException;
import javax.jdo.PersistenceManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddBookServlet extends HttpServlet {

    private static final long serialVersionUID = -2274196995667077150L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        PersistenceManager pm = PMFactory.get().getPersistenceManager();
        //String query = "select from " + Product.class.getName();
        
        //@SuppressWarnings("unchecked")
        //List<Product> products = (List<Product>) pm.newQuery(query).execute();
        //JDBookProductFetcher fetcher = new JDBookProductFetcher();
        //Product product = fetcher.getProduct("10056155");
        Product p = new Product("JD_BOOK", "10056155", 
                "A9565DD54C48CDD4C24ECCAD3B178744", "中国大历史");
        try {
                pm.makePersistent(p);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
    }
}

  
