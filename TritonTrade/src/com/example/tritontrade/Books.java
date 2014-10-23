package com.example.tritontrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.tritontrade.Buy.GetClasses;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Books extends ListActivity  {
	TextView class_name_number;
	Intent i;
	ListView lv;
	private ProgressDialog pDialog;
	JSONParser jpar = new JSONParser();
	JSONObject jobj;
	JSONArray jarray;
	String class_id;
	List<NameValuePair> lst;
	ArrayList<HashMap<String, String>> bookList;
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_BOOKNAME = "bookname";
	private static final String TAG_PRICE = "price";
	private static final String TAG_CLASS_ID = "class_id";
	private static final String TAG_BOOK_ID = "book_id";
	private static final String TAG_SELLER_ID = "seller_id";
	//private static final String TAG_BUYER_ID = "buyer_id";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_CONDITION = "condition";
	private static final String TAG_STATUS = "status";
	private static String url_read_book = "http://tritontrade.org/TritonTradeCRUD/read_book_by_class.php";
	
	private void initialize() {
		class_name_number = (TextView) findViewById(R.id.booksClassname);
		i = getIntent();
		lv = getListView();
		class_id = i.getStringExtra(TAG_CLASS_ID);
		bookList = new ArrayList<HashMap<String, String>>();
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.books);
		initialize();
		
		String name_number = i.getStringExtra("name&number");
		class_name_number.setText(name_number);
		
		
		new Getbooks().execute();
		
		lv = getListView();
        
		lv.setOnItemClickListener(new OnItemClickListener() {
        	 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
            	String bid = ((TextView) view.findViewById(R.id.book_id)).getText()
            			.toString();
            	String bname = ((TextView) view.findViewById(R.id.booklistname)).getText()
                        .toString();
                String bprice = ((TextView) view.findViewById(R.id.book_price)).getText()
                        .toString();
            	String bseller = ((TextView) view.findViewById(R.id.book_seller)).getText()
                        .toString();
                String bcondition = ((TextView) view.findViewById(R.id.book_condition)).getText()
                        .toString();
                String bdescription = ((TextView) view.findViewById(R.id.book_description)).getText()
                        .toString();
                
                Intent in = new Intent(getApplicationContext(),
                        BookInfo.class);
                // sending pid to next activity
                in.putExtra(TAG_CLASS_ID, class_id);
                in.putExtra(TAG_BOOK_ID, bid);
                in.putExtra("user_id", i.getStringExtra("user_id"));
                in.putExtra(TAG_BOOKNAME, bname);
                in.putExtra(TAG_PRICE,bprice);
                in.putExtra(TAG_SELLER_ID, bseller);
                in.putExtra(TAG_CONDITION, bcondition);
                in.putExtra(TAG_DESCRIPTION, bdescription);
                in.putExtra("status", "1");
 
                // starting new activity and expecting some response back
                startActivity(in);
            }
        });
	}	
	 class Getbooks extends AsyncTask<String,String,String> {

		   protected void onPreExecute() {
				
				super.onPreExecute();
				pDialog = new ProgressDialog(Books.this);
				pDialog.setMessage("Gathering Books...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
				
			}
		   
		    @Override
		    protected String doInBackground(String... arg0) {
		    	
		    	List<NameValuePair> lst = new ArrayList<NameValuePair>();

		    	//Log.d("class id: ", class_id);
		    	
		    	lst.add(new BasicNameValuePair(TAG_CLASS_ID,class_id ));
		    	
		    	jobj = jpar.makeHttpRequest(url_read_book, "GET", lst);
		    	
		    	//Log.d("json book object?:",jobj.toString());
		    	
		    	try
				{
					
					int success = jobj.getInt(TAG_SUCCESS);
					
					//Log.d("success: ", String.valueOf(success));
					
					if(success == 1) {
						
						jarray = jobj.getJSONArray("book");	
					    //Log.d("array length", String.valueOf(jarray.length()) );
					    
						for (int i = 0; i < jarray.length(); i++){
			    			JSONObject c = jarray.getJSONObject(i);
			    			HashMap<String, String> map = new HashMap<String, String>();
			    			if(c.getInt(TAG_STATUS) == 1){
			    				String book_id     = String.valueOf(c.getInt(TAG_BOOK_ID)); 
			    				
			    				String bookname    = c.getString(TAG_BOOKNAME);
			    				//Log.d("bookname from query", bookname);
			    				//String buyer_id  = String.valueOf(c.getInt(TAG_BUYER_ID));
			    				String seller_id   = String.valueOf(c.getInt(TAG_SELLER_ID));
			    				
			    				String description = c.getString(TAG_DESCRIPTION);
			    				
			    				String condition   = String.valueOf(c.getInt(TAG_CONDITION));
			    			
			    				String price       = String.valueOf(c.getDouble(TAG_PRICE));
			    				
			    				
			    			
			    				map.put(TAG_BOOK_ID,book_id);
			    				map.put(TAG_BOOKNAME, bookname );
			    				map.put(TAG_SELLER_ID, seller_id);
			    				map.put(TAG_DESCRIPTION, description);
			    				map.put(TAG_CONDITION, condition);
			    				map.put(TAG_PRICE, price);
			    			
			    				bookList.add(map);		    				
			    			}		                	
			    		}
						if ( bookList.isEmpty()){
		    				/*map.put(TAG_BOOK_ID,"");
		    				map.put(TAG_BOOKNAME, "No Books On File" );
		    				map.put(TAG_SELLER_ID, "");
		    				map.put(TAG_DESCRIPTION, "");
		    				map.put(TAG_CONDITION, "");
		    				map.put(TAG_PRICE, "");
		    				bookList.add(map);
		    				 */
	                	}
				    } else {
						/*
	    				map.put(TAG_BOOK_ID,"");
	    				map.put(TAG_BOOKNAME, "No Books On File" );
	    				map.put(TAG_SELLER_ID, "");
	    				map.put(TAG_DESCRIPTION, "");
	    				map.put(TAG_CONDITION, "");
	    				map.put(TAG_PRICE, "");
	    				bookList.add(map);
						*/
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
	                    /**
	                     * Updating parsed JSON data into ListView
	                     * 
	                     */
	                	HashMap<String, String> map = new HashMap<String, String>();
	                
	                	for( int i = 0; i < bookList.size(); i++){
	                		map = bookList.get(i);
	                		String bkname = map.get("bookname");
	                		//Log.d("boookname before adapter", bkname);
	                	}
	                	                   	                
	                    ListAdapter adapter = new SimpleAdapter( Books.this, bookList,R.layout.booklist, 
	                    		new String[] { TAG_BOOKNAME,TAG_BOOK_ID,TAG_SELLER_ID,TAG_PRICE,TAG_CONDITION,TAG_DESCRIPTION},
	                            new int[] { R.id.booklistname, R.id.book_id, R.id.book_seller, R.id.book_price, R.id.book_condition, R.id.book_description });
	                    // updating listview
	                    setListAdapter(adapter);
	                }
	            });
	 
	        }
	   }
}