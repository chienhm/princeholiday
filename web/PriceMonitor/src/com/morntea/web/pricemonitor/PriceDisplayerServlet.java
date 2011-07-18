package com.morntea.web.pricemonitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.jdo.Query;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PriceDisplayerServlet extends HttpServlet {
    
    private static final long serialVersionUID = 4622284175802319756L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        PrintWriter servletWriter = resp.getWriter();
        
        PersistenceManager pm = PMFactory.getPersistenceManager();
        try {
            servletWriter.println("======");
            Query productQuery = pm.newQuery("select from " + Product.class.getName());
            
            @SuppressWarnings("unchecked")
            List<Product> products = (List<Product>) productQuery.execute();
    
            for (Product product : products) {
                servletWriter.println("---------------");
                servletWriter.println(product.getPid() + " " + product.getName());
                Query priceQuery = pm.newQuery("select from " 
                        + Price.class.getName() + " where productId == " 
                        + product.getProductId() + " order by time");
                
                @SuppressWarnings("unchecked")
                List<Price> prices = (List<Price>) priceQuery.execute();
                
                for (Price price : prices) {
                    servletWriter.println(price.getTime() + " " + price.getPrice());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
    }
}