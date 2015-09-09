package pageMonitor3.NotificationAPI;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pageMonitor3.NotificationAPI.objects.APIResponse;
import pageMonitor3.NotificationAPI.objects.User;
import pageMonitor3.NotificationAPI.objects.error;


@RestController
public class LoginController {

	@RequestMapping("/API")
	public APIResponse API(){
		return new error("Alive");
	}
	
	@RequestMapping("/API/Login")
	public APIResponse Login(@RequestParam(value="email", defaultValue="null") String email){
		APIResponse returnVal = null;
		try {
			ResultSet RS = dbInterface.getUserID(email);
			if (RS.next())
				returnVal = new User(RS.getInt("pk_id"),email);
			else
				returnVal = new error(error.errorType.userNotFound);
		} catch (SQLException e) {
			returnVal = new error(e.getMessage());
			e.printStackTrace();
		}
		return returnVal;
	}
	
	@RequestMapping("/API/newUser")
	public APIResponse newUser(@RequestParam(value="email", defaultValue = "null") String email){
		APIResponse returnVal = null;
		try{
			ResultSet RS = dbInterface.getUserID(email);
			if (!RS.next()){
				dbInterface.addNewUser(email);
				RS = dbInterface.getUserID(email);
				if (RS.next())
					returnVal = new User(RS.getInt("pk_id"),email);
				else
					returnVal = new error("unknown Error");
			}
			else
				returnVal = new error(error.errorType.userAlreadyExists);
		}catch (SQLException e) {
			returnVal = new error(e.getMessage());
			e.printStackTrace();
		}
		return returnVal;
	}
}
