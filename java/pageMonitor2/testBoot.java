package pageMonitor2;

import pageMonitor2.com.monitor.pageMonitor;

public class testBoot {

	public static void main(String[] args) {
		
		webPage page1 = new webPage(1,"https://www.cs.drexel.edu/~shm45/test.html");
		webPage page2 = new webPage(2,"https://www.cs.drexel.edu/~shm45/index.html");
		pageMonitor monitor = new pageMonitor();
		monitor.addPage(1, page1);
			
		monitor.startMonitoring();
		
		monitor.addPage(2, page2);
		notification n = new notification("test");
		
		monitor.addWatcher(1,1,n);
		
		n = new notification("index");
		monitor.addWatcher(2,2,n);
		
		try {
			monitor.join();
		} catch (InterruptedException e) {}
	}

}
