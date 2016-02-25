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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.gaur.hiker.library.JSONParser;

public class GetLocationActivity<ViewGroup> extends ListActivity {
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
	
    ArrayList<HashMap<String, String>> respList;
	Button btnSearch;
	Button btnPlan;
	String query;
	String lat;
	String lng;
	ArrayList<Integer> checkedPositions = new ArrayList<Integer>();
    ArrayList<HashMap<String, String>> selectedLocs = new ArrayList<HashMap<String, String>>();

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
        // Hashmap for ListView
        respList = new ArrayList<HashMap<String, String>>();
		Bundle extras = getIntent().getExtras();
    	query = "";
    	if (extras != null) {
    	    query = extras.getString("query").toUpperCase();        	    
    	} 
    	TextView tv = (TextView)findViewById(R.id.loc_query);  
    	tv.setText(query);
    	new SearchLocation().execute();
 
        btnPlan = (Button) findViewById(R.id.btnPlanTrip);
        
        btnPlan.setOnClickListener(new View.OnClickListener() {
             
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent plan = new Intent(getApplicationContext(), PlanActivity.class);
                plan.putExtra("selectedRespList", selectedLocs);
                plan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Closing dashboard screen
                startActivity(plan);
            }
        });

        // Get listview
        ListView lv = getListView();
        // on seleting single product
        // launching Edit Product Screen
        
       lv.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                            int position, long arg3) {
                    	Log.e("dsfas","Sadas");
//                        int count = arg0.getCount();
//                        Log.e("count is ",String.valueOf(count));
                    	HashMap<String, String> tempmap = new HashMap<String, String>();
                    	
                                                             	
                        // getting values from selected ListItem
                        String tname = ((TextView) view.findViewById(R.id.LocName)).getText().toString();
                        String tlat = ((TextView) view.findViewById(R.id.LocLat)).getText().toString();
                        String tlng = ((TextView) view.findViewById(R.id.LocLng)).getText().toString();
                        String trat = ((TextView) view.findViewById(R.id.LocRating)).getText().toString();
//                        String ticon = ((TextView) view.findViewById(R.id.LocIcon)).getText().toString();
                        String taddr = ((TextView) view.findViewById(R.id.LocAddr)).getText().toString();
                        // creating new HashMap
                        Boolean texists = false;
                        int j = 0;
                    	for(int i=0;i<selectedLocs.size();i++){
                    		tempmap = selectedLocs.get(i);
                    		Log.e("sa dasdasd",tempmap.get("addr")+"---"+taddr);
                    		if(tempmap.get("addr").equals(taddr)){
                    			texists = true;
                    			j=i;
                    		}
                    	}
                    	if(texists){
                    		selectedLocs.remove(j);
                            ImageView imageView=(ImageView) view.findViewById(R.id.LocCheckImg);
                            imageView.setImageResource(R.drawable.unchecked);                    		                    		
                    	} else{
                            HashMap<String, String> tmap = new HashMap<String, String>();
                            
                            // adding each child node to HashMap key => value
                            tmap.put("lat", tlat);
                            tmap.put("lng", tlng);
                            tmap.put("name", tname);
                            //tmap.put("icon", ticon);
                            tmap.put("rating", trat);
                            tmap.put("addr", taddr);
     
                            // adding HashList to ArrayList
                            selectedLocs.add(tmap);
                            ImageView imageView=(ImageView) view.findViewById(R.id.LocCheckImg);
                            imageView.setImageResource(R.drawable.checked);                    		
                    	}
                        Log.i("TAG", "" + tname);                    
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
    class SearchLocation extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GetLocationActivity.this);
            pDialog.setMessage("Searching.. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * logging user in
         * */
        protected String doInBackground(String... args) {
             // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            String loc = query.replace(" ", "+");
            params.add(new BasicNameValuePair("address", loc));
//            params.add(new BasicNameValuePair("sensor", "false"));
            Log.e("params are","name - "+loc);
            lat="";
            lng="";
            JSONObject json = jsonParser.getJSONFromUrl("http://www.askmeprice.com/places.php",params);
            try {
//                    loginErrorMsg.setText("");
                    JSONArray resp1 = json.getJSONArray("results");
                    // looping through All Products
                    for (int i = 0; i < resp1.length(); i++) {
                        JSONObject c = resp1.getJSONObject(i);
                        JSONObject r2 = c.getJSONObject("geometry").getJSONObject("location");
                        
                        // Storing each json item in variable
                        String rlat = "";
                        String rlng = "";
                        String rname = "";
                        String ricon = "";
                        String rrating = "";
                        String raddr = "";
                        if(!r2.isNull("lat")){
                            rlat = r2.getString("lat");                        	
                        }
                        if(!r2.isNull("lng")){
                            rlng = r2.getString("lng");                        	
                        }
                        if(!c.isNull("photos")){
                        	JSONArray photoc = c.getJSONArray("photos");
                        	JSONObject photoref = photoc.getJSONObject(0);
                        	if(!photoref.isNull("photo_reference")){
                        		 ricon = photoref.getString("photo_reference"); 
                        	}              	
                        }
                        if(!c.isNull("name")){
                            rname = c.getString("name");                        	
                        }
                        if(!c.isNull("rating")){
                            rrating = c.getString("rating");                        	
                        }
                        if(!c.isNull("formatted_address")){
                            raddr = c.getString("formatted_address");                        	
                        }
                        Log.e("loc name is ", rname);
                        Log.e("loc lat is ", rlat);
                        Log.e("loc lng is ", rlng);
                        Log.e("loc rating is ", rrating);
                        Log.e("loc icon is ", ricon);
                        Log.e("loc addr is ", raddr);
                                               
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put("lat", rlat);
                        map.put("lng", rlng);
                        map.put("name", rname);
                        map.put("icon", ricon);
                        map.put("rating", rrating);
                        map.put("addr", raddr);
 
                        // adding HashList to ArrayList
                        respList.add(map);
                    }
//                    JSONObject resp2 = resp1.getJSONObject(0);
//                    JSONObject resp3 = resp2.getJSONObject("geometry").getJSONObject("location");
//                    lat = resp3.getString("lat");
//                    lng = resp3.getString("lng");
//                    Log.e("lat is "+lat,"lng is "+lng);
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

            // Loader image - will be shown before loading image
            int loader = R.drawable.loader;
            ImageView image = (ImageView) findViewById(R.id.imageView2);
            String image_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+respList.get(0).get("icon")+"&sensor=false&key=AIzaSyCL9wobkQB_x4yy5pmQ3GtqJsQpgzZinAU";
            ImageLoader imgLoader = new ImageLoader(getApplicationContext());
            imgLoader.DisplayImage(image_url, loader, image);
//            ImageView imageView = (ImageView) findViewById(R.id.imageView2); 
    		Log.e("url is",image_url);
//    		ImageLoader imageLoader = ImageLoader.getInstance(); 
//    		imageLoader.init(ImageLoaderConfiguration.createDefault(context));              
//    		imageLoader.displayImage(imageUrl, imageView);
    		
    		// updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                   ListView myList=(ListView)findViewById(android.R.id.list);

                   /*
                   myList.setChoiceMode(CHOICE_MODE_MULTIPLE);
                   ArrayAdapter<HashMap<String, String>> adapter = new ArrayAdapter<HashMap<String, String>>(GetLocationActivity.this, android.R.layout.simple_spinner_item,
                		   R.id.text1, respList){
                        public View getView(int position, View convertView, ViewGroup parent) {
                           View rowView = super.getView(position, convertView, (android.view.ViewGroup) parent);
                           final HashMap<String, String> item = getItem(position);
                           TextView firstText = (TextView) rowView.findViewById(R.id.LocName);
                           firstText.setText(item.get("name"));
                           TextView secondText = (TextView) rowView.findViewById(R.id.LocRating);
                           secondText.setText(item.get("rating"));
                           return rowView;
                       }
                	   
                   };
                	*/
                   ListAdapter adapter = new SimpleAdapter(
                    		getApplicationContext(), respList,
                            R.layout.loc_row, new String[] { "name", "rating",
                                    "addr", "lat", "lng"},
                            new int[] { R.id.LocName, R.id.LocRating, R.id.LocAddr, R.id.LocLat, R.id.LocLng });
                            
                   myList.setAdapter(adapter);
                }
            });

        }

    }
}
