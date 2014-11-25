package pageMonitor2;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Observer;
import java.sql.SQLException;
import java.util.Arrays;

public class webPage extends HTTPGetHandler {

	private int PageID;
	private byte[] Hash = null;
	private notificationHandler handler = new notificationHandler();

    public webPage (String url) throws SQLException, ClassNotFoundException {
        this(DatabaseConnection.maxPK_ID() + 1,url);
    }
	
	public webPage(int pageID, String url) throws SQLException,ClassNotFoundException {
		setURL(url);
		PageID = pageID;
	}

	public webPage(int pageID, String url, byte[] hash){
		setURL(url);
		PageID = pageID;
		Hash = hash;
	}

    public String getURL() {
        return this.retrieveURL();
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
            System.out.println("New Hash");
			Hash = newHash;
            String update = String.format("UPDATE tblMonitoredPages SET fdHash='%s' WHERE pk_id=%d",Arrays.toString(newHash),PageID);
            try {
                DatabaseConnection.updateQuery(update);
                handler.sendNotifications();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
		}
			

		
	}
	
	public void addWatcher(int id, Observer O){
		handler.addWithID(id, O);
	}
	public void removeWatcher(int id){
		handler.removeByID(id);
	}
	

	
	
}
