import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Observable;
import java.util.Observer;

public class EmailNotifier implements Observer {
	private String to;
	private String from;
	
	@Override
	public void update(Observable arg0, Object arg1) {
		to = "khertel@ursuline.edu";
		from = "smchenry2014@gmail.com";
		String username = "smchenry2014@gmail.com";
		String password = "Mcwilak5";
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
			message.setSubject("This is a Test Email");
			message.setText("Hey Simon,\n"
					+ "If you are receiving this email, it means that I have successfully"
					+ " gotten JavaMail to work properly.  I got it to work a few times"
					+ " already by sending emails to my old college email, so I'm pretty"
					+ " confident about this test.  Please email me back if you get this"
					+ " so that I know we're good to go on at least one front.\n\n"
					+ "Best regards,\nScott McHenry\n\n"
					+ "P.S.  I used Google's SMTP service to make this work.");
			Transport.send(message);
			System.out.println("Message sent successfully...");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}