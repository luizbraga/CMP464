package com.example.cmp464;

import java.io.IOException;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;
import utils.Downloader;
import utils.NewsManager;
import utils.PullParser;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;


public class AsyncTaskFeed extends AsyncTask<Void,Void,List<Item>>{
	MainActivity mainActivity;
	
	public AsyncTaskFeed(MainActivity mainActivity){
		this.mainActivity = mainActivity;
	}
	
	
	@Override
	protected List<Item> doInBackground(Void... arg0) {
		List<Item> result = null;
		try {
			 String feed = Downloader.getFeed();
			 result = PullParser.parse(feed);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	
	@Override
	protected void onPostExecute(final List<Item> result) {
		super.onPostExecute(result);
		
		// Create a String array to put into the ListView
		final String[] titles = new String[result.size()];
		for(int i=0;i<result.size();i++){
			titles[i]=result.get(i).getTitle();
		}
		
		if(result != null){
			NewsManager db = new NewsManager(mainActivity);
			// Set the array of items in the MainActivity class
			db.dropTable();
			db.addAllNews(result);
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity,android.R.layout.simple_list_item_1, titles);
			mainActivity.items = result;
			mainActivity.lv.setAdapter(adapter);
			
		}
	}

}
