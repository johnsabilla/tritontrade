package com.example.myfirstapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {

	/* */
	int counter;
	Button add;
	Button subtract;
	TextView display;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        counter = 0;
        
        /*Connection between xml and java*/
        add =(Button) findViewById(R.id.add); 
        subtract = (Button) findViewById(R.id.sub);
        display = (TextView) findViewById(R.id.tvDisplay);
        
        add.setOnClickListener(new View.OnClickListener()
        {
			
			@Override
			public void onClick(View v)
			{
				counter++;
				display.setText("Your total is " + counter);
				
			}
			
		});
        
        subtract.setOnClickListener(new View.OnClickListener()
        {
			
			@Override
			public void onClick(View v)
			{

			    counter--;
			    display.setText("Your total is " + counter);
				
			}
			
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
