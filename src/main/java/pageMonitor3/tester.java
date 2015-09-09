package pageMonitor3;

import org.jsoup.parser.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class tester {
	private final static String dir = System.getProperty("user.dir");

	public static void main (String args[]){
		try {
			String message1 = getMessage(new File(dir + "/testPages/snowPants3.txt"));
			Parser p1 = Parser.htmlParser();
			p1.setTrackErrors(1000);
			p1.parseInput(message1, "message1");
			int p1ECount = p1.getErrors().size();
			
			
			String message2 = getMessage(new File(dir + "/testPages/snowPants2.txt"));
			Parser p2 = Parser.htmlParser();
			p2.setTrackErrors(1000);
			p2.parseInput(message2, "message2");
			int p2ECount = p2.getErrors().size();
			
			String end = "ends";
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	private static String getMessage(File file) throws IOException{
		String returnVal = "";
		List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
		for (String line : lines)
			returnVal += line;
		return returnVal;
	}
}
