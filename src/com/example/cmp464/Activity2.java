package com.example.cmp464;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Activity2 extends Activity{

	Button button;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two);
		
		
		button = (Button) findViewById(R.id.button_activity2);
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Activity2.this, Activity3.class);
				startActivity(intent);
			}
			
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Activity2.this);
		
		if(pref.getBoolean("alternative_activity2", false)){
			Log.i("CMP", "A2_Alternative Activity 2");
			button.setTextAppearance(getApplicationContext(), R.style.buttonAlternative);
			button.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
		}else{
			button.setTextAppearance(getApplicationContext(), R.style.buttonStandart);
		}
		
	}
	
	
	
}
