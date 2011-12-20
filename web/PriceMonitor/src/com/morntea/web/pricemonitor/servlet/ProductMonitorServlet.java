package com.morntea.web.pricemonitor.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.morntea.web.pricemonitor.data.Condition;
import com.morntea.web.pricemonitor.data.ProductItem;
import com.morntea.web.pricemonitor.service.ConditionService;
import com.morntea.web.pricemonitor.service.ProductService;
import com.morntea.web.pricemonitor.service.WeiweiService;

public class ProductMonitorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ProductMonitorServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
		for(ProductItem item : ProductService.getAllItem()) {
			logger.info("Monitor item: " + item.getUrl());
			monitor(item);
		}
	}
	
	public void monitor(ProductItem item) {
		ProductService ps = new WeiweiService();
		ps.load(item);
		List<Condition> conditionList = item.getConditions();
		ConditionService cs = new ConditionService();
		for(Condition condition : conditionList) {
			if(!condition.isMeet() && ps.meet(condition)) {
				//send message if condition is meet
				cs.setCondition(condition);
				//cs.sendMsg(ps.getMessage());
				//If condition is meet, set the meet flag, so it won't send alert again.
				condition.setMeet(true);
			}
		}
		item.setPrice(ps.getNewPrice());
		ps.save();
	}
}
