package pageMonitor2;

import java.sql.*;
/**
 * Created by Scott's Laptop on 11/22/2014.
 */
public class DatabaseConnection {
    private static Connection myconnect;
    public static void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        myconnect = DriverManager.getConnection("jdbc:sqlite:pageMonitor.db");
        myconnect.setAutoCommit(false);
        System.out.println("Connected Successfully");
    }
    public static ResultSet selectQuery(String query) throws SQLException, ClassNotFoundException {
        Statement stmt = myconnect.createStatement();
        ResultSet result = stmt.executeQuery(query);
        stmt.close();
        return result;
    }
    public static int maxPK_ID() throws SQLException {
        Statement stmt = myconnect.createStatement();
        ResultSet result = stmt.executeQuery("SELECT pk_id FROM tblMonitoredPages;");
        int number = 0;
        while (result.next()) {
            number = result.getInt("pk_id");
        }
        result.close();
        stmt.close();
        return number;
    }
    public static void updateQuery(String query) throws SQLException, ClassNotFoundException {
        Statement stmt = myconnect.createStatement();
        stmt.executeUpdate(query);
        stmt.close();
        myconnect.commit();
    }
    public static String createMessage() throws SQLException {
        try {
            Statement stmt = myconnect.createStatement();
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
            rs.close();
            stmt.close();
            return message.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
