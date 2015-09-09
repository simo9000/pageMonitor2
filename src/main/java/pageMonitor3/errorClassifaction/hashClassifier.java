package pageMonitor3.errorClassifaction;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import pageMonitor3.BootStrap;
import pageMonitor3.exceptions.InvalidInputException;
import pageMonitor3.monitor.HashUtilities;

public class hashClassifier {

	private Dictionary<hashWrapper,File> errorList;
	private final static String dir = System.getProperty("user.dir");

	public hashClassifier() throws InvalidInputException{
		File parent = new File(dir + "/errors/catalog");
		File directories[] = parent.listFiles(new isHashFolder());
		errorList = new Hashtable<hashWrapper,File>();
		for (int i = 0; i < directories.length; i++){
			byte[] bytes = isHashFolder.getHashFromFile(directories[i].toPath());
			errorList.put(new hashWrapper(bytes), directories[i]);
		}					
	}

	public boolean isHashKnownError(byte[] hash){
		File knownErrorHash = errorList.get(new hashWrapper(hash));
		if (errorList == null || errorList.isEmpty() || knownErrorHash == null)
			return false;
		else
			return true;
	}
	
	
	public static void main(String[] args) {
		File newErrors = new File(dir + "/errors/new");	
		File canidates[] = newErrors.listFiles(new isNewError());
		if (canidates != null && canidates.length > 0){
				hashClassifier classifier = getClassifier();
				for (int i = 0; i < canidates.length; i++){
					classifier.readNewErrorFile(canidates[i]);
				}
		}
	}
	
	private synchronized void readNewErrorFile(File canidate){
		try {
			List<String> newContent = Files.readAllLines(canidate.toPath(),Charset.defaultCharset());
			String message = "";
			for (String line : newContent)
				message += line;
			byte newHash[] = HashUtilities.createHash(message);
			if (!isHashKnownError(newHash)){
				ByteBuffer wrapper = ByteBuffer.wrap(newHash);
				short entryName = (short) wrapper.getShort();
				File newHashEntry = new File(dir + "/errors/catalog/" + entryName);
				Path newHashEntryPath = newHashEntry.toPath();
				Files.createDirectory(newHashEntryPath, new FileAttribute[0]);
				File newArchiveFile = new File(dir +"/errors/catalog/" + entryName + "/originalMessage.html" );
				Files.write(newArchiveFile.toPath(), message.getBytes(), StandardOpenOption.CREATE);
				File hashFile = new File(dir + "/errors/catalog/" + entryName + "/hash");
				Files.write(hashFile.toPath(), newHash, StandardOpenOption.CREATE);
				errorList.put(new hashWrapper(newHash), newHashEntry);
				Files.delete(canidate.toPath());
			}		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static hashClassifier getClassifier(){
		hashClassifier returnVal = null;
		if (BootStrap.hashErrorClassifier != null)
			returnVal = BootStrap.hashErrorClassifier;
		else
		{
			try {
				returnVal = new hashClassifier();
			} catch (InvalidInputException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
		return returnVal;
	}
}
