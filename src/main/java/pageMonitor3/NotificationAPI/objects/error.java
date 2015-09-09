package pageMonitor3.NotificationAPI.objects;

public class error implements APIResponse{

	public final String message;
	
	public enum errorType{
		userNotFound,
		userAlreadyExists,
		notMonitoringAnyPages,
		invalidUser,
		userAlreadyMonitoringPage,
		userNotMonitoringPage,
		pageNotBeingMonitored,
		pageStateAlreadyMonitored,
		pageStateNotRecognized,
		pageStateNotMonitored
	}
	
	public error(errorType type){
		switch (type){
		case userNotFound:
			this.message = "User not found";
			break;
		case userAlreadyExists:
			this.message = "User already exists";
			break;
		case notMonitoringAnyPages:
			this.message = "User not monitoring any pages";
			break;
		case invalidUser:
			this.message = "User id not valid";
			break;
		case userAlreadyMonitoringPage:
			this.message = "User already monitoring page";
			break;
		case userNotMonitoringPage:
			this.message = "User is not monitoring page";
			break;
		case pageNotBeingMonitored:
			this.message = "Page not being monitored";
			break;
		case pageStateAlreadyMonitored:
			this.message = "Page state already monitored";
			break;
		case pageStateNotRecognized:
			this.message = "Requested page state is not recognized";
			break;
		case pageStateNotMonitored:
			this.message = "Page state not being monitored";
			break;
		default:
			this.message = "Type not recognized";
		}
	}
	
	public error(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
	
}
