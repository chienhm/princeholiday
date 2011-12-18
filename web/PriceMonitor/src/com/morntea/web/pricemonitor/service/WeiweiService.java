package com.morntea.web.pricemonitor.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.morntea.helper.ConsoleLog;
import com.morntea.helper.StringHelper;

public class WeiweiService extends ProductService {
	private static final Logger logger = Logger.getLogger(WeiweiService.class.getName());

	public float getCurrentPrice() {
		if(html!=null && !html.isEmpty()) {
			String sPrice = StringHelper.regFetch(html, "id=\"vvshopMemberPrice\">\\s*гд(.+?)\\s*</STRONG>");
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
}
