package com.gaur.hiker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	Button btnSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        int loader = R.drawable.loader;
        btnSearch = (Button) findViewById(R.id.btnSearch);
        
        btnSearch.setOnClickListener(new View.OnClickListener() {
             
            public void onClick(View arg0) {
            	EditText keyword = (EditText) findViewById(R.id.query);
                String query = keyword.getText().toString();
            	Log.e("search button pressed", "query is"+query);
            	Intent intent = new Intent(getApplicationContext(), GetLocationActivity.class);
            	intent.putExtra("query", query);
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

}
