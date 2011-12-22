package com.morntea.web.pricemonitor.service;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
			logger.info("Send message to " + condition.getEmail().getEmail() + ":" + message);
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
		if(email==null || email.getEmail().isEmpty()) {
			logger.log(Level.SEVERE, "Email is invalid.");
			return;
		}
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("kissrat@gmail.com"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress(email.getEmail()));
            msg.setSubject("Product Change Notification");
            msg.setText(message);
            Transport.send(msg);

        } catch (AddressException e) {
			e.printStackTrace();
        } catch (MessagingException e) {
			e.printStackTrace();
        }
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
