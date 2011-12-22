package com.morntea.web.pricemonitor;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Email;
import com.morntea.web.pricemonitor.data.Condition;
import com.morntea.web.pricemonitor.data.ProductItem;
import com.morntea.web.pricemonitor.service.ProductService;
import com.morntea.web.pricemonitor.service.WeiweiService;


public class Test {
	public static void main(String args[]) {
		ProductItem item = new ProductItem("http://www.homevv.com/vvshopProductView/pid-12349605.jhtml");
		//item.setPrice(10);
		List<Condition> cl = new ArrayList<Condition>();
		
		Condition c = new Condition();
		c.setType(4);
		c.setParameter("6");
		c.setEmail(new Email("5794560@qq.com"));
		cl.add(c);
		
		item.setConditions(cl);

		ProductService pm = new WeiweiService();
		pm.load(item);
		pm.meet(c);
		System.out.println(pm.getMessage());
		//pm.save();
	}

}
