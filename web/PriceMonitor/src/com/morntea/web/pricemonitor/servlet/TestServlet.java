package com.morntea.web.pricemonitor.servlet;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "...";

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("lahvey@gmail.com", "Example.com Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress("kissrat@gmail.com", "Mr. User"));
            msg.setSubject("Your Example.com account has been activated");
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (AddressException e) {
			e.printStackTrace();
        } catch (MessagingException e) {
			e.printStackTrace();
        }
	}

}
