package pageMonitor2;


import java.sql.SQLException;

public class BootStrap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        try {
            DatabaseConnection.connect();
            DatabaseConnection.updateQuery("DELETE from tblMonitoredPages;");
            webPage page = new webPage(1,"https://www.cs.drexel.edu/~shm45/test.html");
            System.out.println("Web page created...");
            pageMonitor monitor = new pageMonitor();
            monitor.addPage(1, page);
            monitor.startMonitoring();

            notification n = new notification("simon");

            monitor.addWatcher(1,1,n);
            Thread.sleep(2000);
            System.out.println(DatabaseConnection.createMessage());
            monitor.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

}