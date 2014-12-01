package pageMonitor3.monitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEntry extends Thread{

	private int pageID;
	private Date entryTime;
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public LogEntry(int pageID_){
		pageID = pageID_;
		entryTime = new Date();
	}
	
	
	public void log(){
		this.start();
	}
	
	@Override
	public void run(){
		Connection con;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:pageMonitor.db");
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO tblModificationLog (FK_PAGE_ID, fdUpdateTime) " +  
						 "VALUES (" + pageID + ",'" + dateFormat.format(entryTime) + "');";
			stmt.executeUpdate(sql);
			stmt.close();
			con.commit();
			con.close();
		} catch (SQLException e) {
			System.out.println("log entry failed for id: " + pageID);
		} catch (ClassNotFoundException e) {
			System.out.println("log entry failed for id: " + pageID);
		}
	}
	
}
