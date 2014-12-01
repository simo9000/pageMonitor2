package pageMonitor3;


import pageMonitor3.monitor.pageMonitor;
import pageMonitor3.NotificationEngine.*;


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
