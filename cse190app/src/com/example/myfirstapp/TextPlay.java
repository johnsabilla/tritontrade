package com.example.myfirstapp;

import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class TextPlay extends Activity implements View.OnClickListener {


	Button chkCmd;
	ToggleButton passTog;
	EditText input;
	TextView display;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text);
		
		initializeActivity();

		
		passTog.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				if(passTog.isChecked()){
					input.setInputType(InputType.TYPE_CLASS_TEXT | 
									   InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}else{
					input.setInputType(InputType.TYPE_CLASS_TEXT);
				}				
			}
		});
		
		chkCmd.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View v) {
				
				String check = input.getText().toString();
				display.setText(check);
				
				/*set gravity */
				if(check.contentEquals("left")) {
					display.setGravity( Gravity.LEFT );
				}
				else if (check.contentEquals("center")) {
					display.setGravity( Gravity.CENTER );
				}
				else if (check.contentEquals("right")) {
					display.setGravity( Gravity.RIGHT );
				}
				/* set color */
				else if (check.contentEquals("blue")) {
				    display.setTextColor(Color.RED);	
				}
				else if (check.contentEquals("WTF")) {
					Random crazy = new Random();
					display.setText("WTF");
					display.setTextSize(crazy.nextInt(75));
					display.setTextColor(Color.rgb(crazy.nextInt(254),
												   crazy.nextInt(254),
												   crazy.nextInt(254)));
				
					int place = crazy.nextInt(3);
					/*
					if(place == 1){
						display.setGravity( Gravity.LEFT );
					}
					else if(place == 2){
						display.setGravity( Gravity.RIGHT );
					}
					else{
						display.setGravity( Gravity.CENTER );
					}
					*/
					
					switch(crazy.nextInt(3)){
					case 0:
						break;
					case 1:
						display.setGravity( Gravity.CENTER );
					    break;
					case 2:
						display.setGravity( Gravity.LEFT );
					    break;
					case 3:
						display.setGravity( Gravity.RIGHT );
						break;
					
					}
					
				}else{
					
					display.setText("All else fails, display this");
					display.setGravity(Gravity.CENTER);
				}
			}
		});
	
  }

	private void initializeActivity() {
		
		chkCmd = (Button) findViewById(R.id.results);
		passTog = (ToggleButton) findViewById(R.id.tgPassword);
		input = (EditText) findViewById(R.id.etCommands);
		display = (TextView) findViewById(R.id.tvResults);	
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.results:
			
			break;
		
		case R.id.tgPassword:
			
			break;
			
		case R.id.tvResults:
			
			break;
			
		}
		
	}
}
