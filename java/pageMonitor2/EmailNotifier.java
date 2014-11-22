import java.io.File;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Observable;
import java.util.Observer;
import java.sql.*;

public class EmailNotifier implements Observer {
	private String to;
	private String from;
	
	public void printFileList(String path) {
		File folder = new File(path);
		File[] filelist = folder.listFiles();
		for (File f : filelist) {
			System.out.println(f.getName());
		}
	}
	
	public String databaseStuff() {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:pageMonitor.db");
			c.setAutoCommit(false);
			System.out.println("Connected Successfully");
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM tblMonitoredPages;");
			StringBuffer message = new StringBuffer();
			while (rs.next()) {
				String pk_id = rs.getString("pk_id");
				String fdURL = rs.getString("fdURL");
				String fdHash = rs.getString("fdHash");
				String fdLastUpdate = rs.getString("fdLastUpdate");
				String fdProcessFlag = rs.getString("fdProcessFlag");
				message.append("PK_ID = " + pk_id + "\n");
				message.append("fdURL = " + fdURL + "\n");
				message.append("fdHASH = " + fdHash + "\n");
				message.append("fdLASTUPDATE = " + fdLastUpdate + "\n");
				message.append("fdPROCESSFLAG = " + fdProcessFlag + "\n\n");
			}
			message.append("Best regards,\nScott McHenry");
			return message.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void emailStuff() {
		to = "smm178@case.edu";
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
			String mytext = databaseStuff();
			message.setText(mytext);
			Transport.send(message);
			System.out.println("Message sent successfully...");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void update(Observable arg0,Object arg1) {
		emailStuff();
	}
}