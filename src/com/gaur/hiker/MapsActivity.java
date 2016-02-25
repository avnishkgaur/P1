package com.gaur.hiker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class MapsActivity extends Activity {
	
	Button btnSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		Bundle extras = getIntent().getExtras();
    	String url = "http://maps.google.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=14&size=512x512&maptype=roadmap&markers=color:blue|label:S|40.702147,-74.015794&markers=color:green|label:G|40.711614,-74.012318&markers=color:red|color:red|label:C|40.718217,-73.998284&sensor=false&key=AIzaSyAvmWGiF8rG2WtbQIj-itHl7UbRbLnSG9A";
    	if (extras != null) {
    	    url = extras.getString("url");        	    
    	} 
        int loader = R.drawable.loader;
        ImageView image = (ImageView) findViewById(R.id.mapImage);
        String image_url = url;
        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
        imgLoader.DisplayImage(image_url, loader, image);
	}

}
