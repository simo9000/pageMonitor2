package pageMonitor3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class dbConnection {
	private static Connection getConnection() throws SQLException{
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) { }
        Connection con = DriverManager.getConnection("jdbc:sqlite:pageMonitor.db");
        con.setAutoCommit(false);
        return con;
   	}
	
	protected static void executeUpdate(String sql) throws SQLException{
		Connection con = getConnection();
		Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        con.commit();
	}
	
	protected static ResultSet executeQuery(String sql) throws SQLException{
		Connection con = getConnection();
		Statement stmt = con.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        return result;
	}
}
