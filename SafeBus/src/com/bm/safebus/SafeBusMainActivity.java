package com.bm.safebus;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.bm.safebus.dialogos.Mensajes;
import com.bm.safebus.mapa.MapaTrackingActivity;
import com.bm.savebus.utilerias.Utils;

/**
 * 
 * @author mikesaurio
 *
 */
public class SafeBusMainActivity extends Activity implements OnClickListener {

	public Button btn_encuentra;
	public Button btn_reporta;
	public Button btn_conecta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   
        
       
		setContentView(R.layout.safebus_activity_main);

		btn_encuentra = (Button) findViewById(R.id.safebus_btn_encuentra);
		btn_encuentra.setOnClickListener(this);
		btn_reporta = (Button) findViewById(R.id.safebus_btn_reporta);
		btn_reporta.setOnClickListener(this);
		btn_conecta = (Button) findViewById(R.id.safebus_btn_conecta);
		btn_conecta.setOnClickListener(this);

	}

	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.safebus_btn_encuentra:
			if(!Utils.hasInternet(SafeBusMainActivity.this)){
				Mensajes.showDialogGPSWiFi(SafeBusMainActivity.this,Mensajes.FLAG_WIFI).show();
			}else if(!Utils.hasGPS(SafeBusMainActivity.this)){
				Mensajes.showDialogGPSWiFi(SafeBusMainActivity.this,Mensajes.FLAG_GPS).show();
			}else{
				iniciarActividad(MapaTrackingActivity.class);
			}
			
			
			break;

		case R.id.safebus_btn_reporta:

			break;

		case R.id.safebus_btn_conecta:

			break;

		default:
			break;
		}

	}
	
	
	
	
	/**
	 * 
	 * @param clase
	 */
	public void iniciarActividad(final Class<?> clase){
		startActivity(new Intent(SafeBusMainActivity.this,clase));
	}
	

}
