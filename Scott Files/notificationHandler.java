package pageMonitor2;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class notificationHandler extends Observable{

	private HashMap<Integer,Observer> watchers = new HashMap<Integer,Observer>();
	
	public void sendNotifications(){
		this.setChanged();
		this.notifyObservers();
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
