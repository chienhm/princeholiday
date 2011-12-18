package com.morntea.web.pricemonitor.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Email;
import com.morntea.web.pricemonitor.data.Condition;

public class ConditionService {
	private static final Logger logger = Logger.getLogger(ConditionService.class.getName());
	private Condition condition;
	
	public void sendMsg(String message) {
		if(condition==null) {
			logger.log(Level.SEVERE, "Condition is not set for condition service.");
		}
		if(condition.getEmail()!=null) {
			sendEmail(condition.getEmail(), message);
		}
		if(condition.getPhone()!=null && condition.getPhone().isEmpty()) {
			sendSMS(condition.getPhone(), message);
		}
		// reset condition
		condition = null;
	}

	public void sendEmail(Email email, String message) {
		logger.log(Level.INFO, "Send email to " + email);
	}
	
	public void sendSMS(String phone, String message) {
		logger.log(Level.INFO, "Send message to " + phone);
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Condition getCondition() {
		return condition;
	}
}
