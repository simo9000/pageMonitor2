package pageMonitor3.NotificationAPI.objects;

public class WebPageStatus implements APIResponse{
	public final String URL;
	public final String updateTime;
	public final String pageName;
	public final boolean checkNormal;
	public final boolean checkError;
	public final String Element;
	public final boolean watchElement;
	
	public WebPageStatus(String URL, String updateTime, boolean checkNormal, boolean checkError, String pageName, String Element){
		this.URL = URL;
		this.updateTime = updateTime;
		this.checkNormal = checkNormal;
		this.checkError = checkError;
		this.pageName = pageName;
		if (Element == null)
		{
			watchElement = false;
			this.Element = "";
		}
		else 
		{
			watchElement = true;
			this.Element = Element;
		}
	}
	
	public String getURL(){
		return URL;
	}
	
	public String getUpdateTime(){
		return updateTime;
	}
	
	public String getPageName(){
		return pageName;
	}
	
	public boolean isCheckNormal(){
		return checkNormal;
	}
	
	public boolean isCheckError(){
		return checkError;
	}
}
