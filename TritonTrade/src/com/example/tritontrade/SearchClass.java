package com.example.tritontrade;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.example.tritontrade.UserLogin.LoadUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SearchClass extends SherlockActivity  {
	
	/*Progress dialog*/
	private ProgressDialog pDialog;
	
	/* selecting for a user script in the server*/
	private static String url_read_class = "http://tritontrade.org/TritonTradeCRUD/read_one_class.php";
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_NAME = "name";
	private static final String TAG_NUMBER = "number";
	private static final String TAG_CLASS_ID = "class_id";
	Intent i;
	EditText ETclassname, ETclassnumber;
	TextView tryagain;
	Button search;
	String classname, classnumber;
	
	JSONParser jsonParser = new JSONParser();
	
	JSONArray bookClass = null;
	
	private void initialize() {
		search = (Button) findViewById(R.id.searchclasscearch);
		ETclassname = (EditText) findViewById(R.id.searchClass);
		ETclassnumber = (EditText) findViewById(R.id.searchNumber);
		tryagain = (TextView) findViewById(R.id.serachclass_tryagain);
		i = getIntent();
		if (i.getStringExtra("not_found").equals("false")){
			tryagain.setVisibility(View.GONE);			
		}else {
			tryagain.setText(i.getStringExtra("not_found"));
		}
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchclass);
		initialize();
				
		search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new LoadClass().execute();
				
			}
		});
		
	}


	class LoadClass extends AsyncTask<String, String, String> {

		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(SearchClass.this);
			pDialog.setMessage("Locating list of books...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			
		}
		
		protected String doInBackground(String... args){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			String classnam = ETclassname.getText().toString();
			String classnum = ETclassnumber.getText().toString();
		
			params.add(new BasicNameValuePair("name", "\"" + classnam + "\"" ));
			params.add(new BasicNameValuePair("number", "\"" + classnum + "\""));
			
			
			JSONObject json = jsonParser.makeHttpRequest(url_read_class, "GET", params);
			
			try
			{
				
				int success = json.getInt(TAG_SUCCESS);
				
				//Log.d("success: ", success);
				
				if(success == 1) {
					
					bookClass = json.getJSONArray("class");
					
						
					JSONObject classObj = bookClass.getJSONObject(0);
					
				    String class_id = classObj.getString(TAG_CLASS_ID);
				    String class_name = classObj.getString(TAG_NAME);
				    String class_number = classObj.getString(TAG_NUMBER);
						       
				    
				    
					Intent books = new Intent(getApplicationContext(), Books.class);
					books.putExtra("user_id", i.getStringExtra("user_id"));
					books.putExtra(TAG_CLASS_ID, class_id);
					books.putExtra("name&number", class_name+" "+class_number);
					books.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(books);
				
					
			    } else {
					
		            String message = "No Books Found For Class "+classnum+" Try Again?";

					Intent i = new Intent(getApplicationContext(), SearchClass.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("not_found", message);
					i.putExtra("user_id",i.getStringExtra("user_id") );
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
}

