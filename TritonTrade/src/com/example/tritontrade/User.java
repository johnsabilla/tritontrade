package com.example.tritontrade;

import android.app.Activity;
import android.app.ProgressDialog;

public class User extends Activity {
	
	/*Progress dialog*/
	private ProgressDialog pDialog;
	
	/* selecting for a user script in the server*/
	private static String url_read_user = "http://tritontrade.org/read_user.php";
	
	
	private static final String TAG_SUCCESS  = "success";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_USERID   = "userid";
	private static final String TAG_EMAIL    = "email";
	
}
