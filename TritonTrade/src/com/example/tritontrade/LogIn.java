package com.example.tritontrade;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class LogIn extends Activity implements OnClickListener {

	/* the attributes in the XML */
    Button register, login;
    EditText username, password;
    
	private void initialize() {
		
		/* initialize those attributes */
		register = (Button) findViewById(R.id.loginButtonRegister);
		login    = (Button) findViewById(R.id.loginButtonLogin);
		username = (EditText) findViewById(R.id.loginUsername);
		password = (EditText) findViewById(R.id.loginPassword);
		
		register.setOnClickListener(this);
		login.setOnClickListener(this);
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_log_in);
		super.onCreate(savedInstanceState);
		initialize();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_log_in, menu);
		return false;
	}

	@Override
	public void onClick(View v) {

		switch( v.getId() ) {
		
		case R.id.loginButtonLogin:
			Intent a = new Intent(LogIn.this, Homepage.class);
			startActivity(a);
			break;
			
		case R.id.loginButtonRegister:
			Intent i = new Intent(LogIn.this, Register.class);
			startActivity(i);
			break;
			
		
		}
		
	}

}
