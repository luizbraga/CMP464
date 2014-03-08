package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {
	
	/**
	 * Method responsible to download the XML file
	 * @return
	 * @throws IOException
	 */
	public static String getFeed() throws IOException{
		InputStream input = null;
		String rssFeed = null;
		try {
			URL url = new URL("http://rss.nytimes.com/services/xml/rss/nyt/Technology.xml");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			input = connection.getInputStream();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			for(int i; (i = input.read(buffer)) != -1;  ){
				output.write(buffer,0,i);
			}
			byte[] response = output.toByteArray();
			rssFeed = new String(response, "UTF-8");
		}  finally{
			if(input != null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return rssFeed;
	}
}
