package pageMonitor2;


public class BootStrap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		webPage page = new webPage(1,"https://www.cs.drexel.edu/~shm45/test.html");
		pageMonitor monitor = new pageMonitor();
		monitor.addPage(1, page);
			
		monitor.startMonitoring();
		
		notification n = new notification("simon");
		
		monitor.addWatcher(1,1,n);
		
		try {
			monitor.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
