package com.morntea.web.pricemonitor.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.morntea.helper.ConsoleLog;
import com.morntea.helper.StringHelper;

public class WeiweiService extends ProductService {
	private static final Logger logger = Logger.getLogger(WeiweiService.class.getName());

	public float getCurrentPrice() {
		if(html!=null && !html.isEmpty()) {
			
			String sPrice = StringHelper.regFetch(html, "id=\"vvshopMemberPrice\">\\s*￥(.+?)\\s*</STRONG>");
			ConsoleLog.log(sPrice);
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
	
	/**
	 * Check if the product has stock by the following code:<br/>
	 * ---------------------------------------------------------<br/>
	 * var validStock = 0;	//可用库存<br/>
	 * var inputNum = document.getElementById("quantity").value;<br/>
	 * validStock =-10-(-20); //没有这句表示没有库存<br/>
	 * if (inputNum>validStock){...<br/>
	 * ---------------------------------------------------------<br/>
	 */
	public boolean inStock() {
		int s = html.indexOf("var inputNum = document") + 25;
		int e = html.indexOf("inputNum>validStock", s);
		if(e>s) {
			String shim = html.substring(s, e);
			if(shim.indexOf("validStock")!=-1) {
				logger.info(item.getUrl() + " has stock.");
				return true;
			}
		}
		return false;
	}
}
