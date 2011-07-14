package com.morntea.web.pricemonitor.server;

import java.util.Date;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;
import java.net.URL;

import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.api.urlfetch.HTTPResponse;

public class JDBookProductFetcher implements ProductFetcher {
    public static Pattern PID_REX = Pattern.compile("pid:\\s*\"(\\d+)\"");
    public static Pattern SID_REX = Pattern.compile("sid:\\s*\"(\\w+)\"");
    public static Pattern NAME_REX = Pattern.compile("<title>¡¶(.*)¡·£¨(.*)£©.*</title>");
    public static Pattern PRICE_REX = Pattern.compile("\\\\uFFE5([0-9\\.]+)\"");
    
    public Product getProduct(String pid) {
        try {
            URLFetchService fetchService = URLFetchServiceFactory.getURLFetchService();
            URL productUrl = new URL("http://book.360buy.com/" + pid + ".html");
            HTTPResponse response = fetchService.fetch(productUrl);
            byte[] content = response.getContent();
            String contentString = new String(content);
            
            String sid = null;
            String name = null;
            Matcher matcher = SID_REX.matcher(contentString);
            if (matcher.find()) {
                sid = matcher.group(1);
            } else {
                return null;
            }
            matcher = NAME_REX.matcher(contentString);
            if (matcher.find()) {
                name = matcher.group(1) + matcher.group(2);
            } else {
                return null;
            }
            Product product = new Product("JD_BOOK", pid, sid, name);
            return product;
        } catch (Exception e) {
            e.printStackTrace();
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
            String contentString = new String(content);
            Matcher matcher = PRICE_REX.matcher(contentString);
            if (matcher.find()) {
                String curPriceStr = matcher.group(1);
                Double curPrice = Double.parseDouble(curPriceStr);
                Date curTime = new Date();
                Price price = new Price(product.getProductId(), curTime, 
                        curPrice, "");
                return price;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
