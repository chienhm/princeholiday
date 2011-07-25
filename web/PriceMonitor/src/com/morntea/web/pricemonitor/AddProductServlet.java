package com.morntea.web.pricemonitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.morntea.web.pricemonitor.data.Product;

public class AddProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public static final Logger logger = Logger.getLogger(AddProductServlet.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String type = req.getParameter("type");
        String pid = req.getParameter("pid");
        Product product = new Product(type, pid, null, null);
        ProductFetcher fetcher = ProductFetcherFactory.createPriceFetcher(product);
        product = fetcher.fetchInfo(product);

        if (product != null) {
            PersistenceManager pm = PMFactory.getPersistenceManager();
            try {
                pm.makePersistent(product);
            } catch (Exception e) {
                logger.severe(product.getPid()
                              + ": Can not make the product persistent:"
                              + e.toString() + "!\n");
            } finally {
                pm.close();
            }
            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter respWriter = resp.getWriter();
            respWriter.print("{type:\"" + product.getType() + "\","
                             + "pid:\"" + product.getPid() + "\","
                             + "name:\"" + product.getName() + "\"}");
        }
    }
}
