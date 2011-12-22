package com.morntea.web.pricemonitor.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.morntea.helper.StringHelper;

public class WeiweiService extends ProductService {
	private static final Logger logger = Logger.getLogger(WeiweiService.class.getName());

	public float getCurrentPrice() {
		if(html!=null && !html.isEmpty()) {
			
			String sPrice = StringHelper.regFetch(html, "id=\"vvshopMemberPrice\">\\s*￥(.+?)\\s*</STRONG>");
			logger.log(Level.INFO, "Get price:" + sPrice);
			if(sPrice.isEmpty()) {
				logger.log(Level.SEVERE, "Can't get price. Url:" + item.getUrl());
			} else {
				return Float.parseFloat(sPrice);
			}
		} else {
			logger.log(Level.SEVERE, "HTML invalid for " + item.getUrl());
		}
		incError();
		return -1;
	}

	public boolean inStock() {
		int stock = getValidStock();
		if(stock>0) {
			message = "到货，当前库存" + stock;
			return true;
		}
		return false;
	}
	
	protected boolean stockLowerThan(int threshold) {
		int stock = getValidStock();
		if(stock<=threshold) {
			message = "库存数量小于 " + threshold + "，当前库存" + stock;
			return true;
		}
		return false;
	}
	
	/**
	 * Check if the product has stock by the following code:<br/>
	 * ---------------------------------------------------------<br/>
	 * var validStock = 0;	//可用库存<br/>
	 * var inputNum = document.getElementById("quantity").value;<br/>
	 * validStock =-10-(-20); //库存数量，没有这句表示没有库存<br/>
	 * if (inputNum>validStock){...<br/>
	 * ---------------------------------------------------------<br/>
	 */	
	private int getValidStock() {
		int s = html.indexOf("var inputNum = document") + 58;
		int e = html.indexOf("validStock =", s);
		if(e==-1) {
			message = "Out of stock.";
			return 0;
		}
		e = e + 12;
		int left = html.indexOf("(", e);
		int right = html.indexOf(")", left);
		String l = html.substring(e, left-1);
		int lv = Integer.parseInt(l);
		String op = html.substring(left-1, left);
		String r = html.substring(left+1, right);
		int rv = Integer.parseInt(r);
		int result = 0;
		if(op.equals("-")) {
			result = lv - rv;
		} else {
			result = lv + rv;
		}
		//System.out.println(l + op + r + "=" + result);
		return result;
	}
}
