package com.morntea.web.pricemonitor;

import java.util.Date;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;
import java.util.logging.Logger;
import java.net.URL;

import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.api.urlfetch.HTTPResponse;

import com.morntea.web.pricemonitor.data.Product;
import com.morntea.web.pricemonitor.data.Price;

public class JDBookProductFetcher implements ProductFetcher {
    public static Pattern PID_REX = Pattern.compile("pid:\\s*\"(\\d+)\"");
    public static Pattern SID_REX = Pattern.compile("sid:\\s*\"(\\w+)\"");
    public static Pattern NAME_REX = Pattern.compile("<title>《(.*)》（(.*)）.*</title>");
    public static Pattern PRICE_REX = Pattern.compile("\\\\uFFE5([0-9\\.]+)\"");
    
    public static final Logger logger = Logger.getLogger(JDBookProductFetcher.class.getName());
    
    public Product getProduct(Product product) {
        try {
            URLFetchService fetchService = URLFetchServiceFactory.getURLFetchService();
            URL productUrl = new URL("http://book.360buy.com/" 
                                     + product.getPid() + ".html");
            HTTPResponse response = fetchService.fetch(productUrl);
            byte[] content = response.getContent();
            String contentString = new String(content, "GBK");
            
            // fetch the book sid.
            Matcher matcher = SID_REX.matcher(contentString);
            if (matcher.find()) {
                String sid = matcher.group(1);
                product.setSid(sid);
            } else {
                logger.warning(product.getPid()
                               + ": Can not match the book sid!\n");
                return null;
            }
            // fetch the book name.
            matcher = NAME_REX.matcher(contentString);
            if (matcher.find()) {
                String name = matcher.group(1);
                String author = matcher.group(2);
                product.setName(name + " - " + author);
            } else {
                logger.warning(product.getPid()
                               + ": Can not match the book name!\n");
                return null;
            }
            return product;
        } catch (Exception e) {
            logger.severe(product.getPid() + ": Fetch book info error: "
                          + e.toString() + "!\n");
        }
        return null; 
    }
    public Price getPrice(Product product) {
        try {
            URLFetchService fetchService = URLFetchServiceFactory.getURLFetchService();
            URL priceUrl = new URL("http://price.360buy.com/price-b-P"
                    + product.getSid() + ".html");
            HTTPResponse response = fetchService.fetch(priceUrl);
            byte[] content = response.getContent();
            String contentString = new String(content, "GBK");
            
            // fetch the book price.
            Matcher matcher = PRICE_REX.matcher(contentString);
            if (matcher.find()) {
                String curPriceStr = matcher.group(1);
                Double curPrice = Double.parseDouble(curPriceStr);
                Date curTime = new Date();
                Price price = new Price(product.getProductId(), curTime, 
                        curPrice, "");
                return price;
            } else {
                logger.warning(product.getPid()
                               + ": Can not fetch book price!\n");
            }
        } catch (Exception e) {
            logger.severe(product.getPid()
                          + ": Fetch book price error: " + e.toString() + "!\n");
        }
        return null;
    }
}
