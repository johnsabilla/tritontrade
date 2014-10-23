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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserLogin extends Activity {
	
	Button btnlogin;
	EditText ETusername;
	EditText ETpassword;
	
	String username, password;
	
	/*Progress dialog*/
	private ProgressDialog pDialog;
	
	/* selecting for a user script in the server*/
	private static String url_read_user = "http://tritontrade.org/TritonTradeCRUD/read_user_login.php";
	
	/* Global variables */
	private static final String TAG_SUCCESS  = "success";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_PASSWORD = "password";
	private static final String TAG_USER_ID= "user_id";
	private static final String TAG_EMAIL = "email";
	
	
	
	JSONParser jsonParser = new JSONParser();
	
	JSONArray user = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		btnlogin = (Button) findViewById(R.id.btloginButton);
		ETusername = (EditText) findViewById(R.id.ETloginUsername);
		ETpassword = (EditText) findViewById(R.id.ETloginPassword);
		
		ETusername = (EditText) findViewById(R.id.ETloginUsername);
		ETpassword = (EditText) findViewById(R.id.ETloginPassword);
		
		//ETusername.setText("john");
		//ETpassword.setText("test");
		Intent i = getIntent();
		
		username = i.getStringExtra(TAG_USERNAME);
		password = i.getStringExtra(TAG_PASSWORD);
		
		btnlogin = (Button) findViewById(R.id.btloginButton);
		
		btnlogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new LoadUser().execute();
				
			}
		});
		
			
		}
		
		
	
	
	class LoadUser extends AsyncTask<String,String,String> {
	
		protected void onPreExecute() {
			
			super.onPreExecute();
			pDialog = new ProgressDialog(UserLogin.this);
			pDialog.setMessage("Trying to connect you sit tight...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			
		}

		@Override
		protected String doInBackground(String... args) {
			
			String id   ;
			String name   ;
			String passwd ;
			String email ;
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			String un = ETusername.getText().toString();
			String pw = ETpassword.getText().toString();
			
			params.add(new BasicNameValuePair("username", "\"" + un + "\"" ));
			params.add(new BasicNameValuePair("password", "\"" + pw + "\""));
			
			JSONObject json = jsonParser.makeHttpRequest(url_read_user, "GET", params);
			
			//Log.d("Username: ", un );
			//Log.d("Passowrd: ", pw );
			//Log.d("User: ", json.toString() );
			
			try
			{
				
				int success = json.getInt(TAG_SUCCESS);
				
				//Log.d("success: ", success);
				
				if(success == 1) {
					
					user = json.getJSONArray("user");
					
					for(int i = 0; i < user.length(); i++){
						
					JSONObject userObj = user.getJSONObject(i);
					
					id      = userObj.getString(TAG_USER_ID);
					name    = userObj.getString(TAG_USERNAME);
				    passwd  = userObj.getString(TAG_PASSWORD);
					email  = userObj.getString(TAG_EMAIL);
					
					//Log.d("Just the tip: ", id );
					//Log.d("Just the tip: ", name );
					//Log.d("Just the tip: ", passwd );
					//Log.d("Just the tip: ", email );
					
					
						//Log.d("user id is ", id);
						
						Intent home = new Intent(getApplicationContext(), Homepage.class);
						home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						Bundle b = new Bundle();
						b.putString("user_id", id);
						home.putExtras(b);
						startActivity(home);
				
					}
			}
				else {
					
					Intent i = new Intent(getApplicationContext(), Register.class);
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
}