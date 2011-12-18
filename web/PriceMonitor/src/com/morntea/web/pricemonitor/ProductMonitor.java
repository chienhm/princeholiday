package com.morntea.web.pricemonitor;

import java.util.List;

import com.morntea.web.pricemonitor.data.Condition;
import com.morntea.web.pricemonitor.data.ProductItem;
import com.morntea.web.pricemonitor.service.ConditionService;
import com.morntea.web.pricemonitor.service.ProductService;

public class ProductMonitor {
	
	public void run() {		
		ProductItem item = new ProductItem("");
		ProductService ps = new ProductService();
		ps.load(item);
		List<Condition> conditionList = item.getConditions();
		ConditionService cs = new ConditionService();
		for(Condition condition : conditionList) {
			if(!condition.isMeet() && ps.meet(condition)) {
				//send message if condition is meet
				cs.setCondition(condition);
				cs.sendMsg();
				//If condition is meet, set the meet flag, so it won't send alert again.
				condition.setMeet(true);
				
				System.out.println("Yes");
			}
		}
		item.setPrice(ps.getNewPrice());
	}
}
