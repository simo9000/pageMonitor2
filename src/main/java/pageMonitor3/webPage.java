package pageMonitor3;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observer;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import pageMonitor3.exceptions.InvalidInputException;
import pageMonitor3.monitor.HTTPGetHandler;
import pageMonitor3.monitor.HashUtilities;
import pageMonitor3.monitor.HashUpdater;
import pageMonitor3.monitor.LogEntry;
import pageMonitor3.monitor.notificationHandler;
import pageMonitor3.monitor.dbInterface;

public class webPage extends HTTPGetHandler {

	private double interval = 2000;
	private final double minimumInterVal = 2000;
	private final int maxHashMisses = 3;

	private int PageID;
	private byte[] Hash = null, errorHash = null;
	private notificationHandler handler;
	private boolean alive = true;
	private int hashMissCount = 0;
	private String name;
	private int pageWatcherCount = 0;
	private Map<String,byte[]> watchedElements;
	
	private pageState state = pageState.normal;
	
	public webPage(int pageID, String url, String name){
		setURL(url);
		this.name = name;
		PageID = pageID;
		handler = new notificationHandler(url);
		pageWatcherCount = 0;
		watchedElements = new HashMap<String, byte[]>();
	}
	
	public webPage(int pageID, String url, String hash, String name){
		setURL(url);
		this.name = name;
		PageID = pageID;
		handler = new notificationHandler(url);
		pageWatcherCount = getPageWatchCount(pageID);
		watchedElements = getWatchedElements(pageID);
		try {
			Hash = HashUtilities.parseHash(hash);
		} catch (InvalidInputException e) {
			Hash = null;
		}
	}
	
	public void considerRefresh(){
		// TODO - add probabilistic stuff here
		refresh();
	}

	@Override
	public void considerRefresh(double requestTime) {
		if (requestTime < interval)
			try {
				//System.out.println("requestTime = " + requestTime);	
				Thread.sleep((long) (interval - requestTime));
			} catch (InterruptedException e) { }
		if (alive)
			refresh();
	}
	
	public double getRequestTime(){
		return getRequest_Time();
	}
	
	@Override
	public synchronized void handleResponce(String response) {
		byte[] newHash = HashUtilities.createHash(response);
		if(!Arrays.equals(newHash, Hash) && Hash != null){
			if (hashMissCount > maxHashMisses){
				considerTransition(newHash, response);
                hashMissCount = 0;
			} else {
				hashMissCount++;
			}
		}else if (Hash == null){
			Hash = newHash;
			new HashUpdater(PageID,newHash).update();
		}else {
			if (interval > minimumInterVal)
				if (interval - minimumInterVal < 0)
					interval = minimumInterVal;
				else
					interval -= minimumInterVal;
		}
	}
	
	private void considerTransition(byte[] newHash, String response){
		boolean isNewHashError = BootStrap.hashErrorClassifier.isHashKnownError(newHash);
		switch (state){
			case normal:
				if (isNewHashError)
				{
					state = pageState.serverError;
					errorHash = newHash;
					handler.sendNotifications(response, state, name);
				}
				else
				{
					Hash = newHash;
					if(pageWatcherCount > 0)
					{
						new HashUpdater(PageID,newHash).update();
						new LogEntry(PageID).log();
						interval += 30000;
						handler.sendNotifications(response, state, name);
					}
					if (!watchedElements.isEmpty())
						checkElements(response);
				}
				break;
			case serverError:
				if (!isNewHashError)
				{
					state = pageState.normal;
					Hash = newHash;
					if(pageWatcherCount > 0)
					{
						new HashUpdater(PageID,newHash).update();
						new LogEntry(PageID).log();
						interval += 30000;
					}
					handler.sendNotifications(response, state, name);
				}
				else
				{
					errorHash = newHash;
					handler.sendNotifications(response, state, name);
				}
				break;
		}
	}
	
	public void checkElements(String response){
		Parser parser = Parser.htmlParser();
		Document doc = parser.parseInput(response, this.getURL());
		boolean elementChanged = false;
		for (Entry<String, byte[]> entry : watchedElements.entrySet())
		{
			Element e = doc.getElementById(entry.getKey());
			if (e != null)
			{
				byte[] newElementHash = HashUtilities.createHash("" + e.toString());
				if (Arrays.equals(newElementHash, entry.getValue()) && entry.getValue() != null)
				{
					new HashUpdater(PageID,newElementHash, entry.getKey()).update();
					handler.sendNotifications(response, state, response, entry.getKey());
					elementChanged = true;
				} else if (entry.getValue() == null){
					Hash = newElementHash;
					new HashUpdater(PageID, newElementHash, entry.getKey()).update();
					elementChanged = true;
				}
				
			}
		}
		if (elementChanged) interval += 30000;
	}
	
	public static int getPageWatchCount(int pageID){
		int returnVal = 0;
		try {
			ResultSet RS = dbInterface.getPageWatchers(pageID);
			while(RS.next())
				returnVal++;
		}
		catch (SQLException e){ 
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
		return returnVal;
	}
	
	public static Map<String,byte[]> getWatchedElements(int pageID){
		Map<String,byte[]> returnVal = new HashMap<String,byte[]>();
		try{
			ResultSet RS = dbInterface.getWatchedElements(pageID);
			while(RS.next())
				returnVal.put(RS.getString(1),HashUtilities.parseHash(RS.getString(2)));
		} 
		catch (SQLException|InvalidInputException e){
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
		return returnVal;
	}
	
	public void addWatcher(int id, Observer O, pageState state, String element){
		handler.addWithID_state(id, state, O);
		watchedElements.put(element,null);
	}
	
	public void addWatcher(int id, Observer O, pageState state){
		handler.addWithID_state(id, state, O);
		pageWatcherCount++;
	}
	
	public void removeWatcher(int id, pageState state, String element){
		handler.removeWithID_state(id, state);
		watchedElements.remove(element);
	}
	
	public void removeWatcher(int id, pageState state){
		handler.removeWithID_state(id, state);
		pageWatcherCount--;
	}
	
	public void removeAllWatchers(int id){
		handler.removeAllByID(id);
	}
	
	public synchronized void killMonitoring(){
		alive = false;
	}
	
	public synchronized  void enableMonitoring(){
		alive = true;
	}


	
	
	
}
