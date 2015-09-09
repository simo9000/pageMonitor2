package pageMonitor3.NotificationAPI;

import java.sql.ResultSet;
import java.sql.SQLException;

import pageMonitor3.dbConnection;
import pageMonitor3.pageState;

public class dbInterface extends dbConnection {

	public static ResultSet getUserID(String email) throws SQLException{
		String sql = "SELECT pk_id " +
					 "FROM tblUsers " +
					 "WHERE fdEmailAddress ='" + email + "';";
		return executeQuery(sql);
	}

	public static void addNewUser(String email) throws SQLException{
		String sql = "INSERT INTO tblUsers " + 
					 "VALUES((SELECT CASE WHEN MAX(pk_id) IS NULL THEN 1 ELSE MAX(pk_id)+1 END FROM tblUsers),'" + email + "');";
		executeUpdate(sql);
	}
	
	public static ResultSet getUserPages(int id) throws SQLException{
		String sql = "SELECT tblMonitoredPages.pk_id, tblMonitoredPages.fdURL, tblMonitoredPages.fdLastUpdate, tblNotificationRequests.fdNotificationType, tblMonitoredPages.fdName, tblPageElements.fdElementName " +
					 "FROM tblMonitoredPages " +
					 "INNER JOIN tblNotificationRequests ON tblMonitoredPages.pk_id = tblNotificationRequests.FK_PAGE_ID " +
					 "LEFT JOIN tblPageElements ON tblNotificationRequests.FK_PAGE_ELEMENT_ID=tblPageElements.PK_ID " + 
					 "WHERE tblNotificationRequests.FK_USER_ID=" + id + " " +
					 "ORDER BY tblMonitoredPages.pk_id;";
		return executeQuery(sql);
	}
	
	public static ResultSet getUserPage(int userID, int pageID) throws SQLException{
		String sql = "SELECT tblMonitoredPages.fdLastUpdate, tblNotificationRequests.fdNotificationType as type " +
					 "FROM tblMonitoredPages " +
					 "INNER JOIN tblNotificationRequests ON tblMonitoredPages.pk_id = tblNotificationRequests.FK_PAGE_ID " +
					 "WHERE tblNotificationRequests.FK_USER_ID=" + userID + 
					 " AND tblNotificationRequests.FK_PAGE_ID=" + pageID + ";";
		return executeQuery(sql);
	}
	
	public static ResultSet getUserPageElement(int userID, int pageID) throws SQLException{
		String sql = "SELECT tblPageElements.fdElementName " +
					 "FROM tblNotificationRequests " +
					 "INNER JOIN tblPageElements ON tblNotificationRequests.FK_PAGE_ELEMENT_ID = tblPageElements.PK_ID " +
					 "WHERE tblNotificationRequests.FK_USER_ID=" + userID + 
					 " AND tblNotificationRequests.FK_PAGE_ID=" + pageID + ";";
		return executeQuery(sql);
	}
	
	public static ResultSet getPageID(String url) throws SQLException{
		String sql = "SELECT pk_id " +
					 "FROM tblMonitoredPages " +
					 "WHERE fdURL='" + url + "';";
		return executeQuery(sql);
	}
	
	public static void addUserToPage(int userID, int pageID, pageState state) throws SQLException{
		String sql = "INSERT INTO tblNotificationRequests VALUES(" + userID + "," + pageID + ",'" + state.name() + "');";
		executeUpdate(sql);
	}
	
	public static void addNewPage(String url, String name) throws SQLException{
		String sql = "INSERT INTO tblMonitoredPages " +
					 "VALUES ((SELECT CASE WHEN MAX(pk_id) IS NULL THEN 1 ELSE MAX(pk_id)+1 END FROM tblMonitoredPages)," +
					 "'" + url + "',NULL,datetime('now'),'" + name + "');";
		executeUpdate(sql);
	}
	
	public static void removeUserFromPage(int userID, int pageID) throws SQLException{
		String sql = "DELETE FROM tblNotificationRequests " + 
					 "WHERE FK_USER_ID=" + userID + " AND FK_PAGE_ID=" + pageID + ";";
		executeUpdate(sql);
	}
		
	public static void removeUserFromPage(int userID, int pageID, pageState state) throws SQLException{
		String sql = "DELETE FROM tblNotificationRequests " + 
				 "WHERE FK_USER_ID=" + userID + " AND FK_PAGE_ID=" + pageID + " AND fdNotificationType='" + state.name() + "';";
		executeUpdate(sql);
	}
	
	public static ResultSet getUsersOnPage(int pageID) throws SQLException{
		String sql = "SELECT FK_USER_ID " +
					 "FROM tblNotificationRequests " + 
					 "WHERE FK_PAGE_ID =" + pageID + ";";
		return executeQuery(sql);
	}
	
	public static void removePage(int pageID) throws SQLException{
		String sql = "DELETE FROM tblMonitoredPages " +
					 "WHERE pk_id =" + pageID + ";";
		executeUpdate(sql);
	}
	
	public static ResultSet getUserEmail(int userID) throws SQLException{
		String sql = "SELECT fdEmailAddress " +
					 "FROM tblUsers " +
					 "WHERE pk_id =" + userID + ";";
		return executeQuery(sql);
	}

	public static void addElementToUserPage(int userID, int pageID, String element) throws SQLException{
		String sql = "SELECT PK_ID "+
					 "FROM tblPageElements " +
					 "WHERE FK_PAGE_ID=" + pageID + " AND fdElementName='" + element + "';";
		ResultSet RS = executeQuery(sql);
		int elementID;
		if (!RS.next())
		{
			sql = "INSERT INTO tblPageElements " +
				  "VALUES ((SELECT CASE WHEN MAX(pk_id) IS NULL THEN 1 ELSE MAX(pk_id)+1 END FROM tblPageElements)," +
				  pageID + ",'" + element + "',NULL);";
			executeUpdate(sql);
			sql = "SELECT PK_ID " +
				  "FROM tblPageElements " +
				  "WHERE FK_PAGE_ID=" + pageID + " AND fdElement='" + element +"';";
			ResultSet SRS = executeQuery(sql); 
			SRS.next();
			elementID = SRS.getInt(1);
		}
		else
			elementID = RS.getInt(1);
		sql = "UPDATE tblNotificationRequests " +
				  "SET FK_PAGE_ELEMENT_ID=" + elementID + " " +
				  "WHERE FK_USER_ID=" + userID + " AND FK_PAGE_ID=" + pageID + ";";
		executeUpdate(sql);
		
	}

	public static void removeUserElementFromPage(int userID, int pageID) {
		String sql = "UPDATE tblNotificationRequests " +
					 "SET FK_PAGE_ELEMENT_ID=NULL " +
					 "WHERE FK_USER_ID=" + userID + " AND FK_PAGE_ID=" + pageID + ";";
		
	}
}
