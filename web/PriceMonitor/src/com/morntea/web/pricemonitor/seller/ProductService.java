package com.morntea.web.pricemonitor.seller;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.impl.client.DefaultHttpClient;

import com.morntea.helper.Http;
import com.morntea.web.pricemonitor.data.Condition;
import com.morntea.web.pricemonitor.data.ProductItem;

public class ProductService {
	private static final Logger log = Logger.getLogger(ProductService.class.getName());
	protected String html;
	protected ProductItem item;
	protected float newPrice;

	public void load(ProductItem item) {
		this.item = item;
		Http http = new Http(new DefaultHttpClient());
		html = http.doGet(item.getUrl());
		//System.out.println(html);
		item.setFetchDate(new Date());
		newPrice = getCurrentPrice();
	}
	
	public boolean meet(Condition condition) {
		switch(condition.getType()) {
			case 0: //CONTAIN_STRING
				return containString(condition.getParameter());
			case 1: //PRICE_LOWER_THAN
				return priceLowerThan(Float.parseFloat(condition.getParameter()));
			case 2: //PRICE_DOWN
				return priceDown();
			case 3: //PRICE_UP
				return priceUp();
			default:
		}
		return false;
	}
	
	public boolean priceLowerThan(float expectedPrice) {
		if(newPrice<0)return false;
		
		log.log(Level.INFO, "Expected Price:" + expectedPrice + ", Current Price:" + newPrice);
		if(newPrice<expectedPrice) {
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
}
