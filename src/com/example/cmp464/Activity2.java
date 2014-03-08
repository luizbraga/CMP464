package com.example.cmp464;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Activity that contains the description and link for the selected item
 * @author luizbraga
 *
 */
public class Activity2 extends Activity{

		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two);
		
		Bundle bundle = getIntent().getExtras();
		String descr = bundle.getString("description");
		String lnk = bundle.getString("link");
		
		TextView descTV = (TextView) findViewById(R.id.feed_description);
		TextView descLink = (TextView) findViewById(R.id.feed_link);
		
		descTV.setText(descr);
		descLink.setText(lnk);
	}
		
	
	
}
