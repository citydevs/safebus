package com.bm.safebus.facebook;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bm.safebus.R;
import com.bm.savebus.utilerias.Utils;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class FacebookLoginActivity extends Activity {
	private final List<String> PERMISSIONS_TO_READ = Arrays.asList("user_likes", "user_status","public_profile");
	private final List<String> PERMISSIONS_TO_PUBLISH = Arrays.asList("publish_actions");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebooklogin);
		
		Session s = new Session(this);
		Session.setActiveSession(s);
		Session.OpenRequest request = new Session.OpenRequest(this);
		request.setPermissions(PERMISSIONS_TO_READ);
		request.setCallback(SessionStatusCallback);
		s.openForRead(request);
		
		Button btnPost = (Button) findViewById(R.id.facebook_btn_post);
		btnPost.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//postStatusUpdate();
				
			}
		});
		
		// start Facebook Login
		//Session.openActiveSession(this, true, SessionStatusCallback);
	}

	Session.StatusCallback SessionStatusCallback = new Session.StatusCallback() {
		
		// callback when session changes state
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			if (session.isOpened()) {
				// make request to the /me API
				Request.newMeRequest(session, new Request.GraphUserCallback() {
					
					// callback after Graph API response with user object
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							TextView welcome = (TextView) findViewById(R.id.facebook_tv_useranme);
							welcome.setText("Hola " + user.getName() + "!");
							Log.d("*************", user.getId()+"");
							//738944056143313
							String result = Utils.doHttpConnection("https://graph.facebook.com/"+user.getId()+"/likes/775396332498712");
							if(result!=null){
								Log.d("*********", result+"");
							}else{
								Log.d("*********", "null");
							}
						}
					}
				}).executeAsync();
			}
		}
	};
	
	
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
}
