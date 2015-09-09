package pageMonitor3.NotificationAPI;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pageMonitor3.BootStrap;
import pageMonitor3.pageState;
import pageMonitor3.webPage;
import pageMonitor3.NotificationAPI.objects.APIResponse;
import pageMonitor3.NotificationAPI.objects.PageList;
import pageMonitor3.NotificationAPI.objects.error;
import pageMonitor3.NotificationEngine.EmailNotification;
import pageMonitor3.NotificationEngine.VerboseEmailNotification;

@RestController
public class UserController {

	@RequestMapping("/API/User/Pages")
	public APIResponse getUserPages(@RequestParam(value="id", defaultValue="-1") int id){
		APIResponse returnVal = null;
		if (id != -1){
			try {
				returnVal = new PageList(dbInterface.getUserPages(id));
			} catch (SQLException e) {
				returnVal = new error(e.getMessage());
				e.printStackTrace();
			}
		}
		else
			returnVal = new error(error.errorType.invalidUser);
		return returnVal;
	}
	
	@RequestMapping("/API/User/Pages/Add")
	public APIResponse addUserPage(@RequestParam(value="id", defaultValue="-1") int userID,
								   @RequestParam(value="URL", defaultValue="null") String url,
								   @RequestParam(value="name", defaultValue="null") String name){
		APIResponse returnVal = null;
		if (userID != -1 && !url.equals("null")){
			try{
				ResultSet RS = dbInterface.getPageID(url);
				if (RS.next()){
					int pageID = RS.getInt("pk_id");
					ResultSet URS = dbInterface.getUserPage(userID, pageID );
					if (!URS.next()){
						addNewNotification(userID, pageID, pageState.normal);
						returnVal = new PageList(dbInterface.getUserPages(userID));
					}
					else
						returnVal = new error(error.errorType.userAlreadyMonitoringPage);
				}
				else{
					int pageID = addNewPage(url, name);
					addNewNotification(userID, pageID, pageState.normal);
					returnVal = new PageList(dbInterface.getUserPages(userID));
				}
			}
			catch(SQLException e) {
				returnVal = new error(e.getMessage());
				e.printStackTrace();
			}
		}
		else
			returnVal = new error("invalid input");
		return returnVal;
	}
	
	@RequestMapping("/API/User/Pages/Remove")
	public APIResponse removeUserPage(@RequestParam(value="id", defaultValue="-1") int userID,
								      @RequestParam(value="URL", defaultValue="null") String url){
		APIResponse returnVal = null;
		if (userID != -1 && !url.equals("null")){
			try{
				ResultSet rawPageRS = dbInterface.getPageID(url);
				if (rawPageRS.next()){
					int pageID = rawPageRS.getInt("pk_id");
					ResultSet RS = dbInterface.getUserPage(userID, pageID);
					if (RS.next())
						removeAllWatchers(userID, pageID);
						returnVal = new PageList(dbInterface.getUserPages(userID));
						ResultSet remainingRS = dbInterface.getUsersOnPage(pageID);
						if (!remainingRS.next())
							removePage(pageID);
					else 
						returnVal = new error(error.errorType.userNotMonitoringPage);
				}
				else
					returnVal = new error(error.errorType.pageNotBeingMonitored);
			}
			catch(SQLException e){
				returnVal = new error(e.getMessage());
				e.printStackTrace();
			}
		}
		else
			returnVal = new error("invalid input");
		return returnVal;
	}
	
	@RequestMapping("/API/User/Pages/AddState")
	public APIResponse addUserPageState(@RequestParam(value="id", defaultValue="-1") int userID,
									  @RequestParam(value="URL", defaultValue="null") String url,
									  @RequestParam(value="state", defaultValue="null") String state){
		APIResponse returnVal = null;
		if (userID != -1 && !url.equals("null") && !state.equals("null")){
			try{
				ResultSet rawPageRS = dbInterface.getPageID(url);
				if (rawPageRS.next()){
					int pageID = rawPageRS.getInt("pk_id");
					ResultSet RS = dbInterface.getUserPage(userID, pageID);
					boolean pageExists = false, stateExists = false;
					while (RS.next()){
						pageExists = true;
						if (state.equals(RS.getString("type"))) stateExists = true;
					}
					if (!pageExists)
						returnVal = new error(error.errorType.userNotMonitoringPage);
					else if (!stateExists){
						pageState newState = null;
						try{
							newState = pageState.valueOf(state);
							addNewNotification(userID, pageID, newState);
							returnVal = new PageList(dbInterface.getUserPages(userID));
						} catch (IllegalArgumentException e){
							returnVal = new error(error.errorType.pageStateNotRecognized);
						}
					} else
						returnVal = new error(error.errorType.pageStateAlreadyMonitored);	
				}
				else
					returnVal = new error(error.errorType.pageNotBeingMonitored);
			}
			catch(SQLException e){
				returnVal = new error(e.getMessage());
				e.printStackTrace();
			}
		}
		else
			returnVal = new error("invalid input");
		return returnVal;
	}
	
	@RequestMapping("/API/User/Pages/RemoveState")
	public APIResponse removeUserPageState(@RequestParam(value="id", defaultValue="-1") int userID,
			  							   @RequestParam(value="URL", defaultValue="null") String url,
			  							   @RequestParam(value="state", defaultValue="null") String state){	
		APIResponse returnVal = null;
		if (userID != -1 && !url.equals("null") && !state.equals("null")){
			try{
				ResultSet rawPageRS = dbInterface.getPageID(url);
				if (rawPageRS.next()){
					int pageID = rawPageRS.getInt("pk_id");
					ResultSet RS = dbInterface.getUserPage(userID, pageID);
					boolean pageExists = false, stateExists = false;
					while (RS.next()){
						if(!pageExists)pageExists = true;
						if (state.equals(RS.getString("type"))) {
							stateExists = true;
							break;
						}
					}
					if (!pageExists)
						returnVal = new error(error.errorType.userNotMonitoringPage);
					else if (stateExists){
						pageState victimState = pageState.valueOf(state);
						removeWatcher(userID, pageID, victimState);
						ResultSet remainingRS = dbInterface.getUsersOnPage(pageID);
						if (!remainingRS.next())
							removePage(pageID);
						returnVal = new PageList(dbInterface.getUserPages(userID));
					} else
						returnVal = new error(error.errorType.pageStateNotMonitored);	
				}
				else
					returnVal = new error(error.errorType.pageNotBeingMonitored);
			}
			catch(SQLException e){
				returnVal = new error(e.getMessage());
				e.printStackTrace();
			}
		}
		else
			returnVal = new error("invalid input");
		return returnVal;
	}

	@RequestMapping("/API/User/Pages/EditElement")
	public APIResponse editElement(@RequestParam(value="id", defaultValue="-1") int userID,
								@RequestParam(value="URL", defaultValue="null") String url,
								@RequestParam(value="element", defaultValue="null") String element){
		APIResponse returnVal = null;
		if (userID != -1 && !url.equals("null") && !element.equals("null")){
			try{
				ResultSet rawPageRS = dbInterface.getPageID(url);
				if (rawPageRS.next()){
					int pageID = rawPageRS.getInt("pk_id");
					ResultSet RS = dbInterface.getUserPage(userID, pageID);
					if (!RS.next()) 
						returnVal = new error(error.errorType.userNotMonitoringPage);
					
					else
					{
						pageState state = pageState.valueOf(RS.getString("type"));
						ResultSet ERS = dbInterface.getUserPageElement(userID, pageID);
						if (ERS.next()){
							String currentElement = ERS.getString(1);
							if (!currentElement.equals(element))
								removeWatcher(userID,pageID, state, element);
						}
						if (!element.equals("null") || !element.equals(""))
							addNewNotification(userID,pageID, state, element);
						else 
							enablePageNotification(userID, pageID, state);
						returnVal = new PageList(dbInterface.getUserPages(userID));
					}
				}
			}catch (SQLException e){
				returnVal = new error(e.getMessage());
				e.printStackTrace();
			}
		}
		return returnVal;
	}
	
	private int addNewPage(String url, String name) throws SQLException{
		dbInterface.addNewPage(url, name);
		ResultSet pageHolder = dbInterface.getPageID(url);
		pageHolder.next();
		int pageID = pageHolder.getInt("pk_id");
		BootStrap.monitor.addPage(pageID, new webPage(pageID,url, name));
		return pageID;
	}
	
	private void addNewNotification(int userID, int pageID, pageState state) throws SQLException{
		dbInterface.addUserToPage(userID, pageID, state);
		enablePageNotification(userID, pageID, state);
	}
	
	private void addNewNotification(int userID, int pageID, pageState state, String element) throws SQLException{
		dbInterface.addElementToUserPage(userID, pageID, element);
		ResultSet emailHolder = dbInterface.getUserEmail(userID);
		String emailAddress = emailHolder.getString("fdEmailAddress");
		//BootStrap.monitor.addWatcher(pageID, userID, state, new VerboseEmailNotification(emailAddress, state), element);
		BootStrap.monitor.addWatcher(pageID, userID, state, new EmailNotification(emailAddress));
	}
	
	private void enablePageNotification(int userID, int pageID, pageState state) throws SQLException{
		ResultSet emailHolder = dbInterface.getUserEmail(userID);
		String emailAddress = emailHolder.getString("fdEmailAddress");
		//BootStrap.monitor.addWatcher(pageID, userID, state, new VerboseEmailNotification(emailAddress, state));
		BootStrap.monitor.addWatcher(pageID, userID, state, new EmailNotification(emailAddress));
	}
	
	private void removeAllWatchers(int userID, int pageID) throws SQLException{
		dbInterface.removeUserFromPage(userID, pageID);
		BootStrap.monitor.removeAllWatchers(pageID, userID);
	}
	
	private void removeWatcher(int userID, int pageID, pageState state) throws SQLException{
		dbInterface.removeUserFromPage(userID, pageID, state);
		BootStrap.monitor.removeWatcher(pageID, userID, state);
	}
	
	private void removeWatcher(int userID, int pageID, pageState state, String element) throws SQLException{
		dbInterface.removeUserElementFromPage(userID, pageID);
		BootStrap.monitor.removeWatcher(pageID, userID, state, element);
	}
	
	private void removePage(int pageID) throws SQLException{
		dbInterface.removePage(pageID);
		BootStrap.monitor.removePage(pageID);
	}
	
	
}
