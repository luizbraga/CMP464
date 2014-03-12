package com.example.cmp464;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
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
		
		WebView descTV = (WebView) findViewById(R.id.feed_description);
		TextView descLink = (TextView) findViewById(R.id.feed_link);
		
		descTV.loadData(descr, "text/html", null);
		descLink.setText(lnk);
	}
		
	
	
}
