package com.example.tritontrade;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class Main extends Activity {

	/* the attributes in the XML */
    Button register, login;
    EditText username, password;
    
	private void initialize() {
		
		/* initialize those attributes */
		register = (Button) findViewById(R.id.loginButtonRegister);
		login    = (Button) findViewById(R.id.loginButtonLogin);
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.main);
		super.onCreate(savedInstanceState);
		initialize();
		
		login.getBackground().setColorFilter(Color.BLUE, Mode.MULTIPLY);
		register.getBackground().setColorFilter(Color.YELLOW, Mode.MULTIPLY);
		
		
		/* if login button is pushed, run this activity*/
		login.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), UserLogin.class);
				startActivity(i);
			}
		});
		
		/* if register button is pushed, run this activity */
		register.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), Register.class);
				startActivity(i);
			}
		});
		
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_log_in, menu);
		return false;
	}



/*
	public void onClick(View v) {

		switch( v.getId() ) {
		
		case R.id.loginButtonLogin:
			Intent a = new Intent(Main.this, Homepage.class);
			startActivity(a);
			break;
			
		case R.id.loginButtonRegister:
			Intent i = new Intent(Main.this, Register.class);
			startActivity(i);
			break;
			
		
		}
		
		
	}
	*/

}
