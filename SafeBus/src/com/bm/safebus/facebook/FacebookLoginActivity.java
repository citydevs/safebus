package com.bm.safebus.facebook;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.bm.safebus.R;
import com.facebook.widget.LikeView;


/**
 * Clase que se encarga de dar like a una fanPage
 * @author mikesaurio
 *
 */
public class FacebookLoginActivity extends Activity {

	private LikeView btnPost;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	     requestWindowFeature(Window.FEATURE_NO_TITLE);  
	
	     
	     setContentView(R.layout.activity_facebooklogin);
		

		
	     	btnPost = (LikeView) findViewById(R.id.facebook_btn_post);
	     	btnPost.setObjectId("775396332498712");
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LikeView.handleOnActivityResult(this, requestCode, resultCode, data); 

	}
	
}
