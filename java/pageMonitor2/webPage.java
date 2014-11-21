package pageMonitor2;

import java.util.Arrays;
import java.util.Observer;

public class webPage extends HTTPGetHandler {

	private int PageID;
	private byte[] Hash = null;
	private notificationHandler handler = new notificationHandler();
	
	public webPage(int pageID, String url){
		setURL(url);
		PageID = pageID;
	}
	
	public webPage(int pageID, String url, byte[] hash){
		setURL(url);
		PageID = pageID;
		Hash = hash;
	}
	
	public void considerRefresh(){
		// TODO - add probabilistic stuff here
		refresh();
	}
	
	@Override
	public synchronized void handleResponce(String response) {
		byte[] newHash = HashCreator.createHash(response);
		if(!Arrays.equals(newHash, Hash) && Hash != null){
			Hash = newHash;
			new LogEntry(PageID).log();
			new HashUpdater(PageID,newHash).update();
			handler.sendNotifications();
		}else if (Hash == null){
			Hash = newHash;
		}
			

		
	}
	
	public void addWatcher(int id, Observer O){
		handler.addWithID(id, O);
	}
	public void removeWatcher(int id){
		handler.removeByID(id);;
	}
	

	
	
}