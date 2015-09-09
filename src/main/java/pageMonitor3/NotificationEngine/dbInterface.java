package pageMonitor3.NotificationEngine;

import java.sql.ResultSet;
import java.sql.SQLException;

import pageMonitor3.dbConnection;

public class dbInterface extends dbConnection{
	
	public static ResultSet getActivePages() throws SQLException{
		String sql = "SELECT pk_id, fdURL, fdHash, fdName " +
				 "FROM tblMonitoredPages;";
		return executeQuery(sql);
	}
	
	public static ResultSet getActivePageNotifications() throws SQLException{
		String sql = "SELECT tblMonitoredPages.pk_id AS PageID, " +
					 		"tblUsers.pk_id as UserID, " +
					 		"tblUsers.fdEmailAddress as emailAddress, " +
					 		"tblNotificationRequests.fdNotificationType as fdNotificationType " +
					 "FROM tblUsers " +
					 		"INNER JOIN tblNotificationRequests " +
					 			"ON tblUsers.pk_id = tblNotificationRequests.FK_USER_ID " +
					 		"INNER JOIN tblMonitoredPages " +
					 			"ON tblMonitoredPages.pk_id = tblNotificationRequests.FK_PAGE_ID " +
					 "WHERE tblNotificationRequests.FK_PAGE_ELEMENT_ID IS NULL;";
		return executeQuery(sql);
	}	
	
	public static ResultSet getActiveElementNotifications() throws SQLException{
		String sql = "SELECT tblMonitoredPages.pk_id AS PageID, " +
					 		"tblUsers.pk_id as UserID, " +
					 		"tblUsers.fdEmailAddress as emailAddress, " +
					 		"tblNotificationRequests.fdNotificationType as fdNotificationType " +
					 		"tblPageElements.fdElementName as elementName " +
					 		"tblPageElements.fdElementHash as hash " +
					 "FROM tblUsers " +
					 		"INNER JOIN tblNotificationRequests " +
					 			"ON tblUsers.pk_id = tblNotificationRequests.FK_USER_ID " +
					 		"INNER JOIN tblMonitoredPages " +
					 			"ON tblMonitoredPages.pk_id = tblNotificationRequests.FK_PAGE_ID " +
					 		"INNER JOIN tblPageElements " +
					 			"ON tblPageElements.pk_id = tblNotificationRequests.FK_PAGE_ELEMENT_ID; ";
		return executeQuery(sql);
	}
}
