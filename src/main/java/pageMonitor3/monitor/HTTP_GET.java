package pageMonitor3.monitor;

import java.io.*;
import java.net.*;

import org.jsoup.parser.*;
import org.jsoup.nodes.*;


public class HTTP_GET {
		public static String getHTML(String urlToRead) throws Exception{
			URL url;
			HttpURLConnection conn;
			BufferedReader rd;
			String line;
			String result = "";
				url = new URL(urlToRead);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
				conn.setRequestMethod("GET");
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    	while ((line = rd.readLine()) != null) 
		    	result += line;
		     	rd.close();
			return result;
		}
		
		public static String getElementText(String urlToRead, String id) throws Exception{
			return getElementText(getHTML(urlToRead),urlToRead,id);
		}
		
		public static String getElementText(String message, String URL, String id) throws Exception{
			Document doc;
			String returnVal = null;
			Parser parser = Parser.htmlParser();
			doc = parser.parseInput(message, URL);
			Element e = doc.getElementById(id);
			if (e != null)returnVal = "" + e.toString();
			return returnVal;
		}
}
