package com.morntea.web.pricemonitor.service;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.morntea.helper.Http;
import com.morntea.web.pricemonitor.PMFactory;
import com.morntea.web.pricemonitor.data.Condition;
import com.morntea.web.pricemonitor.data.ProductItem;

public class ProductService {
	private static final Logger logger = Logger.getLogger(ProductService.class.getName());
	protected String html;
	protected ProductItem item;
	protected float newPrice;
	protected String message;
	private PersistenceManager pm = PMFactory.getPersistenceManager();

	public void load(ProductItem item) {
		this.item = item;
		html = Http.getHtml(item.getUrl(), "gb2312");
		//System.out.println(html);
		item.setFetchDate(new Date());
		newPrice = getCurrentPrice();
	}
	
	public boolean meet(Condition condition) {
		String para = condition.getParameter();
		switch(condition.getType()) {
			case 0: //CONTAIN_STRING
				return containString(para);
			case 1: //PRICE_LOWER_THAN
				if(para==null || para.isEmpty()) {
					return false;
				}
				return priceLowerThan(Float.parseFloat(para));
			case 2: //PRICE_DOWN
				return priceDown();
			case 3: //PRICE_UP
				return priceUp();
			case 4: //IN_STOCK
				return inStock();
			case 5: //STOCK_LOWER_THAN
				if(para==null || para.isEmpty()) {
					return false;
				}
				return stockLowerThan(Integer.parseInt(para));
			default:
		}
		return false;
	}
	
	protected boolean stockLowerThan(int stock) {
		return false;
	}

	public boolean inStock() {
		return false;
	}
	
	public boolean priceLowerThan(float expectedPrice) {
		if(newPrice<0)return false;
		
		logger.log(Level.INFO, "Expected Price:" + expectedPrice + ", Current Price:" + newPrice);
		if(newPrice<=expectedPrice) {
			return true;
		}
		return false;
	}
	
	public boolean priceDown() {
		if(priceChange()<0) {
			return true;
		}
		return false;
	}
	
	public boolean priceUp() {
		if(priceChange()>0) {
			return true;
		}
		return false;
	}
	
	/**
	 * @return 1 -- If price rises.<br/>
	 *        -1 -- If price goes down.<br/>
	 *         0 -- If price doesn't change.
	 */
	public int priceChange() {
		if(newPrice<0)return 0;
		
		float lastPrice = item.getPrice();
		if(lastPrice<0)return 0;
		
		float diff = newPrice - lastPrice;
		if(diff>0.01) {
			return 1;
		} else if(diff<-0.01) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public boolean containString(String str) {
		if(html.indexOf(str)!=-1) {
			return true;
		}
		return false;
	}
	
	public float getCurrentPrice() {
		return -1;		
	}
	
	/**
	 * Increase error number if error occurs.<br/>
	 * TODO If error number hit the limit, send alert to administrator.
	 */
	public void incError() {
		item.setErrorTimes(item.getErrorTimes()+1);
	}
	
	public float getNewPrice() {
		return newPrice;
	}

	public String getMessage() {
		return message;
	}
	
	// save product with its conditions?
	public void save() {
        try {
        	logger.info("save item:" + item.getUrl());
            pm.makePersistent(item);
        } catch (Exception e) {
            logger.severe(item.getId()
                          + ": Can not save product item: " + item.getUrl()
                          + "(" + e.toString() + ")!\n");
        }/* finally {
            pm.close();
        }*/
	}
	
	public List<ProductItem> getAllItem() {
		Query query = pm.newQuery(ProductItem.class);
		@SuppressWarnings("unchecked")
		List<ProductItem> items = (List<ProductItem>) query.execute();
		//pm.detachCopyAll(items);
		return items;
	}
	
	public ProductItem getItemByUrl(String url) {
		ProductItem item = null;
		Query query = pm.newQuery(ProductItem.class, "url == itemUrl");
		query.declareParameters("String itemUrl");
		@SuppressWarnings("unchecked")
		List<ProductItem> items = (List<ProductItem>) query.execute(url);
		if(items.size()>0) {
			item = items.get(0);
		}
		return item;
	}
	
	public void close() {
		if(pm!=null)pm.close();
	}
}
