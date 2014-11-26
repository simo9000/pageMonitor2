package pageMonitor2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;



public class HashUpdater extends Thread {

	private int PageID;
	private byte[] NewHash;
	
	public HashUpdater(int pageID, byte[] newHash ){
		PageID = pageID;
		NewHash = newHash;
	}
	
	public void update(){
		this.start();
	}
	
	public void run(){
		Connection con;
		try {
			String sql = "UPDATE tblMonitoredPages " +  
						 "SET fdHash='" + Arrays.toString(NewHash) + "';";
			DatabaseConnection.updateQuery(sql);
		} catch (SQLException e) {
			System.out.println("Hash update failed for id: " + PageID);
		} catch (ClassNotFoundException e) {
			System.out.println("Hash update failed for id: " + PageID);
		}
	}
	
}
