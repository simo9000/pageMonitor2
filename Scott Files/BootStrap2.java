package pageMonitor2;

import java.sql.*;
/**
 * Created by Scott's Laptop on 11/23/2014.
 */
public class BootStrap2 {
    public static void main (String[] args) {
        try {
            DatabaseConnection.connect();
            System.out.println(DatabaseConnection.maxPK_ID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
