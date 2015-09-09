package pageMonitor3.monitor;

import java.sql.ResultSet;
import java.sql.SQLException;

import pageMonitor3.dbConnection;

public class dbInterface extends dbConnection {
	
	public static ResultSet getPageWatchers(int pageID) throws SQLException{
		String sql = "SELECT FK_USER_ID " + 
					 "FROM tblNotificationRequests " +
					 "WHERE FK_PAGE_ID=" + pageID + " " + 
					 "AND FK_PAGE_ELEMENT_ID IS NULL;";
		return executeQuery(sql);
	}
	
	public static ResultSet getWatchedElements(int pageID) throws SQLException{
		String sql = "SELECT fdElementName, fdElementHash " +
					 "FROM tblNotificationRequests " +
					 "INNER JOIN tblPageElements " +
					 	"ON tblNotificationRequests.FK_PAGE_ELEMENT_ID=tblPageElements.pk_id " +
					 "WHERE tblNotificationRequests.FK_PAGE_ID=" + pageID + ";";
		return executeQuery(sql);
	}
}
