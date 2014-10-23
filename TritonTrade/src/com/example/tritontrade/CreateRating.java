package com.example.tritontrade;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class CreateRating extends Activity {
	
	TextView seller_name;
	EditText comment;
	Button submit, back;
	RatingBar r;
	Intent i;
	JSONParser jpar = new JSONParser();
	JSONObject jobj;
	JSONArray jarray;
	private ProgressDialog pDialog;
	private static final String TAG_SELLER_ID = "seller_id";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_RATING = "rating";
	private static final String TAG_COMMENT = "comment";
	private static final String TAG_SELLERNAME = "sellername";
	private static String url_read_user = "http://tritontrade.org/TritonTradeCRUD/read_user.php";
	private static String url_create_rating = "http://tritontrade.org/TritonTradeCRUD/create_rating.php";
	
	private void initialize(){
		comment = (EditText) findViewById(R.id.creatrating_comment);
		submit = (Button) findViewById(R.id.creatingrating_submit);
		back = (Button) findViewById(R.id.creatingrating_back);
		r = (RatingBar) findViewById(R.id.createRating);
		seller_name = (TextView) findViewById(R.id.createrating_sellerName);
		i = getIntent();
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createrating);
		initialize();
		
		new GetSellerInfo().execute();
		
		submit.setOnClickListener(new View.OnClickListener (){
			
			 public void onClick(View v){
				 new PostRating().execute();
			 }
		});
		
		back.setOnClickListener(new View.OnClickListener (){
			
			 public void onClick(View v){
				 Intent bk = new Intent(CreateRating.this, Homepage.class);
				 bk.putExtra("user_id", i.getStringExtra("user_id"));
				 startActivity(bk);
			 }
		});
	}

	class GetSellerInfo extends AsyncTask<String, String, String>{

		protected void onPreExecute() {
			
			super.onPreExecute();
			pDialog = new ProgressDialog(CreateRating.this);
			pDialog.setMessage("Gathering Seller's Data...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			
		}	
		@Override
		protected String doInBackground(String... params) {
			
			List<NameValuePair> seller = new ArrayList<NameValuePair>();
        	seller.add(new BasicNameValuePair("user_id",i.getStringExtra(TAG_SELLER_ID)));
        	jobj = jpar.makeHttpRequest(url_read_user, "GET", seller);
        	
        	runOnUiThread(new Runnable() {
                public void run() {

                	try {
				
                		int success = jobj.getInt(TAG_SUCCESS);
                		
                		//Log.d("success: ", success);
			
                		if(success == 1 ) {
                			jarray = jobj.getJSONArray("user");
                			                
                			JSONObject s = jarray.getJSONObject(0);
                			
                			seller_name = (TextView) findViewById(R.id.createrating_sellerName);
					
                			seller_name.setText(s.getString(TAG_USERNAME));

                		}
                		else {
                			Intent oops = new Intent(getApplicationContext(), Homepage.class);
                			oops.putExtra("user_id", i.getStringExtra("user_id"));
                			oops.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                			startActivity(oops);
                		}
                	}
                	catch (JSONException e){
                		e.printStackTrace();
                	} 				
                }	
			});
                
			return null;
		}
		protected void onPostExecute(String file_url ) {
			pDialog.dismiss();
		
		}	
	}
	
	class PostRating extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... args) {
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			//Log.d("creating rating for user_id: ",i.getStringExtra("seller_id"));
			params.add(new BasicNameValuePair("user_id", i.getStringExtra("seller_id")));
			
			//Log.d("creating rating with a rating of: ",Float.toString(r.getRating()));
			params.add(new BasicNameValuePair(TAG_RATING, Float.toString(r.getRating()) ));
			
			//Log.d("creating rating with a comment of: ",comment.getText().toString());
			params.add(new BasicNameValuePair(TAG_COMMENT, comment.getText().toString() ));
			
			jobj = jpar.makeHttpRequest(url_create_rating, "GET", params);
			
			
			//Log.d("creating rating post: ", jobj.toString());
			
			try{
				int success = jobj.getInt(TAG_SUCCESS);
				if (success == 1){
					Intent ok = new Intent(CreateRating.this, RateMe.class);
					ok.putExtra("return", "home");
					ok.putExtra("user_id", i.getStringExtra("user_id"));
					ok.putExtra(TAG_SELLERNAME, seller_name.getText());
					ok.putExtra(TAG_SELLER_ID,i.getStringExtra(TAG_SELLER_ID) );
					startActivity(ok);
				}
			} catch (JSONException e) {
                e.printStackTrace();
            }
			return null;
		}
		
	}

}
