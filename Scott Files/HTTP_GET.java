package pageMonitor2;

import java.io.*;
import java.net.*;


public class HTTP_GET {
		public static String getHTML(String urlToRead) throws Exception{
			URL url;
			HttpURLConnection conn;
			BufferedReader rd;
			String line;
			String result = "";
				url = new URL(urlToRead);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    	while ((line = rd.readLine()) != null) 
		    	result += line;
		     	rd.close();
			return result;
		}
}
