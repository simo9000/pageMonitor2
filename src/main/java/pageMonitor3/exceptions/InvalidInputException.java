package pageMonitor3.exceptions;

public class InvalidInputException extends Exception {
	
	private static final long serialVersionUID = 1L;
	public final String message;
	
	public InvalidInputException(){
		this.message = "";
	}
	
	public InvalidInputException(String message){
		this.message = message;
	}
	
}
