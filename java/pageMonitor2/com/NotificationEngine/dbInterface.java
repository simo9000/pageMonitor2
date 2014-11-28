package pageMonitor2.com.NotificationEngine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class dbInterface {
	
	private static Connection getConnection() throws SQLException{
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) { }
        Connection con = DriverManager.getConnection("jdbc:sqlite:pageMonitor.db");
        con.setAutoCommit(false);
        return con;
   	}
	
	private static void executeUpdate(String sql) throws SQLException{
		Connection con = getConnection();
		Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        con.commit();
	}
	
	private static ResultSet executeQuery(String sql) throws SQLException{
		Connection con = getConnection();
		Statement stmt = con.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        return result;
	}
	
	private static ResultSet getPages(int status) throws SQLException{
		String sql = "SELECT pk_id, fdURL " +
				 "FROM tblMonitoredPages " +
				 "WHERE fdProcessFlag=" + status + ";";
		return executeQuery(sql);
	}
	
	public static ResultSet getNewPages() throws SQLException{
		ResultSet returnVal = getPages(0);
		return returnVal;
	}
	
	public static ResultSet getActivePages() throws SQLException{
		return getPages(1);
	}
		
	public static void setNewPageToActive(int pageID) throws SQLException{
		String sql = "Update tblMonitoredPages " +
					 "SET fdProcessFlag=1 " + 
					 "WHERE pk_id=" + pageID + ";";
		executeUpdate(sql);
	}
	
	private static ResultSet getNotifications(int status) throws SQLException{
		String sql = "SELECT tblMonitoredPages.pk_id AS PageID, " +
					 		"tblUsers.pk_id as UserID, " +
					 		"tblUsers.fdEmailAddress as emailAddress " +
					 "FROM tblUsers " +
					 		"INNER JOIN tblNotificationRequests " +
					 			"ON tblUsers.pk_id = tblNotificationRequests.FK_USER_ID " +
					 		"INNER JOIN tblMonitoredPages " +
					 			"ON tblMonitoredPages.pk_id = tblNotificationRequests.FK_PAGE_ID " +
					 "WHERE tblNotificationRequests.fdActive = " + status + ";";
		return executeQuery(sql);
	}
	
	public static ResultSet getNewNotifications() throws SQLException{
		return getNotifications(0);
	}
	
	public static ResultSet getActiveNotifications() throws SQLException{
		return getNotifications(1);
	}
	
	public static void setNewNotificationToActive(int pageID, int UserID) throws SQLException{
		String sql = "Update tblNotificationRequests " +
					 "SET fdActive=1 " +
					 "WHERE FK_PAGE_ID=" + pageID + " AND FK_USER_ID=" + UserID +";";
		executeUpdate(sql);
	}
	
	public static ResultSet getNotificationsToDelete() throws SQLException{
		String sql = "SELECT FK_USER_ID, FK_PAGE_ID " + 
					 "FROM tblNotificationRequests " +
					 "WHERE fdActive=2;";
		return executeQuery(sql);
	}
	
	public static void clearDeletedNotification(int pageID, int UserID) throws SQLException{
		String sql = "DELETE FROM tblNotificationRequests " + 
					 "WHERE FK_PAGE_ID=" + pageID + " AND FK_USER_ID=" + UserID +";";
		executeUpdate(sql);
	}
	
	public static ResultSet getPagesToDelete() throws SQLException{
		String sql = "SELECT pk_id " +
					 "FROM tblMonitoredPages " +
					 "WHERE fdProcessFlag=2;";
		return executeQuery(sql);
	}
	
	public static void clearDeletedPage(int pageID) throws SQLException{
		String sql = "DELETE FROM tblMonitoredPages " +
					 "WHERE pk_id=" + pageID + ";";
		executeUpdate(sql);
	}
	
	
	
}
