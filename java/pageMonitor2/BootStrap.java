package pageMonitor2;

import pageMonitor2.com.monitor.pageMonitor;
import pageMonitor2.com.NotificationEngine.*;


public class BootStrap {

	public static void main(String[] args) {
		
		pageMonitor monitor = new pageMonitor();
		
		NotificationEngine engine = new NotificationEngine(monitor);
		
		engine.initialize();
		
		monitor.startMonitoring();
		
		engine.startMonitoring();
		
		try{
			monitor.join();
			engine.join();
		}
		catch(Exception e){
			
		}
	}
}
