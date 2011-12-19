package com.morntea.web.pricemonitor;

import com.google.appengine.api.datastore.Email;
import com.morntea.web.pricemonitor.service.ConditionService;


public class Test {
	public static void main(String args[]) {
		ConditionService cs = new ConditionService();
		cs.sendEmail(new Email("lahvey@gmail.com"), "test message");
	}

}
