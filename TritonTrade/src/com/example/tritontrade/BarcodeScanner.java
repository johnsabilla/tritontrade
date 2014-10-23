package com.example.tritontrade;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class BarcodeScanner extends Activity {
	private static String booksearch = "https://www.googleapis.com/books/v1/volumes?";
	
	private static String googlekey = "AIzaSyCNGLDI8SnaWEzy6vMhdl-LT86r41KIacs";

	Button search;
	String barcode;
	JSONParser jsonParser = new JSONParser();
	String userid;

	
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.barcode);
		
		Bundle id = getIntent().getExtras();
		
		userid = id.getString("user_id");
		
		search = (Button) findViewById(R.id.btsearchgoogle);
		
		search.setVisibility(View.GONE);
		
	}
	
	public void onClick(View view) {
		
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		  if (scanResult != null) {
		
		    final String type;
		    
		    barcode = scanResult.getContents();
		    type = scanResult.getFormatName();
		   
		    EditText etbarcode = (EditText) findViewById(R.id.etbarcode);
		   
		    EditText ettype    = (EditText) findViewById(R.id.ettype);
		          
		    etbarcode.setText(barcode);
		   
		    ettype.setText(type);  
		    
		    new getBookInfo().execute();
						
						//Uri uri = Uri.parse("https://www.googleapis.com/books/v1/volumes?q=isbn:" + barcode 
						//		     + "&key=AIzaSyCNGLDI8SnaWEzy6vMhdl-LT86r41KIacs");
						//Intent i = new Intent(Intent.ACTION_VIEW, uri);
						//startActivity(i);
		    
		  }
		  
		}
	
	 class getBookInfo extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			int total;
			
			String title ;
			String author;
			String description;
			
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			//Log.d("barcode: ", barcode );
			
			param.add(new BasicNameValuePair("key", googlekey));
			param.add(new BasicNameValuePair("q", "isbn:" + barcode ));
			booksearch = booksearch + barcode;
			//Log.d("Url: ", booksearch);
			JSONObject json = jsonParser.makeHttpRequest(booksearch,"GET",param);
			
			//Log.d("book: ", json.toString() );
			try
			{
				total = json.getInt("totalItems");
				//Log.d("totalItems: ", Integer.toString(json.getInt("totalItems")) );
			
				if(total == 1){
				
					JSONArray jArray = json.getJSONArray("items");
					 
					for(int i = 0 ; i < jArray.length(); i++) {
						JSONObject volumeInfo = jArray.getJSONObject(i).getJSONObject("volumeInfo");
						
						title = volumeInfo.getString("title");
						author = volumeInfo.getString("authors");
						description = volumeInfo.getString("description");
						
						description = description.substring(0,250);
						
						//Log.d("title : ", title);
						//Log.d("author: ", author);
						//Log.d("description: ", description);
						
					
					
					Intent sell = new Intent(getApplicationContext(), Sell.class);
					Bundle bookinfo = new Bundle();
					bookinfo.putString("title", title);
					bookinfo.putString("author", author);
					bookinfo.putString("description", description);
					bookinfo.putString("set", "yes");
					sell.putExtra("user_id", userid);
					sell.putExtras(bookinfo);
					startActivity(sell); 
					}	
					
				}
				else {
					Intent sell = new Intent(getApplicationContext(), Sell.class);
					Bundle bookinfo = new Bundle();
					bookinfo.putString("set", "yes");
					sell.putExtra("user_id", userid);
					sell.putExtras(bookinfo);
					startActivity(sell); 
				}
			}
			catch (JSONException e){
				
				e.printStackTrace();
			}
			
			return null;
		}
		 
	 
	 }
	 
}
