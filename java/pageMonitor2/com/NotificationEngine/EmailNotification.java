package pageMonitor2.com.NotificationEngine;

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
	
	public EmailNotification(String emailAddress_){
		emailAddress = emailAddress_;
	}
	
	@Override
	public void update(Observable arg0, Object page) {
		String to = emailAddress;
		String from = "smchenry2014@gmail.com";
		final String username = "smchenry2014@gmail.com";
		final String password = "Mcwilak5";
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
			System.out.println("Message sent successfully...");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
}
