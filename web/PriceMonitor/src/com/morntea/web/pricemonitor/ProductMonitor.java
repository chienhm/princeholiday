package com.morntea.web.pricemonitor;

import java.util.List;

import com.morntea.web.pricemonitor.data.Condition;
import com.morntea.web.pricemonitor.data.ProductItem;

public class ProductMonitor {
	public void run() {		
		ProductItem item = new ProductItem("");
		ProductService ps = new ProductService();
		ps.load(item);
		List<Condition> conditionList = item.getConditions();
		for(Condition condition : conditionList) {
			if(ps.meet(condition)) {
				//ConditionService.send
				System.out.println("Yes");
			}
		}
	}
}
