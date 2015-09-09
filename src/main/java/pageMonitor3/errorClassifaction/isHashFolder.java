package pageMonitor3.errorClassifaction;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import pageMonitor3.exceptions.InvalidInputException;
import pageMonitor3.monitor.HashUtilities;

public class isHashFolder implements FileFilter{

	@Override
	public boolean accept(File pathname) {
		//if (pathname.isDirectory() && doesNameParseToHash(pathname.getName().getBytes()))
		if (pathname.isDirectory() && doesFolderContainValidHashFile(pathname.toPath()))
			return true;
		else
			return false;
	}
	
	private boolean doesNameParseToHash(byte[] bs){
		String name = Arrays.toString(bs);
		boolean returnVal = true;
		try{
			HashUtilities.parseHash(name);
		} catch (InvalidInputException e){
			returnVal = false;
		}
		return returnVal;
	}
	
	private boolean doesFolderContainValidHashFile(Path path){
		byte[] bytes = getHashFromFile(path);
		if (bytes != null && bytes.length == HashUtilities.STANDARD_HASH_LENGTH)
			return true;
		else
			return false;
	}
	
	public static byte[] getHashFromFile(Path path){
		File hashFile = new File(path.toString() + "/hash");
		Path subPath = hashFile.toPath();
		byte bytes[] = null;
		try {
			bytes = Files.readAllBytes(subPath);
			return bytes;
		} catch (IOException e) {
			return null;
		}
	}

}
