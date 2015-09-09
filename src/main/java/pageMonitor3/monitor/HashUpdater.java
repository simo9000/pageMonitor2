package pageMonitor3.monitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HashUpdater extends Thread {

	private int PageID;
	private byte[] NewHash;
	private String Element = null;
	
	public HashUpdater(int pageID, byte[] newHash ){
		PageID = pageID;
		NewHash = newHash;
	}
	
	public HashUpdater(int pageID, byte[] newHash, String element){
		PageID = pageID;
		NewHash = newHash;
		Element = element;
	}
	
	public void update(){
		this.start();
	}
	
	public void run(){
		if (Element == null)
			updatePageHash();
		else
			updateElementHash();
		
	}
	
	private void updatePageHash(){
		Connection con;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:pageMonitor.db");
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			String sql = "UPDATE tblMonitoredPages " +  
						 "SET fdHash='" + Arrays.toString(NewHash) + "', fdLastUpdate='" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + "' " +
						 "WHERE pk_id=" + PageID + ";";
			stmt.executeUpdate(sql);
			stmt.close();
			con.commit();
			con.close();
		} catch (SQLException e) {
			System.out.println("Hash update failed for id: " + PageID);
		} catch (ClassNotFoundException e) {
			System.out.println("Hash update failed for id: " + PageID);
		}
	}
	
	private void updateElementHash(){
		Connection con;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:pageMonitor.db");
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			String sql = "UPDATE tblPageElements " +  
						 "SET fdHash='" + Arrays.toString(NewHash) + "'" +
						 "WHERE FK_PAGE_ID=" + PageID + " AND fdElementName=" + Element + ";";
			stmt.executeUpdate(sql);
			stmt.close();
			con.commit();
			con.close();
		} catch (SQLException e) {
			System.out.println("Hash update failed for id: " + PageID + " Element: " + Element);
		} catch (ClassNotFoundException e) {
			System.out.println("Hash update failed for id: " + PageID + " Element: " + Element);
		}
	}
	
}
