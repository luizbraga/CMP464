package com.example.cmp464;

import java.util.List;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
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
	List<Item> items = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv = (ListView) findViewById(R.id.feed_list);
		
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
		// Execute the async task
		if(!isNetworkAvailable(this)){
			Toast.makeText(this,"No Internet connection",Toast.LENGTH_LONG).show();
			finish();
		}else{
			new AsyncTaskFeed(this).execute();
		}
	}
	
	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
	        return true;
	    else
	        return false;
	}
	
}


