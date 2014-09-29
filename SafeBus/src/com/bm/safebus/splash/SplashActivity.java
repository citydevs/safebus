package com.bm.safebus.splash;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.bm.safebus.instrucciones.PaginadorInstrucciones;
import com.bm.savebus.R;

public class SplashActivity extends Activity {

    private static final long SPLASH_SCREEN_DELAY = 3000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   
        setContentView(R.layout.activity_splash);
        
        init(PaginadorInstrucciones.class);
        
    }
    
	   public void init(final Class<?> clase){
		  
		   
	    	TimerTask task = new TimerTask() {
	            @Override
	            public void run() {
	            	Intent mainIntent = new Intent().setClass(SplashActivity.this, clase);
	                startActivity(mainIntent);
	                finish();
	            }
	        };
	        Timer timer = new Timer();
	        timer.schedule(task, SPLASH_SCREEN_DELAY);
	    }
    
    
  
 
}