package com.example.cmp464;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import utils.Downloader;
import utils.NewsManager;
import utils.PullParser;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


/**
 * Class that provides the service and after download the RSS will check if the date is after the newest news
 * @author luizbraga
 *
 */
public class NewsDownloaderService extends Service {
	
	List<Item> items = new ArrayList<Item>();
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new DownloaderServiceTask(this).execute();
		
		return Service.START_FLAG_REDELIVERY;
	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public void onListReceived(List<Item> items){
		this.items = items;
		verifyDate();
	}
	
	
	public void verifyDate(){
		boolean updateFlag = false;
		NewsManager db = new NewsManager(this);
		
		Date newestInDB = db.getNewestItemDate();
		for(Item item : items){
			if(item.getDate().after(newestInDB)){
				Log.d("CMP464", "UPDATE IT!");
				updateFlag = true;
			}
		}
		
		if(updateFlag){
			Log.d("CMP464", "Feed will be updated");
			db.dropTable();
			db.addAllNews(items);
			PendingIntent pintent = PendingIntent.getActivity(
					this, 0, new Intent(this, MainActivity.class), 0);
			NotificationCompat.Builder noti = new NotificationCompat.Builder(this)
											.setSmallIcon(R.drawable.ic_launcher)
											.setContentIntent(pintent)
											.setContentTitle("Feed updated")
											.setContentText("Your feed receiver has been updated")
											.setAutoCancel(true);
			NotificationManager nmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			nmanager.notify(0, noti.build());
			
			sendBroadcast("com.example.cmp464", updateFlag);
		}
	}
	
	
	private void sendBroadcast(String path, boolean updateFlag) {
		Intent intent = new Intent(path);
		intent.putExtra("update", updateFlag);
		sendBroadcast(intent);
		
	}


	private class DownloaderServiceTask extends AsyncTask<Void, Void, List<Item>>{
		


		private NewsDownloaderService service;
		
		public DownloaderServiceTask(NewsDownloaderService service){
			this.service = service;
		}
		
		@Override
		protected List<Item> doInBackground(Void... arg0) {
			List<Item> result = new ArrayList<Item>();
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
		protected void onPostExecute(List<Item> result) {
			if (result.size() == 0) {
                Log.w("CMP464", "XML download and parse had errors");
            }else{
            	Log.w("CMP464", "XML downloaded and parsed - Size: "+result.size());
            }
			service.onListReceived(result);
		}
		
	}
	

}
