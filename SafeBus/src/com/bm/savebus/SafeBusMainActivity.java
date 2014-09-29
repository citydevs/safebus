package com.bm.savebus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.bm.safebus.mapa.MapaTrackingActivity;
import com.bm.savebus.R;

/**
 * 
 * @author mikesaurio
 *
 */
public class SafeBusMainActivity extends Activity implements OnClickListener {

	public ImageView btn_encuentra;
	public ImageView btn_reporta;
	public ImageView btn_conecta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.safebus_activity_main);

		btn_encuentra = (ImageView) findViewById(R.id.safebus_btn_encuentra);
		btn_encuentra.setOnClickListener(this);
		btn_reporta = (ImageView) findViewById(R.id.safebus_btn_reporta);
		btn_reporta.setOnClickListener(this);
		btn_conecta = (ImageView) findViewById(R.id.safebus_btn_conecta);
		btn_conecta.setOnClickListener(this);

	}

	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.safebus_btn_encuentra:
			
			iniciarActividad(MapaTrackingActivity.class);
			break;

		case R.id.safebus_btn_reporta:

			break;

		case R.id.safebus_btn_conecta:

			break;

		default:
			break;
		}

	}
	
	
	public void iniciarActividad(final Class clase){
		startActivity(new Intent(SafeBusMainActivity.this,clase));
	}
	

}
