package com.bm.safebus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.safebus.mapa.MapaTrackingActivity;
import com.bm.savebus.utilerias.Utils;
import com.mikesaurio.mensajesydialogos.Mensajes;

/**
 * 
 * @author mikesaurio
 * 
 */
public class SafeBusMainActivity extends Activity implements OnClickListener {

	public Button btn_encuentra;
	public Button btn_reporta;
	public Button btn_conecta;
	private AlertDialog customDialog = null;
	private int height;
	private int width;
	private TextView tv_problemas_titulo;
	private ImageView alarma_iv_alarma;
	private AnimationDrawable frameAnimation;
	private LinearLayout ll_quien,ll_enviando_mensaje,ll_reporte_hecho;
	private ImageView iv_reporte_usuario,iv_reporte_chofer;
	private int aviso_a= 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.safebus_activity_main);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width / 3,
				height / 4);

		btn_encuentra = (Button) findViewById(R.id.safebus_btn_encuentra);
		btn_encuentra.setOnClickListener(this);
		btn_encuentra.setLayoutParams(lp);
		btn_reporta = (Button) findViewById(R.id.safebus_btn_reporta);
		btn_reporta.setOnClickListener(this);
		btn_reporta.setLayoutParams(lp);
		btn_conecta = (Button) findViewById(R.id.safebus_btn_conecta);
		btn_conecta.setOnClickListener(this);
		btn_conecta.setLayoutParams(lp);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.safebus_btn_encuentra:
			if (!Utils.hasInternet(SafeBusMainActivity.this)) {
				Mensajes.showDialogGPSWiFi(SafeBusMainActivity.this,
						Mensajes.FLAG_WIFI).show();
			} else if (!Utils.hasGPS(SafeBusMainActivity.this)) {
				Mensajes.showDialogGPSWiFi(SafeBusMainActivity.this,
						Mensajes.FLAG_GPS).show();
			} else {
				iniciarActividad(MapaTrackingActivity.class);
			}

			break;

		case R.id.safebus_btn_reporta:
			showDialogQuienTieneProblemas().show();
			break;

		case R.id.safebus_btn_conecta:
			
			break;
		case R.id.safebus_btn_alguien_mas:
		new MensajeTask().execute(2);
			break;
		case R.id.safebus_btn_yo:
			new MensajeTask().execute(1);
			break;

		default:
			break;
		}

	}

	/**
	 * 
	 * @param clase
	 */
	public void iniciarActividad(final Class<?> clase) {
		startActivity(new Intent(SafeBusMainActivity.this, clase));
	}

	/**
	 * Dialogo para asegurar que quieres salir de la app
	 * 
	 * @param Activity
	 *            (actividad que llama al di‡logo)
	 * @return Dialog (regresa el dialogo creado)
	 **/

	public Dialog showDialogQuienTieneProblemas() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = getLayoutInflater()	.inflate(R.layout.activity_reporte, null);
		builder.setView(view);
		builder.setCancelable(true);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width / 3,
				width / 3);

		Button btn_alguien_mas = (Button) view.findViewById(R.id.safebus_btn_alguien_mas);
		btn_alguien_mas.setLayoutParams(lp);
		btn_alguien_mas.setOnClickListener(this);

		Button btn_yo = (Button) view.findViewById(R.id.safebus_btn_yo);
		btn_yo.setLayoutParams(lp);
		btn_yo.setOnClickListener(this);

		tv_problemas_titulo = (TextView) view.findViewById(R.id.safebus_tv_problemas_titulo);


		alarma_iv_alarma = (ImageView) view	.findViewById(R.id.enviar_alarma_iv_alarma);
		alarma_iv_alarma.setLayoutParams(lp);

		alarma_iv_alarma.setBackgroundResource(R.drawable.animation_alarma);
		frameAnimation = (AnimationDrawable) alarma_iv_alarma.getBackground();
		
		ll_quien =(LinearLayout)view.findViewById(R.id.safebus_ll_quien);
		ll_enviando_mensaje=(LinearLayout)view.findViewById(R.id.safebus_ll_enviando_mensaje);
		ll_reporte_hecho=(LinearLayout)view.findViewById(R.id.safebus_ll_reporte_hecho);
		
		iv_reporte_chofer = (ImageView) view	.findViewById(R.id.enviar_alarma_iv_reporte_chofer);
		iv_reporte_chofer.setLayoutParams(lp);
		
		iv_reporte_usuario = (ImageView) view	.findViewById(R.id.enviar_alarma_iv_reporte_usuario);
		iv_reporte_usuario.setLayoutParams(lp);

		return (customDialog = builder.create());
	}
	
	
	private class MensajeTask extends AsyncTask<Integer, Void, Boolean> {
	    private long time;

	    @Override
	    protected void onPreExecute() {
	    
	    	ll_quien.setVisibility(LinearLayout.GONE);
			ll_enviando_mensaje.setVisibility(LinearLayout.VISIBLE);
			tv_problemas_titulo.setText("Enviando alarma...");
			frameAnimation.start();
	    	
	        super.onPreExecute();
	        time = System.currentTimeMillis();
	    }

	    @Override
	    protected Boolean doInBackground(Integer... params) {
	    	aviso_a=params[0];
	    	
	        try {
	            Thread.sleep(5000);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    @Override
	    protected void onPostExecute(Boolean result) {
	    	frameAnimation.stop();
	    	ll_enviando_mensaje.setVisibility(LinearLayout.GONE);
	    	ll_reporte_hecho.setVisibility(LinearLayout.VISIBLE);
	    	if(aviso_a==1){
	    		tv_problemas_titulo.setText(getResources().getString(R.string.notificar_chofer));
	    		iv_reporte_usuario.setVisibility(ImageView.VISIBLE);
	    	}else{
	    		tv_problemas_titulo.setText(getResources().getString(R.string.notificar_chofer_familia));
	    		
	    		iv_reporte_usuario.setVisibility(ImageView.VISIBLE);
	    		iv_reporte_chofer.setVisibility(ImageView.VISIBLE);
	    	}
	    	
	    	
	        super.onPostExecute(result);
	       
	    }
	}
	

}
