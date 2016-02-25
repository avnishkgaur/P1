package com.gaur.hiker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.gaur.hiker.library.JSONParser;

public class PlanActivity extends ListActivity {
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    ArrayList<HashMap<String, String>> arl;
    ArrayList<HashMap<String, String>> places = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> places_temp = new ArrayList<HashMap<String, String>>();
	
	Button btnSearch;
	ImageButton btnMaps;
	
	GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan);
		
        new PlanLocation().execute();
        
        // Get listview
        ListView lv = getListView();
        // on seleting single product
        // launching Edit Product Screen
        
       lv.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                            int position, long arg3) {
                    	Log.e("dsfas","Sadas");
                        gps = new GPSTracker(PlanActivity.this);
                        
                        // check if GPS enabled     
                        if(gps.canGetLocation()){
                             
                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();
                             
                            // \n is for new line
//                            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
                            		Uri.parse("http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr=20.5666,45.345"));
                            		startActivity(intent);
                        }else{
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            gps.showSettingsAlert();
                        }
                    }
                });        
        
       btnMaps = (ImageButton) findViewById(R.id.btnMaps);
       
       btnMaps.setOnClickListener(new View.OnClickListener() {
            
           public void onClick(View arg0) {
           	Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
           	String colat1 = arl.get(0).get("lat");
           	String colng1 = arl.get(0).get("lng");
           	String colat2 = arl.get(1).get("lat");
           	String colng2 = arl.get(1).get("lng");
           	String colat3 = arl.get(2).get("lat");
           	String colng3 = arl.get(2).get("lng");
           	String colat4 = arl.get(3).get("lat");
           	String colng4 = arl.get(3).get("lng");
           	intent.putExtra("url", "http://maps.google.com/maps/api/staticmap?center="+colat1+","+colng1+"&zoom=14&size=512x512&maptype=roadmap&markers=color:blue|label:1|"+colat2+","+colng2+"&markers=color:green|label:2|"+colat3+","+colng3+"&markers=color:red|color:red|label:3|"+colat4+","+colng4+"&sensor=false&key=AIzaSyAvmWGiF8rG2WtbQIj-itHl7UbRbLnSG9A");
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
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
        case R.id.action_share:
        	Intent i=new Intent(android.content.Intent.ACTION_SEND);
        	i.setType("text/plain");
        	i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Interesting travel info");
        	i.putExtra(android.content.Intent.EXTRA_TEXT, "I would like to share with you this amazing app called Hiking");
        	startActivity(Intent.createChooser(i,"Share via"));
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class PlanLocation extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PlanActivity.this);
            pDialog.setMessage("Planning.. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * logging user in
         * */
        protected String doInBackground(String... args) {
             // Building Parameters
        	arl = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("selectedRespList");
        	String origin = arl.get(0).get("lat")+","+arl.get(0).get("lng");
        	String dest = arl.get(arl.size()-1).get("lat")+","+arl.get(arl.size()-1).get("lng");
        	String oTitle = arl.get(0).get("name");
        	String oAddr = arl.get(0).get("addr");
        	Log.e("as","DSAs");
        	TextView tv = (TextView)findViewById(R.id.fLocName);  
        	tv.setText(oTitle);
        	TextView tv1 = (TextView)findViewById(R.id.fLocAddr);  
        	tv1.setText(oAddr);
            String listString = "";
        	ArrayList<String> resp3 = new ArrayList<String>();
        	if(arl.size()>2){
        		int maxloop = 9;
        		if(arl.size()<10){
        			maxloop = arl.size()-1;
        		}
                for(int i =1; i<maxloop;i++){
                	HashMap<String, String> resp1 = arl.get(i);
                	resp3.add(resp1.get("lat")+","+resp1.get("lng"));
//                	resp3.add(resp1.get("lat")+","+resp1.get("lng"));
                }
                for (String s : resp3)
                {
                    listString += s + "|";
                }
            }
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            String route = "origin="+origin+"&destination="+dest+"&waypoints=optimize:true|"+listString;
            route.replace(" ", "+");
            Log.e("route string is",route);
            params.add(new BasicNameValuePair("params", route));
            JSONObject json = jsonParser.getJSONFromUrl("http://www.askmeprice.com/opt.php",params);
            try {
                JSONArray resp1 = json.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
                for(int i=0;i<resp1.length();i++){
                	JSONObject time2 = resp1.getJSONObject(i).getJSONObject("duration");
                	String time3 = time2.getString("text");
                	String time4 = time2.getString("value");
                	String daddr = resp1.getJSONObject(i).getString("end_address");
                	String dlat = resp1.getJSONObject(i).getJSONObject("end_location").getString("lat");
                	String dlng = resp1.getJSONObject(i).getJSONObject("end_location").getString("lng");
                	String dname = "";
                	Float lowest = (float) 1.0;
                	for(int j=0;j<arl.size();j++){
                		HashMap<String, String> resp2 = arl.get(j);
                		Float temp1 = Float.parseFloat(resp2.get("lat"));
                		Float temp2 = Float.parseFloat(resp2.get("lng"));
                		Float temp3 = Float.parseFloat(dlat);
                		Float temp4 = Float.parseFloat(dlng);
                		Float temp5 = (Math.abs(temp3-temp1)+Math.abs(temp4-temp2));
                		if(temp5<lowest){
                			dname = resp2.get("name");
                			lowest = temp5;
                		}
//                		if(resp2.get("lat").equals(dlat)){
//                			dname = resp2.get("name");
//                		}                		
                	}
                	HashMap<String, String> tp = new HashMap<String, String>();
                	tp.put("time_taken", time3+" ");
                	tp.put("dist", "");
                	tp.put("reach_at", "7:45 AM");
                	tp.put("destination", dname);
                	tp.put("dest_addr", daddr);
                	places.add(tp);
                }
            	// create a for loop, storing time for each place
                // layot would be 1. reach at 7 am. 2. TITLE OF LOCATION 3. Adress in small 4. spend 30 minutes here on side. 5. down arrow with minutes 
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                   ListView myList=(ListView)findViewById(android.R.id.list);
                	
                   ListAdapter adapter = new SimpleAdapter(
                    		getApplicationContext(), places,
                            R.layout.plan_row, new String[] { "time_taken", "reach_at",
                                    "destination", "dest_addr"},
                            new int[] { R.id.timeToReach, R.id.timeNow, R.id.LocName, R.id.LocAddr });
                   myList.setAdapter(adapter);
                }
            });


        }

    }
}
