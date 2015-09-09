package pageMonitor3.NotificationEngine;

import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailNotification implements Observer {

	private String emailAddress = null;
	private Boolean debug = true;
	
	public EmailNotification(String emailAddress_){
		emailAddress = emailAddress_;
	}
	
	@Override
	public void update(Observable arg0, Object page) {
		if (debug) System.out.println("sending update to " + emailAddress + " for " + page);
		String to = emailAddress;
		String from = "cs575pagemontior@gmail.com";
		final String username = "cs575pagemontior@gmail.com";
		final String password = "s;4KksW&b(/x5?p";
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable","true");
		properties.put("mail.smtp.host","smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		Session session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username,password);
					}
				});
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("pageMonitor Notification");
			String body = "<html>" + 
						  "<p>" +
						  "pageMonitor has detected a change in <a href=\"" + page + "\">" + page + "</a>" +
						  "</p>" +
						  "<html>";
			message.setContent(body,"text/html; charset=utf-8");
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
}
