package com.morntea.web.pricemonitor;

import java.util.List;
import java.io.IOException;
import javax.jdo.PersistenceManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.morntea.web.pricemonitor.data.Product;
import com.morntea.web.pricemonitor.data.Price;

public class PriceMonitorServlet extends HttpServlet {

    private static final long serialVersionUID = 3535010863164481522L;
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        PersistenceManager pm = PMFactory.getPersistenceManager();
        String query = "select from " + Product.class.getName();
        
        @SuppressWarnings("unchecked")
        List<Product> products = (List<Product>) pm.newQuery(query).execute();
        
        try {
            ProductFetcher priceFetcher = null;
            Price price = null;
            for(Product product : products) {
                priceFetcher = ProductFetcherFactory.createPriceFetcher(product);
                price = priceFetcher.fetchPrice(product);
                pm.makePersistent(price);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
    }
}
