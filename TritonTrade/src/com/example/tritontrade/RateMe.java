package com.example.tritontrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.tritontrade.SearchClass.LoadClass;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.view.View.OnTouchListener;

public class RateMe extends ListActivity {

	RatingBar rb;
	Intent i;
	TextView seller_name;
	Button return_page;
	private ProgressDialog pDialog;
	JSONParser jpar = new JSONParser();
	JSONObject jobj;
	JSONArray jarray;
	ArrayList<HashMap<String, String>> ratingList;
	private static String url_read_rating = "http://tritontrade.org/TritonTradeCRUD/read_user_rating.php";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rateme);
		i = getIntent();
		seller_name = (TextView) findViewById(R.id.ratemesellername);
		seller_name.setText(i.getStringExtra("sellername"));
		ratingList = new ArrayList<HashMap<String, String>>();
		rb = (RatingBar) findViewById(R.id.ratingBar1);
		
		new GetRating().execute();
		
		rb.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {	
				return true;
			}
		});
		
		return_page = (Button) findViewById(R.id.ratemereturn);
		
		return_page.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			
				if(i.getStringExtra("return").equals("home")){
					Intent home = new Intent(RateMe.this, Homepage.class);
					home.putExtra("user_id", i.getStringExtra("user_id"));
					startActivity(home);
				}
				else{
					Intent sell = new Intent(RateMe.this, BookInfo.class);
					sell.putExtra("bookname",i.getStringExtra("bookname"));
					sell.putExtra("price", i.getStringExtra("price"));
					sell.putExtra("condition", i.getStringExtra("condition"));
					sell.putExtra("description", i.getStringExtra("description"));
					sell.putExtra("seller_id", i.getStringExtra("seller_id"));
					sell.putExtra("status",i.getStringExtra("status"));
					sell.putExtra("user_id",i.getStringExtra("user_id"));
					startActivity(sell);
					
					//sell.putExtra();
				}	
				
			}
		});
	}
	
	class GetRating extends AsyncTask<String, String, String>{
		
		double avgRating;
		
		protected void onPreExecute() {
			
			super.onPreExecute();
			pDialog = new ProgressDialog(RateMe.this);
			pDialog.setMessage("Gathering Seller's Rating & Comments...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			
		}

		protected String doInBackground(String... params) {
			
			List<NameValuePair> seller = new ArrayList<NameValuePair>();
        	seller.add(new BasicNameValuePair("user_id",i.getStringExtra("seller_id") ));
        	jobj = jpar.makeHttpRequest(url_read_rating, "GET", seller);
        	//Log.d("seller info: ", jobj.toString());
			
        	try {
        		int success = jobj.getInt("success");
        		
        		if (success == 1){
        			jarray = jobj.getJSONArray("rating");
        			
        			//Log.d("array length: ", Integer.toString(jarray.length()));
        			
        			for (int i = 0; i < jarray.length(); i++){
        				
        				
        				JSONObject c = jarray.getJSONObject(i);
        				String rating = Double.toString(c.getDouble("rating"));
        				String comment = c.getString("comment");
        				
        				HashMap<String, String> map = new HashMap<String, String>();
        			         				       				
        				map.put("rating", rating);
        				map.put("comment", comment);
        				
        				ratingList.add(map);
        				       			
        				avgRating = avgRating + c.getDouble("rating");
        			}
        			
        			avgRating = avgRating / jarray.length();
        			//Log.d("avgRating", Double.toString(avgRating));
        			
        		}
        		else{
        			 pDialog.dismiss();
        		}
        	}
			catch (JSONException e){
				e.printStackTrace();
			}
			return null;
		}
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            
            runOnUiThread(new Runnable() {
                public void run() {
                	
                	//Log.d("value of avgrating", Double.toString(avgRating));
                	
                	rb.setRating((float) avgRating);
                	
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                	//Log.d("DID u do the adapter","fuck u");
                    ListAdapter adapter = new SimpleAdapter( RateMe.this, ratingList,R.layout.ratinglist, 
                    		new String[] { "comment", "rating"},
                            new int[] { R.id.rating_comment, R.id.rating_num  });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
        }
	}
}
