package com.example.cmp464;

import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
		new AsyncTaskFeed(this).execute();
	}
	
}


