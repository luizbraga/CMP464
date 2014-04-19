package com.example.cmp464;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import utils.NewsManager;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
/**
 * Main activity that will download the RSS from The New York Times
 * and then put into a ListView
 * @author luizbraga
 *
 */
public class MainActivity extends Activity {
	
	ListView lv;
	List<Item> items = new ArrayList<Item>();
	
	private BroadcastReceiver receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				// Receive the extra from the service
				boolean updateFlag = bundle.getBoolean("update");
				if(updateFlag){
					Log.d("CMP464", "Feed updated");
					loadFields(context);
				}
				
			}
			
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		lv = (ListView) findViewById(R.id.feed_list);
		
		// Restart the service
		Calendar cal = Calendar.getInstance();

		Intent intent = new Intent(this, NewsDownloaderService.class);
		PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

		AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		// Start every 30 seconds
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent); 
		// End of restart service
		
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this,Activity2.class);
				intent.putExtra("description", items.get(position).getDescription());
				intent.putExtra("link", items.get(position).getLink());
				
				startActivity(intent);
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		// Handle the force close if doesn't have internet connection
		if(!isNetworkAvailable(this)){
			Toast.makeText(this,"No Internet connection",Toast.LENGTH_LONG).show();
			finish();
		}else{
			// Load the fields with data stored in database
			loadFields(this);
			// Create and load the database if it doesn't exists
			if(items.size() == 0){
				new AsyncTaskFeed(this).execute();
			}
			
		}
	}
	
	private void loadFields(Context context) {
		// Get the values in the Database
		NewsManager db = new NewsManager(context);
		items.clear();
		items = db.getAllItems();
		
		// Create a String array to put into the ListView
		final String[] titles = new String[items.size()];
		for(int i=0;i<items.size();i++){
			titles[i]=items.get(i).getTitle();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, titles);
		lv.setAdapter(adapter);
	}

	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
	        return true;
	    else
	        return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		registerReceiver(receiver, new IntentFilter("com.example.cmp464"));
	}

	@Override
	protected void onPause() {
		super.onPause();
		try{
			unregisterReceiver(receiver);
		}catch(IllegalArgumentException e){
			
		}
	}
	
}


