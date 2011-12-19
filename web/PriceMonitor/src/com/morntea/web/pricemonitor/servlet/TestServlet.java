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
		String from = req.getParameter("from");
		String to = req.getParameter("to");
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "Hello, Google App Engine.";

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, "MornTea.com Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress(to, "Dear MornTea User"));
            msg.setSubject("Send Email To You");
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (AddressException e) {
			e.printStackTrace();
        } catch (MessagingException e) {
			e.printStackTrace();
        }
	}

}
