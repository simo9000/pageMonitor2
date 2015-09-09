package pageMonitor3.monitor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import pageMonitor3.exceptions.InvalidInputException;

public class HashUtilities {

	private final static String hashType = "MD5";
	public final static int STANDARD_HASH_LENGTH = 16;
	
	public static byte[] createHash(String item){
		byte[] returnVal = null;
		try {
			MessageDigest md = MessageDigest.getInstance(hashType);
			byte[] data = item.getBytes();
			md.update(data);
			byte[] hash = md.digest();
			returnVal = hash;
		} catch (NoSuchAlgorithmException e) {
		}
		return returnVal;
	}
	
	public static byte[] parseHash(String hash) throws InvalidInputException{
		
		// check for non-null input
		if (hash == null) throw new InvalidInputException();
		
		// Split string into parsable units
		String[] byteValues = hash.substring(1, hash.length() - 1).split(",");
		
		// check for standard hash length
		if (byteValues.length !=  STANDARD_HASH_LENGTH) throw new InvalidInputException();
				
		// create and populate return value
		byte[] returnVal = new byte[byteValues.length];
		try{
			for (int i=0, len=returnVal.length; i<len; i++) 
				   returnVal[i] = Byte.parseByte(byteValues[i].trim());
		}
		catch (NumberFormatException e){
			throw new InvalidInputException();
		}
		     
		return returnVal;
	}
	
}
