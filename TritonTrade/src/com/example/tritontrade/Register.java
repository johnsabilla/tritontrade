package com.example.tritontrade;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends Activity {

	EditText username, email, password1, password2;
	Button submit;
	TextView status, emailStatus;
	
	JSONParser jsonParser = new JSONParser();
	
	private ProgressDialog pDialog;
	
	private static String url_create_user = "http://tritontrade.org/TritonTradeCRUD/create_user.php";
	
	private static final String TAG_SUCCESS = "success";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		username  = (EditText) findViewById(R.id.EtUsernameRegister);
		email	  = (EditText) findViewById(R.id.EtEmailRegister);
		password1 = (EditText) findViewById(R.id.EtPassword1Register);
		password2 = (EditText) findViewById(R.id.EtPassword2Register);
		submit    = (Button)   findViewById(R.id.BtnRegister);
		status    = (TextView) findViewById(R.id.TvRegister);
		emailStatus = (TextView) findViewById(R.id.TvValidEmailRegister);
		
		 emailStatus.setVisibility(View.GONE);
		 status.setVisibility(View.GONE);
		
		 email.addTextChangedListener(new TextWatcher() { 
		        public void afterTextChanged(Editable s) { 
		            if (email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+") && s.length() > 0)
		            {
		            	emailStatus.setVisibility(View.VISIBLE);
		                emailStatus.setText("Valid email");
		            	 
		            }
		            else
		            {
		            	emailStatus.setVisibility(View.VISIBLE);
		                emailStatus.setText("Invalid email");
		            }
		            
		        } 
		        public void beforeTextChanged(CharSequence s, int start, int count, int after) {} 
		        public void onTextChanged(CharSequence s, int start, int before, int count) {} 
		    }); 
		 
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			if( username.getText().toString().isEmpty() ){
				status.setText("Username is empty!");
			}
			if( password1.getText().toString().equals(password2.getText().toString() ) && !(username.getText().toString().isEmpty()) && !(email.getText().toString().isEmpty()))
			{					
				status.setVisibility(View.VISIBLE);
				status.setText("Passwords match!");
				new CreateNewUser().execute();	
			}
			else {
				status.setVisibility(View.VISIBLE);
				status.setText("Passwords do not match!");
			}
			}	
			
		});
		
	}
	
	class CreateNewUser extends AsyncTask<String,String,String> {

		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating your account...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
		@Override
		protected String doInBackground(String... args) {
			
			/* Grab the values from the fields */
			String Sname  = username.getText().toString();
			String Spass  = password1.getText().toString();
			String Semail = email.getText().toString();
			
			/* create parameter hash to be passed in with the url*/
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair("username", Sname) );
			params.add(new BasicNameValuePair("password", Spass) );
			params.add(new BasicNameValuePair("email", Semail) );
			
			JSONObject json = jsonParser.makeHttpRequest(url_create_user, "POST", params);
			
			//Log.d("Create Reponse:", json.toString() );
			
			try {
				
				int success = json.getInt(TAG_SUCCESS);
				
				if( success == 1) {
					
					String user_id = json.getString("user_id");
					Intent i = new Intent(getApplicationContext(), Homepage.class);
					i.putExtra("user_id", user_id);
					startActivity(i);
					
					finish();
				}
				else {
					emailStatus.setText("Something went wrong, please restart the app.");
				}
					
			} catch (JSONException e) {
				e.printStackTrace();
				
			}
			
			return null;
		}
		
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
		
	}
	
}
