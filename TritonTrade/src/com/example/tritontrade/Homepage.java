package com.example.tritontrade;

import com.google.ads.*;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockListActivity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;



public class Homepage extends SherlockListActivity implements OnClickListener {
	/* the attributes in the XML */
    Button buy, sell, logout;
 
    
    /* progress dialog bar */
    private ProgressDialog pDialog;
    
    String bookid ;
    String classid ;
    String sellerid ;
    String buyerid ;
    String bookname;
    String description ;
    String price;
    String condition;
    String status;
	
    JSONParser jParser = new JSONParser();
    
    ArrayList<HashMap<String, String>> booklist;
    
    private static String url_user_history = "http://tritontrade.org/TritonTradeCRUD/user_history.php";
    private static String url_book_info = "http://tritontrade.org/TritonTradeCRUD/read_book_by_bookid.php";
    
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_BOOKS = "books";
    private static final String TAG_BOOKID = "book_id";
    private static final String TAG_CLASSID = "class_id";
    private static final String TAG_SELLERID = "seller_id";
    private static final String TAG_BUYERID = "buyer_id";
    private static final String TAG_BOOKNAME = "bookname";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_PRICE = "price";
    private static final String TAG_CONDITION = "condition";
    private static final String TAG_STATUS = "status";

    JSONArray books = null;
    
    String userid;
	
	
    
	private void initialize() {
		
		/* initialize those attributes */
		buy = (Button) findViewById(R.id.homepageButtonBuy);
		logout    = (Button) findViewById(R.id.homepageButtonLogout);
		sell = (Button)findViewById(R.id.homepageButtonSell);
		
		buy.setOnClickListener(this);
		logout.setOnClickListener(this);
		sell.setOnClickListener(this);
		
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		initialize();
		
		  AdView adView = (AdView)this.findViewById(R.id.adView);
		    adView.loadAd(new AdRequest());
		    
		//adView = new AdView(this, AdSize.BANNER, "a1513659633b2c0");
		
		//LinearLayout layout = (LinearLayout)findViewById(R.id.adView);
		
		//layout.addView(adView);
		
		//adView.loadAd(new AdRequest());
		
		Bundle id = getIntent().getExtras();
			
		userid = id.getString("user_id");

		
		booklist = new ArrayList<HashMap<String, String>>();
		
		new LoadHistory().execute();
		
		 ListView lv = getListView();
		 
	        // on seleting single product
	        // launching Edit Product Screen
	        lv.setOnItemClickListener(new OnItemClickListener() {
	 
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {
	                // getting values from selected ListItem
	                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
	                        .toString();
	                String status = ((TextView) view.findViewById(R.id.status)).getText().toString();
	                
	  
	                
	                if (status.equals("For Sale") || status.equals("Pending") ){
	                	
	                	Intent in = new Intent(getApplicationContext(), BookInfo.class);
	                	
	                	String bid = ((TextView) view.findViewById(R.id.bookid)).getText().toString();                	
	                	String bprice = ((TextView) view.findViewById(R.id.price)).getText().toString();              	
	                	String bname = ((TextView) view.findViewById(R.id.name)).getText().toString();
	                	String bcondition = ((TextView) view.findViewById(R.id.condition)).getText().toString();
	                	String bdescription = ((TextView) view.findViewById(R.id.description)).getText().toString();
	                	String bstatus = ((TextView) view.findViewById(R.id.status)).getText().toString();
	                	String bsellerid = ((TextView) view.findViewById(R.id.sellerid)).getText().toString();
	                	
	            		//Log.d("Book name: ", bookname);
	            		//Log.d("Book id: ", bid);
	            		//Log.d("Book price: ", price);
	            		//Log.d("Book condition: ", condition);
	            		//Log.d("Book description: ", description);
	            		//Log.d("book seller id: ", sellerid);
	            		//Log.d("book status: ", status);
	            		
	                    in.putExtra(TAG_BOOKID, bid);
		                in.putExtra(TAG_BOOKNAME, bname);
		                in.putExtra(TAG_PRICE, bprice);
		                in.putExtra(TAG_SELLERID, bsellerid);
		                in.putExtra(TAG_CONDITION, bcondition);
		                in.putExtra(TAG_DESCRIPTION, bdescription);
		                in.putExtra(TAG_STATUS, bstatus);
	                	in.putExtra("user_id", userid);
	                	
	                	startActivity(in);
	                }
	                
	                if(status.equals("Purchased")) {
	                	
	                	Intent rate = new Intent(getApplicationContext(), CreateRating.class);
	                	String bsellerid = ((TextView) view.findViewById(R.id.sellerid)).getText().toString();
	                	rate.putExtra(TAG_SELLERID, bsellerid);
	                	rate.putExtra("user_id", userid);
	                	startActivity(rate);
	                	
	                }
	          
	            }
	        }); 
	        
	    
	}
	/*
    public void onDestroy() {
        if (adView != null) {
          adView.destroy();
        }
        super.onDestroy();
      }
    */
    class LoadHistory extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Homepage.this);
            pDialog.setMessage("Loading history, please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        
		@Override
		protected String doInBackground(String... args) {
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	
			params.add(new BasicNameValuePair("user_id", userid ));
			JSONObject json = jParser.makeHttpRequest(url_user_history, "GET", params);
			
			
			//Log.d("Single book Details", json.toString());
			
			try {
				
				int success = json.getInt(TAG_SUCCESS);
			  
				if( success == 1 ) {
					 
					
					 books = json.getJSONArray(TAG_BOOKS);
					 
					 for(int i = 0; i < books.length(); i++) {
						 
						 JSONObject b = books.getJSONObject(i);
						 
						    bookid = b.getString(TAG_BOOKID);
						    classid = b.getString(TAG_CLASSID);
						    sellerid = b.getString(TAG_SELLERID);
						    buyerid = b.getString(TAG_BUYERID);
						    bookname = b.getString(TAG_BOOKNAME);
						    description = b.getString(TAG_DESCRIPTION);
						    price = b.getString(TAG_PRICE);
						    condition = b.getString(TAG_CONDITION);
						    status = b.getString(TAG_STATUS);
						  
						    HashMap<String, String> map = new HashMap<String, String>();
						            		
						    map.put(TAG_BOOKNAME, bookname);
						    map.put(TAG_PRICE, price);
						    map.put(TAG_CONDITION, condition);
						    map.put(TAG_DESCRIPTION, description);
						    map.put(TAG_SELLERID, sellerid);
						    map.put(TAG_BOOKID, bookid);

						    map.put( TAG_STATUS , statusDecode(status, sellerid));
						    
						    booklist.add(map);
					 	}    
					 	
					 }	
					else{
					 	//Log.d("size", String.valueOf(booklist.size()));
					 	if (booklist.size() == 0){
					 		HashMap<String, String> map = new HashMap<String, String>();
		            		//Log.d("sup", "yo");
						    map.put(TAG_BOOKNAME, "Welcome To Triton Trade!");
						    map.put(TAG_PRICE, "");
						    map.put(TAG_CONDITION, "");
						    map.put(TAG_DESCRIPTION, "");
						    map.put(TAG_SELLERID, "");
						    map.put(TAG_BOOKID, "");

						    map.put( TAG_STATUS , "Account History will be listed here.");
						    
						    booklist.add(map);
					 	}
					}		
			} catch (JSONException e) {
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
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                
                    ListAdapter adapter = new SimpleAdapter( Homepage.this, booklist, R.layout.list_item,
                    		new String[] { TAG_BOOKNAME, TAG_STATUS,TAG_SELLERID, TAG_BOOKID, TAG_PRICE, TAG_CONDITION,TAG_DESCRIPTION, userid},
                            new int[] {  R.id.name,  R.id.status ,R.id.sellerid, R.id.bookid, R.id.price, R.id.condition, R.id.description ,R.id.pid});

                    // updating listview
                    setListAdapter(adapter);
                 
                }
            });
        }
    }
        	public void onClick(View v) {

        		switch( v.getId() ) {
        		
        		case R.id.homepageButtonSell:
        			Intent a = new Intent(Homepage.this, Sell.class);
        			a.putExtra("user_id", userid);
        			startActivity(a);
        			break;
        		
        		case R.id.homepageButtonBuy:
        			Intent b = new Intent(Homepage.this, Buy.class);
        			b.putExtra("user_id", userid);
        			startActivity(b);
        			break;
        		
        		case R.id.homepageButtonLogout:
        			Intent c = new Intent(Homepage.this, Main.class);
        			startActivity(c);
        			break;
        		}
        	}
        	
        	
            public String statusDecode(String status, String sellerid) {
            	
            	String output;
            	if(status.equals("1") ){
            		output = "For Sale";
            	}
            	else if(status.equals("2")) {
            		output = "Pending";
            	}
            	else if(sellerid.equals(userid)){
            		output = "Sold";
            	}
            	else
            		output = "Purchased";
            	
            	return output;
            }
}
        

	

