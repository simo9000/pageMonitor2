package pageMonitor3;


import pageMonitor3.monitor.pageMonitor;
import pageMonitor3.NotificationEngine.*;
import pageMonitor3.errorClassifaction.*;
import pageMonitor3.exceptions.InvalidInputException;

import org.springframework.boot.SpringApplication;


public class BootStrap {

	public static pageMonitor monitor;
	public static hashClassifier hashErrorClassifier;
	public static void main(String[] args) {
		
				
		try {
			hashErrorClassifier = new hashClassifier();
		} catch (InvalidInputException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		monitor = new pageMonitor();
		
		NotificationEngine engine = new NotificationEngine(monitor);
		
		engine.initialize();
		
		monitor.startMonitoring();
		
		//engine.startMonitoring();
		
		SpringApplication.run(pageMonitor3.NotificationAPI.BootStrap.class, args);
		
		try{
			monitor.join();
		}
		catch(Exception e){
			
		}
	}
}
