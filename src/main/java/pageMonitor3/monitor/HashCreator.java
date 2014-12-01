package pageMonitor3.monitor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCreator {

	private final static String hashType = "MD5";
	
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
	
}
