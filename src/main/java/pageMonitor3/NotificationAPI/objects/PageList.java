package pageMonitor3.NotificationAPI.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import pageMonitor3.*;

public class PageList implements APIResponse{

	public final WebPageStatus pages[];
	ArrayList<WebPageStatus> builder = new ArrayList<WebPageStatus>();
	
	public PageList(ResultSet data){
		try {
			int currentPageID = 0;
			boolean currentPageNormal = false;
			boolean currentPageError = false;
			String currentURL = "";
			String currentLastUpdate = "";
			String currentPageName = "";
			String currentPageElement = "";
			while(data.next()){
				if (data.getInt(1) != currentPageID){
					addPageStatus(currentURL, currentLastUpdate, currentPageNormal, currentPageError, currentPageID, currentPageName, currentPageElement);
					currentPageNormal = false;
					currentPageError = false;
					currentURL = data.getString(2);
					currentLastUpdate = data.getString(3);
					currentPageID = data.getInt(1);
					currentPageName = data.getString(5);
					currentPageElement = data.getString(6);
				}
				String stateString = data.getString(4);
				if (pageState.valueOf(stateString) == pageState.normal)
					currentPageNormal = true;
				else if (pageState.valueOf(stateString) == pageState.serverError)
					currentPageError = true;				
			}
			addPageStatus(currentURL, currentLastUpdate, currentPageNormal, currentPageError, currentPageID, currentPageName, currentPageElement);
		} catch (SQLException e) {	}
		WebPageStatus holder[] = new WebPageStatus[0];
		this.pages = builder.toArray(holder);
	}
	
	private void addPageStatus(String URL, String update, boolean normal, boolean error, int currentPageID, String pageName, String Element){
		if (currentPageID != 0){
			WebPageStatus wps = new WebPageStatus(URL, update, normal, error, pageName, Element);
			builder.add(wps);
			}
	}
}
