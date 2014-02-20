package com.example.cmp464;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Button button;
	
	int padding;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get 25dp in pixels according to the size of the screen
		padding = 25 * (int)(getResources().getDisplayMetrics().density);
		
		button = (Button) findViewById(R.id.button_activity1);
		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, Activity2.class);
				startActivity(intent);
			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		Intent intent = new Intent(this,SettingsActivity.class);
		startActivityForResult(intent, 0);
		
		return super.onOptionsItemSelected(item);
		
	}
	

	@Override
	protected void onStart() {
		super.onStart();
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		
		if(pref.getBoolean("alternative", false)){
			Log.i("CMP", "Alternative Style");
			button.setTextAppearance(getApplicationContext(), R.style.buttonAlternative);
			button.setPadding(padding, padding, padding, padding);
		}else{
			button.setTextAppearance(getApplicationContext(), R.style.buttonStandart);
			button.setPadding(padding, padding, padding, padding);
		}
		
		
	}

}
