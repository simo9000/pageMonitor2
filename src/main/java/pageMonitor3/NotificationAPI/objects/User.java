package pageMonitor3.NotificationAPI.objects;


public class User implements APIResponse {
	
	private final long id;
	private final String email;
	
	public User(long id, String email){
		this.id = id;
		this.email = email;
	}
	
	public long getId(){
		return id;
	}
	
	public String getEmail(){
		return email;
	}
}
