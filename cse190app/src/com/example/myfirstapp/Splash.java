package com.example.myfirstapp;

import android.app.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Splash extends Activity {

	MediaPlayer onSong;
	@Override
	
	protected void onCreate(Bundle TravisLoveBacon) {
	
		
		super.onCreate(TravisLoveBacon);
		setContentView(R.layout.splash);
		
		onSong = MediaPlayer.create(Splash.this, R.raw.victory);
		onSong.start();
		Thread timer = new Thread()
		{
			
			public void run()
			{
				/* Try/Catch block */
				try
				{
					sleep(5000); /* 3 seconds */
				} catch (InterruptedException e)
				{
				    e.printStackTrace();	
				} finally
				{
					Intent openStartingPoint = new Intent("com.example.myfirstapp.MENU");
					startActivity( openStartingPoint );
				}
				
			}
			
		};
		
		timer.start();
	}
	
	/*removes splash screen from stack */
	protected void onPause() {
		super.onPause();
		onSong.release();
		finish();
	}

}
