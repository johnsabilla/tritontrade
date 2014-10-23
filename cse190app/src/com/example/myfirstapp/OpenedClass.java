package com.example.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class OpenedClass extends Activity implements OnClickListener, OnCheckedChangeListener{

	TextView question, test;
	Button returnData;
	RadioGroup selectionList;
	String gotBread, sendData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send);
		initialize();
		//Bundle gotBasket = getIntent().getExtras();
		//gotBread = gotBasket.getString("key");
		//question.setText(gotBread);
	}
	
	private void initialize() {
		
		question = (TextView) findViewById(R.id.tvQuestion);
		test = (TextView) findViewById(R.id.tvText);
		returnData = (Button) findViewById(R.id.bReturn);
		returnData.setOnClickListener(this);
		selectionList = (RadioGroup) findViewById(R.id.rgAnswers);
		selectionList.setOnCheckedChangeListener(this);
	}
	

	@Override
	public void onCheckedChanged(RadioGroup buttonView, int isChecked) {
	
		
		switch(isChecked) {
		
		case R.id.rCrazy:
			sendData = "Probably Right";
			break;
		
		case R.id.rRetarded:
			sendData = "It goes without saying";
			break;
			
		case R.id.rStand:
			sendData = "Spot on!";
			break;
			
		}
		
		test.setText(sendData);
	}

	@Override
	public void onClick(View v) {
		
		
		Intent person = new Intent();
		Bundle backpack = new Bundle();
		backpack.putString("answer", sendData);
		person.putExtras(backpack);
		setResult(RESULT_OK, person);
		finish();
		
	}
	

}
