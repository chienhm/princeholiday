package com.morntea.web.pricemonitor;

import org.apache.http.impl.client.DefaultHttpClient;

import com.morntea.helper.Http;
import com.morntea.web.pricemonitor.data.Condition;
import com.morntea.web.pricemonitor.data.ProductItem;

public class ProductService {
	protected String html;
	private ProductItem item;
	public void load(ProductItem item) {
		this.item = item;
		Http http = new Http(new DefaultHttpClient());
		html = http.doGet(html);
	}
	
	public boolean meet(Condition condition) {
		switch(condition.getType()) {
			case 1: //PRICE_DOWN
				return priceLowerThan(Float.parseFloat(condition.getParameter()));
			case 2: //CONTAIN_STRING
				return containString(condition.getParameter());
			default:
		}
		return false;
	}
	
	public boolean priceLowerThan(float expectedPrice) {
		float price = getCurrentPrice();
		if(price<expectedPrice) {
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
		float price = getCurrentPrice();
		float lastPrice = item.getPrice();
		float diff = price - lastPrice;
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
		return 0;
		
	}
}
