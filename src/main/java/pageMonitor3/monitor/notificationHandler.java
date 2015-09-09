package pageMonitor3.monitor;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import pageMonitor3.pageState;
import pageMonitor3.NotificationEngine.Tuple;
import pageMonitor3.NotificationEngine.Tuple5;

public class notificationHandler extends Observable{

	private HashMap<Tuple<Integer,pageState,Integer>,Observer> watchers = new HashMap<Tuple<Integer,pageState,Integer>,Observer>();
	private String URL;
	
	public notificationHandler(String URL_){
		URL = URL_;
	}
	
	// v 0.1
	public void sendNotifications(){
		this.setChanged();
		this.notifyObservers(URL);
		this.clearChanged();
	}

	// v 0.2
	public void sendVerboseNotifications(String Message){
		this.setChanged();
		//Tuple compoundMessage = new Tuple<String,String>(URL, Message);
		//this.notifyObservers(compoundMessage);
		this.clearChanged();
	}
	
	// v 0.3
	public void sendNotifications(String Message, pageState state){
		this.setChanged();
		Tuple<String, String, pageState> compoundMessage = new Tuple<String,String,pageState>(URL, Message, state);
		this.notifyObservers(compoundMessage);
		this.clearChanged();		
	}
	
	// v 0.4 page only monitoring
	public void sendNotifications(String Message, pageState state, String name){
		this.setChanged();
		Tuple5<String, String, pageState, String, Integer> compoundMessage = new Tuple5<String, String, pageState, String, Integer>(URL, Message, state, name, null);
		this.notifyObservers(compoundMessage);
		this.clearChanged();		
	}
	
	// v 0.5 element monitoring
	public void sendNotifications(String Message, pageState state, String name, String element){
		this.setChanged();
		Tuple5<String, String, pageState, String, String> compoundMessage = new Tuple5<String, String, pageState, String, String>(URL, Message, state, name, element);
		this.notifyObservers(compoundMessage);
		this.clearChanged();		
	}
	
	
	public void addWithID_state(int id, pageState state, Observer O){
		Tuple<Integer,pageState,Integer> key = new Tuple<Integer, pageState, Integer>(id, state, 0);
		watchers.put(key, O);
		this.addObserver(O);
	}
	
	public void removeAllByID(int id){
		for (pageState state : pageState.values()){
			Tuple<Integer,pageState,Integer> key = new Tuple<Integer, pageState, Integer>(id, state, 0);
			Observer O = watchers.get(key);
			if (O != null)
				this.deleteObserver(O);
		}
	}
	
	public void removeWithID_state(int id, pageState state){
		Tuple<Integer, pageState, Integer> key = new Tuple<Integer, pageState, Integer>(id, state, 0);
		Observer O = watchers.get(key);
		this.deleteObserver(O);
 	}
	
	
	
}
