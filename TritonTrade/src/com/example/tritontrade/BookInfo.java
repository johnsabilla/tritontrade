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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;


public class BookInfo extends Activity {
	TextView bokname, bokprice, bokcondition, bokdescription, bokseller;
	RatingBar rb;
	TextView c_name, c_email, s_email;
	String bname, bprice, bcondition, bdescription, bsellerid, bsellername, bselleremail, status, bookid; 
	String curr_user_id, curr_user_name, curr_user_email, seller_name;
	Button notify,changeStatus, edit, delete;
	Intent i;
	private ProgressDialog pDialog;
	JSONParser jpar = new JSONParser();
	JSONObject jobj;
	JSONParser jpar_user = new JSONParser();
	JSONObject jobj_user;
	JSONParser jpar_seller = new JSONParser();
	JSONObject jobj_seller;
	JSONArray jarray_user;
	JSONArray jarray_seller;
	
	
	private static final String TAG_USERNAME = "username";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_BOOKNAME = "bookname";
	private static final String TAG_PRICE = "price";
	private static final String TAG_CLASS_ID = "class_id";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_USER_ID = "user_id";
	private static final String TAG_BOOK_ID = "book_id";
	private static final String TAG_SELLER_ID = "seller_id";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_CONDITION = "condition";
	private static final String TAG_STATUS = "status";
	private static String url_read_user = "http://tritontrade.org/TritonTradeCRUD/read_user.php";
	private static String url_email_seller = "http://tritontrade.org/TritonTradeCRUD/email_seller.php";
	private static String url_book_status = "http://tritontrade.org/TritonTradeCRUD/update_book_status.php";
	private static String url_read_rating = "http://tritontrade.org/TritonTradeCRUD/read_user_rating.php";
	private static String url_delete_book = "http://tritontrade.org/TritonTradeCRUD/delete_book.php";

	
	private void initialize() {
		bokname = (TextView) findViewById(R.id.infobookname);
		bokprice = (TextView) findViewById(R.id.infobookprice);
		bokcondition = (TextView) findViewById(R.id.infobookcondition);
		bokdescription = (TextView) findViewById(R.id.infobookdescription);
		rb = (RatingBar) findViewById(R.id.infobooksellerrating);
		notify =(Button) findViewById(R.id.infobookbutton);
		bokseller = (TextView) findViewById(R.id.infobookseller);

		i = getIntent();		

		changeStatus = (Button) findViewById(R.id.ButtonBookInfoStatusChange);
		edit = (Button) findViewById(R.id.bookinfo_edit);
		delete = (Button) findViewById(R.id.bookinfo_delete);
		
		changeStatus.setVisibility(View.GONE);
		delete.setVisibility(View.GONE);
		edit.setVisibility(View.GONE);
		bookid = i.getStringExtra(TAG_BOOK_ID);

		
		bname= i.getStringExtra(TAG_BOOKNAME);
		bprice= i.getStringExtra(TAG_PRICE);
		bcondition= i.getStringExtra(TAG_CONDITION);
		bdescription= i.getStringExtra(TAG_DESCRIPTION);
		bsellerid= i.getStringExtra(TAG_SELLER_ID);
		status = i.getStringExtra(TAG_STATUS);
		curr_user_id = i.getStringExtra("user_id");
		

		//Log.d("current user id :", curr_user_id);
		//Log.d("seller id :", bsellerid);

		//	FUCK CHECK VALUES FUCK FUCK FUCK DEBUG
		
		//Log.d("Book name: ", bname);
		//Log.d("Book price: ", bprice);
		//Log.d("Book condition: ", bcondition);
		//Log.d("Book description: ", bdescription);
		//Log.d("book status: ", status);
		//Log.d("current user id :", curr_user_id);
		//Log.d("seller id :", bsellerid);

		
		notify.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				new Emailseller().execute();
			}
		});
		
		rb.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {	
				return true;
			}
		});
		bokseller.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent rating = new Intent(BookInfo.this, RateMe.class);
				rating.putExtra(TAG_BOOKNAME,bname);
				rating.putExtra(TAG_PRICE, bprice);
				rating.putExtra(TAG_CONDITION, bcondition);
				rating.putExtra(TAG_DESCRIPTION, bdescription);
				rating.putExtra(TAG_SELLER_ID, bsellerid);
				rating.putExtra("sellername",seller_name);
				rating.putExtra(TAG_STATUS,status);
				rating.putExtra("user_id",curr_user_id);
				rating.putExtra("return","bookinfo");
				startActivity(rating);
			
			}
		}); 
		
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookinfo);
		initialize();	
		

		if(status != null) {
		/* If the status is pending, show the user the changeStatus button*/
		if( status.equals("Pending")){
			notify.setVisibility(View.GONE);
			
			if (curr_user_id.equals(bsellerid)){
				changeStatus.setVisibility(View.VISIBLE);
				changeStatus.setOnClickListener(new View.OnClickListener (){
				
					public void onClick(View v){
						new ChangeStatus().execute();
					}
				});
			}
		}
		if (status.equals("For Sale") && curr_user_id.equals(bsellerid)){
			notify.setVisibility(View.GONE);
			edit.setVisibility(View.VISIBLE);
			delete.setVisibility(View.VISIBLE);
			edit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent edit = new Intent(BookInfo.this, Edit.class);
					edit.putExtra(TAG_USER_ID, curr_user_id);
					edit.putExtra(TAG_BOOK_ID, bookid);
					startActivity(edit);
				}
			});
			
			delete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					new DeleteBook().execute();
					
				}
			});
		}
		
		
		}
		
		
		bokname.setText("Book: " +bname);
		bokname.setText("Book: " + bname);
		bokprice.setText("Book price: $" + bprice + "0");
		
		//Log.d("in book info : cond = ", bcondition);
		
		if (bcondition.equals("1")){
			bokcondition.setText("Condition: Used");			
		}
		else if (bcondition.equals("2") ){
			bokcondition.setText("Condition: Good");
		}
		else
			bokcondition.setText("Condition: Perfect");
		
		bokdescription.setText("Description: " +bdescription);
		
		
		new Getinfo().execute();
		
		
	
	}
	
	class Getinfo extends AsyncTask<String,String,String> { 
		double avg;
		protected void onPreExecute() {
				
				super.onPreExecute();
				pDialog = new ProgressDialog(BookInfo.this);
				pDialog.setMessage("Gathering Seller's Data...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
				
			}
		@Override
		protected String doInBackground(String... arg0) {
			/* Obtaining seller information */
			
			//Log.d("Book name: ", bname);
			//Log.d("Book price: ", bprice);
			//Log.d("Book condition: ", bcondition);
			//Log.d("Book description: ", bdescription);
			//Log.d("book status: ", status);
			//Log.d("current user id :", curr_user_id);
			//Log.d("seller id :", bsellerid);
						
			List<NameValuePair> rating = new ArrayList<NameValuePair>();
			rating.add(new BasicNameValuePair("user_id", bsellerid));
			jobj = jpar.makeHttpRequest(url_read_rating, "GET", rating);
			
        	List<NameValuePair> seller = new ArrayList<NameValuePair>();
        	seller.add(new BasicNameValuePair("user_id",bsellerid ));
        	jobj_seller = jpar_seller.makeHttpRequest(url_read_user, "GET", seller);
        	//Log.d("seller info: ", jobj_seller.toString());
			
        	/* obtaining current user information*/
        	List<NameValuePair> user = new ArrayList<NameValuePair>();
        	user.add(new BasicNameValuePair("user_id",curr_user_id ));
        	jobj_user = jpar_user.makeHttpRequest(url_read_user, "GET", user);
        	//Log.d("current info: ", jobj_user.toString());
        	
        	/* setting values to text views */
			runOnUiThread(new Runnable() {
                public void run() {

                	
                	try {
				
                		int success = jobj_seller.getInt(TAG_SUCCESS);
                		int successv2 = jobj_user.getInt(TAG_SUCCESS);
                		int successv3 = jobj.getInt(TAG_SUCCESS);
                		//Log.d("success: ", success);
			
                		if(success == 1  && successv2 ==1) {
                			jarray_seller = jobj_seller.getJSONArray("user");
                			jarray_user = jobj_user.getJSONArray("user");
                			
                			JSONObject s = jarray_seller.getJSONObject(0);
                			JSONObject u = jarray_user.getJSONObject(0);
                			
                			c_name = (TextView) findViewById(R.id.bookinfo_current_user);
                			c_email = (TextView) findViewById(R.id.bookinfo_current_user_email);
                			s_email = (TextView) findViewById(R.id.bookinfo_seller_email);
                			bokseller = (TextView) findViewById(R.id.infobookseller);
                			
                			seller_name = s.getString(TAG_USERNAME);
                			bokseller.setText("Seller's name: " + s.getString(TAG_USERNAME));      
                			s_email.setText(s.getString(TAG_EMAIL));
                			c_name.setText(u.getString(TAG_USERNAME));
                			c_email.setText(u.getString(TAG_EMAIL));
                		}
                		else {
                			Intent i = new Intent(getApplicationContext(), Books.class);
                			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                			startActivity(i);
                		}
                		if(successv3 == 1){
                			
                			JSONArray jarray = jobj.getJSONArray("rating");
                			
                			for(int i = 0; i < jarray.length(); i++){
                				JSONObject c = jarray.getJSONObject(i);
                				avg = avg + c.getDouble("rating");
                			}
                			
                			avg = avg / jarray.length();
                			rb.setRating((float) avg);               			
                		
                		}
                		else{
                			rb.setRating((float) 0);
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
	class ChangeStatus extends AsyncTask<String, String, String> {
		
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(BookInfo.this);
			pDialog.setMessage("Updating book status...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
	
		}

		@Override
		protected String doInBackground(String... args) {
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			//Log.d("Book id is", bookid);
			params.add(new BasicNameValuePair("book_id",bookid));

			JSONObject json = jpar.makeHttpRequest(url_book_status, "GET", params);
			//Log.d("Inside Try", "you're outside try");
			try {
				   
				   
					int success = json.getInt(TAG_SUCCESS);
			  
					//Log.d("Inside Try", "you're inside try");
					
					if( success == 1 ) {
						
						Intent inten = new Intent( getApplicationContext(), Homepage.class);
						inten.putExtra("user_id", curr_user_id);
						startActivity(inten);
						
					}
				}
				catch (JSONException e) {
	                e.printStackTrace();
	            }
			
			return null;
		}
		
	}

	class Emailseller extends AsyncTask<String,String,String> {
	
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(BookInfo.this);
				pDialog.setMessage("Emailing Seller...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
		
			}
		
		@Override
		protected String doInBackground(String... params) {
			
			
			List<NameValuePair> email = new ArrayList<NameValuePair>();
			
			//Log.d("buyer id: ", curr_user_id);
			//Log.d("book id: ", i.getStringExtra("book_id"));
			//Log.d("seller email: ", s_email.getText().toString());
			//Log.d("seller name: ", bokseller.getText().toString() );
			//Log.d("user email: ", c_email.getText().toString() );
			//Log.d("user name: ", c_name.getText().toString() );
			
			email.add(new BasicNameValuePair("buyer_id", curr_user_id));
			email.add(new BasicNameValuePair("book_id", i.getStringExtra("book_id")  ));
			email.add(new BasicNameValuePair("selleremail", s_email.getText().toString()));
			email.add(new BasicNameValuePair("sellername", seller_name ));
			email.add(new BasicNameValuePair("buyeremail", c_email.getText().toString() ));
			email.add(new BasicNameValuePair("buyername", c_name.getText().toString()));
		
			jobj = jpar.makeHttpRequest(url_email_seller, "GET", email);
			
			Log.d("was email ok? :", jobj.toString());
			 
			try {
				
				
				int successv2 = jobj.getInt(TAG_SUCCESS);
			
				
				//Log.d("success: ", successv2);
			
				if(successv2 == 1) {
					Intent home = new Intent(BookInfo.this, Homepage.class);
					home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					home.putExtra("user_id", curr_user_id);
					startActivity(home);
				}
				else {
					Intent i = new Intent(getApplicationContext(), Books.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			}
			catch (JSONException e){
					e.printStackTrace();
			}
					
			return null;
		}
		protected void onPostExecute(String file_url ) {
			pDialog.dismiss();
		}
	}
	
	class DeleteBook extends AsyncTask<String, String, String> {
		
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(BookInfo.this);
				pDialog.setMessage("Deleting Book...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			}

			@Override
			protected String doInBackground(String... arg0) {
				
				List<NameValuePair> book = new ArrayList<NameValuePair>();
				
				//Log.d("book id: ",  i.getStringExtra("book_id")  );
					
				book.add(new BasicNameValuePair("book_id",  i.getStringExtra("book_id")  ));
		
				jobj = jpar.makeHttpRequest(url_delete_book, "GET", book);
				
				try {
					
					int success = jobj.getInt(TAG_SUCCESS);
				
					if(success == 1) {
						Intent home = new Intent(BookInfo.this, Homepage.class);
						home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						home.putExtra("user_id", curr_user_id);
						startActivity(home);
					}
					else {

					}
				}
				catch (JSONException e){
						e.printStackTrace();
				}
				
				return null;
			}
			
			protected void onPostExecute(String file_url ) {
				pDialog.dismiss();
			}
		
	}
	
}



