package com.morntea.web.pricemonitor;

import java.util.ArrayList;
import java.util.List;

import com.morntea.web.pricemonitor.data.Condition;
import com.morntea.web.pricemonitor.data.ProductItem;
import com.morntea.web.pricemonitor.service.ProductService;
import com.morntea.web.pricemonitor.service.WeiweiService;


public class Test {
	public static void main(String args[]) {
		//--------------------------------------------- Test Mock
		ProductItem item = new ProductItem("http://www.homevv.com/vvshopProductView/pid-20927.jhtml");
		//item.setPrice(10);
		Condition c = new Condition();
		c.setType(1);
		c.setParameter("9");
		List<Condition> cl = new ArrayList<Condition>();
		cl.add(c);
		item.setConditions(cl);
		//--------------------------------------------- Begin Test
		ProductService ps = new WeiweiService();
		ps.load(item);
		List<Condition> conditionList = item.getConditions();
		for(Condition condition : conditionList) {
			if(!condition.isMeet() && ps.meet(condition)) {
				//ConditionService.send
				System.out.println("good");
			}
		}
	}

}
