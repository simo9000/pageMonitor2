package pageMonitor3;


import java.util.Observable;
import java.util.Observer;


public class notification implements Observer{

	private String name;
	
	public notification(String name_) {
		name = name_;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println(name + " is updated.");
		
	}
	

	
}
