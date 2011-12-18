package com.morntea.web.pricemonitor.seller;

import com.morntea.helper.StringHelper;
import com.morntea.web.pricemonitor.ProductService;

public class WeiweiService extends ProductService {

	public float getCurrentPrice() {
		if(html!=null && !html.isEmpty()) {
			String sPrice = StringHelper.regFetch(html, "为为价:￥([^-]+)-为为网");
			if(sPrice.isEmpty()) {
				return -1;
			} else {
				return Float.parseFloat(sPrice);
			}
		}
		return -1;
	}
}
