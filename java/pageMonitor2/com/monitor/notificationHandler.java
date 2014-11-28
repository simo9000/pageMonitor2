package pageMonitor2.com.monitor;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class notificationHandler extends Observable{

	private HashMap<Integer,Observer> watchers = new HashMap<Integer,Observer>();
	private String URL;
	
	public notificationHandler(String URL_){
		URL = URL_;
	}
	
	public void sendNotifications(){
		this.setChanged();
		this.notifyObservers(URL);
		this.clearChanged();
	}
		
	public void addWithID(int id, Observer O){
		watchers.put(id, O);
		this.addObserver(O);
	}
	
	public void removeByID(int id){
		this.deleteObserver(watchers.get(id));
	}
	
	
	
}
