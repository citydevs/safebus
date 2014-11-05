package com.bm.safebus;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.safebus.facebook.FacebookLoginActivity;
import com.bm.safebus.mapa.MapaTrackingActivity;
import com.bm.safebus.panico.PanicAlert;
import com.bm.safebus.registro.ContactoActivity;
import com.bm.savebus.utilerias.Utils;
import com.facebook.Session;
import com.mikesaurio.mensajesydialogos.Mensajes;

/**
 * Clase principal que contiene las diferentes opciones que se pueden hacer 
 * 
 * @author mikesaurio
 * 
 */
public class SafeBusMainActivity extends Activity implements OnClickListener {
		
	public Button btn_encuentra;
	public Button btn_reporta;
	public Button btn_conecta;
	private AlertDialog customDialog = null;
	private TextView tv_problemas_titulo;
	private ImageView alarma_iv_alarma;
	private AnimationDrawable frameAnimation;
	private LinearLayout ll_quien,ll_enviando_mensaje,ll_reporte_hecho;
	private ImageView iv_reporte_usuario,iv_reporte_chofer;
	private int aviso_a= 0;
	private Menu menu;
	private Point p;
	private static final int ENVIAR_ALARMA_CHOFER=0;
	private static final int ENVIAR_ALARMA_FAMILIAR_CHOFER=1;
	String[] info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		
		setContentView(R.layout.safebus_activity_main);
		
		
		/*ActionBar*/
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);//new ColorDrawable(Color.WHITE)
		mActionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.marco));
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.action_bar_custome, null);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		/**/
		
		info= new Utils(SafeBusMainActivity.this).getPreferenciasContacto();

		 p = Utils.getTamanoPantalla(SafeBusMainActivity.this);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(p.x / 3,
				p.y / 4);

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
			iniciarActividad(FacebookLoginActivity.class);
			break;
		case R.id.safebus_btn_alguien_mas:
		new MensajeTask().execute(2);
			break;
		case R.id.safebus_btn_yo:
			new MensajeTask().execute(1);
			break;
		case R.id.enviar_alarma_btn_aceptar:
			customDialog.dismiss();
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
	 * Envia alarma de emergencia 
	 * @param tipo (int)
	 * ENVIAR_ALARMA_FAMILIAR_CHOFER envia la alarma al chofer y un SMS a contacto de emergencia
	 * ENVIAR_ALARMA_CHOFER envia alarma a chofer
	 */
	public void enviarAlarma(int tipo) {
	switch (tipo) {
	case ENVIAR_ALARMA_CHOFER:
		
		break;
	case ENVIAR_ALARMA_FAMILIAR_CHOFER:
		
		PanicAlert.sendSMS(info[0], getString(R.string.mensaje_emergencia));

		break;

	default:
		break;
		}
	}
	
	/*Facebook*/
	
	
	

	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.menuadd:
	    	startActivity(new Intent(SafeBusMainActivity.this,ContactoActivity.class));
	      return true;
	    case R.id.menuabouth:
	    	Mensajes.mostrarAercaDe(SafeBusMainActivity.this).show();
	    	return true;
	  
	    default:
	    	return false;
	    }
	  } 
	
	
	
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_main, menu);
	  this.menu=menu;
	  MenuItem bedMenuItem = menu.findItem(R.id.menuadd);
	 	info= new Utils(SafeBusMainActivity.this).getPreferenciasContacto();
	  		if(info[0]!=null){
	  			 bedMenuItem.setTitle(getResources().getString(R.string.main_editar_contacto));
	  				
	  		} else {
	  			 bedMenuItem.setTitle(getResources().getString(R.string.main_agregar_contacto));
	  		}
	    return true;
	  } 
	
	
	@Override
	protected void onResume() {
		if(menu!=null){
		 MenuItem bedMenuItem = menu.findItem(R.id.menuadd);
		 	info= new Utils(SafeBusMainActivity.this).getPreferenciasContacto();
		  		if(info[0]!=null){
		  			 bedMenuItem.setTitle(getResources().getString(R.string.main_editar_contacto));
		  				
		  		} else {
		  			 bedMenuItem.setTitle(getResources().getString(R.string.main_agregar_contacto));
		  		}
	}
		super.onResume();
	}

	
	
	
	
	
	/**
	 * Clase que envia al contacto de emergencia 
	 * @author mikesaurio
	 *
	 */
	private class MensajeTask extends AsyncTask<Integer, Void, Boolean> {
	    private long time;

	    @Override
	    protected void onPreExecute() {
	    
	    	ll_quien.setVisibility(LinearLayout.GONE);
			ll_enviando_mensaje.setVisibility(LinearLayout.VISIBLE);
			tv_problemas_titulo.setText("Enviando alarma...");
			frameAnimation.start();
			if(aviso_a==2){
	    		enviarAlarma(ENVIAR_ALARMA_CHOFER);
	    	}else{
	    		
		  		if(info[0]!=null){
		    		enviarAlarma(ENVIAR_ALARMA_FAMILIAR_CHOFER);
		  				
		  		} else {
		    		enviarAlarma(ENVIAR_ALARMA_CHOFER);
		  		}
	    	}
	    	
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
	    	if(aviso_a==2){
	    		tv_problemas_titulo.setText(getResources().getString(R.string.notificar_chofer));
	    		iv_reporte_chofer.setVisibility(ImageView.VISIBLE);
	    	}else{
	    		String[] info= new Utils(SafeBusMainActivity.this).getPreferenciasContacto();
		  		if(info[0]!=null){
		  			tv_problemas_titulo.setText(getResources().getString(R.string.notificar_chofer_familia));
		    		iv_reporte_usuario.setVisibility(ImageView.VISIBLE);
		    		iv_reporte_chofer.setVisibility(ImageView.VISIBLE);
		  				
		  		} else {
		  			tv_problemas_titulo.setText(getResources().getString(R.string.notificar_chofer));
		    		iv_reporte_chofer.setVisibility(ImageView.VISIBLE);
		  		}	
	    	}
	        super.onPostExecute(result);  
	    }
	}

	/*Dialogos*/
	/**
	 * Dialogo para asegurar que quieres salir de la app
	 * 
	 * @return Dialog (regresa el dialogo creado)
	 **/

	public Dialog showDialogQuienTieneProblemas() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = getLayoutInflater()	.inflate(R.layout.activity_reporte, null);
		builder.setView(view);
		builder.setCancelable(true);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(p.x / 3,
				p.x / 3);

		Button btn_alguien_mas = (Button) view.findViewById(R.id.safebus_btn_alguien_mas);
		btn_alguien_mas.setLayoutParams(lp);
		btn_alguien_mas.setOnClickListener(this);

		Button btn_yo = (Button) view.findViewById(R.id.safebus_btn_yo);
		btn_yo.setLayoutParams(lp);
		btn_yo.setOnClickListener(this);
		
		Button btn_aceptar = (Button) view.findViewById(R.id.enviar_alarma_btn_aceptar);
		btn_aceptar.setOnClickListener(this);

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
	
	
}