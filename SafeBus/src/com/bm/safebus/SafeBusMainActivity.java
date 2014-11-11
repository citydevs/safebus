package com.bm.safebus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.safebus.facebook.FacebookLoginActivity;
import com.bm.safebus.gcm.UserInfo;
import com.bm.safebus.mapa.MapaTrackingActivity;
import com.bm.safebus.panico.PanicAlert;
import com.bm.safebus.registro.ContactoActivity;
import com.bm.savebus.utilerias.Utils;
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
	
	//dialogo
	TextView dialogo_califica_tv_caracteres;
	String Scalificacion;
	EditText comentario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//hola
		
		
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
			if((new Utils(SafeBusMainActivity.this).getPreferenciasPlaca()[0])!=null){
				long tiempo =Long.parseLong(new Utils(SafeBusMainActivity.this).getPreferenciasPlaca()[2]);
				if((tiempo+7200000)>Utils.getFechaHoy()){
					showDialogQuienTieneProblemas().show();
				}else{
					new Utils(SafeBusMainActivity.this).setPreferenciasPlaca(null,new Utils(SafeBusMainActivity.this).getPreferenciasPlaca()[1]);
					showDialogPlaca().show();
				}
				
			}else{
				showDialogPlaca().show();
			}
			
			break;

		case R.id.safebus_btn_conecta:
			if((new Utils(SafeBusMainActivity.this).getPreferenciasPlaca()[0])!=null){
				long tiempo =Long.parseLong(new Utils(SafeBusMainActivity.this).getPreferenciasPlaca()[2]);
				if((tiempo+7200000)>Utils.getFechaHoy()){
					iniciarActividad(FacebookLoginActivity.class);
				}else{
					if((new Utils(SafeBusMainActivity.this).getPreferenciasPlaca()[1])==null){
						showDialogCalificaBus().show();
					}else{
						new Utils(SafeBusMainActivity.this).setPreferenciasPlaca(null,null);
						showDialogPlaca().show();
					}
				}
			}else{
				showDialogPlaca().show();
			}
			
			break;
		case R.id.safebus_btn_alguien_mas:
				new MensajeTask(2).execute();
			break;
		case R.id.safebus_btn_yo:
				new MensajeTask(1).execute();
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

	    public MensajeTask(int i) {
	    	aviso_a=i;
		}

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
	    	
	    	
	        try {
	        	Utils.doHttpConnection("https://cryptic-peak-2139.herokuapp.com/api/client_panic?email="
	        			+UserInfo.getEmail(SafeBusMainActivity.this)
	        			+"&placa="+new Utils(SafeBusMainActivity.this).getPreferenciasPlaca()[0]);
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
	
	
	
	
	public AlertDialog showDialogPlaca() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = getLayoutInflater()	.inflate(R.layout.dialogo_placa, null);
		builder.setView(view);
		builder.setCancelable(true);
		
		final EditText dialogo_placa_et_placa = (EditText)view.findViewById(R.id.dialogo_placa_et_placa);
		Button dialogo_placa_btn_aceptar=(Button)view.findViewById(R.id.dialogo_placa_btn_aceptar);
		dialogo_placa_btn_aceptar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!dialogo_placa_et_placa.getText().toString().equals("")){
					new Utils(SafeBusMainActivity.this).setPreferenciasPlaca(dialogo_placa_et_placa.getText().toString(),null);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(dialogo_placa_et_placa.getWindowToken(), 0);
					customDialog.dismiss();
				}else{
					dialogo_placa_et_placa.setError(getString(R.string.ruta_registro_vacio));
				}
				
			}
		});
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(dialogo_placa_et_placa, InputMethodManager.SHOW_IMPLICIT);	

		return (customDialog = builder.create());
	}
	
	
	public AlertDialog showDialogCalificaBus() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = getLayoutInflater()	.inflate(R.layout.dialogo_califica, null);
		builder.setView(view);
		builder.setCancelable(true);

		
		 dialogo_califica_tv_caracteres =(TextView)view.findViewById(R.id.dialogo_califica_tv_caracteres);
		
	final	 TextView califica_taxi_tv_titulo_calif =(TextView)view.findViewById(R.id.califica_taxi_tv_titulo_calif);
		 
		
		
		 comentario = (EditText)view.findViewById(R.id.dialogo_califica_servicio_et_comentario);
		comentario.addTextChangedListener(mTextEditorWatcher);
		
		RatingBar	rank = (RatingBar)view.findViewById(R.id.dialogo_califica_servicio_ratingBarServicio);
		rank.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {
				Scalificacion = (String.valueOf(rating));
				if(rating==0.0){
					califica_taxi_tv_titulo_calif.setText(getResources().getString(R.string.Califica_taxi_0));
				}
				if(rating==0.5){
					califica_taxi_tv_titulo_calif.setText(getResources().getString(R.string.Califica_taxi_05));
				}
				if(rating==1.0){
					califica_taxi_tv_titulo_calif.setText(getResources().getString(R.string.Califica_taxi_10));
				}
				if(rating==1.5){
					califica_taxi_tv_titulo_calif.setText(getResources().getString(R.string.Califica_taxi_15));
				}
				if(rating==2.0){
					califica_taxi_tv_titulo_calif.setText(getResources().getString(R.string.Califica_taxi_20));
				}
				if(rating==2.5){
					califica_taxi_tv_titulo_calif.setText(getResources().getString(R.string.Califica_taxi_25));
				}
				if(rating==3.0){
					califica_taxi_tv_titulo_calif.setText(getResources().getString(R.string.Califica_taxi_30));
				}
				if(rating==3.5){
					califica_taxi_tv_titulo_calif.setText(getResources().getString(R.string.Califica_taxi_35));
				}
				if(rating==4.0){
					califica_taxi_tv_titulo_calif.setText(getResources().getString(R.string.Califica_taxi_40));
				}
				if(rating==4.5){
					califica_taxi_tv_titulo_calif.setText(getResources().getString(R.string.Califica_taxi_45));
				}
				if(rating==5.0){
					califica_taxi_tv_titulo_calif.setText(getResources().getString(R.string.Califica_taxi_50));
				}
			}

			
		});
		
		Button calificar_aceptar =(Button)view.findViewById(R.id.dialogo_califica_servicio_btnAceptar);
		calificar_aceptar.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				Utils util = new Utils(SafeBusMainActivity.this);
				
				util.setPreferenciasPlaca(util.getPreferenciasPlaca()[0], Scalificacion);
				
				Utils.doHttpPostCalificacionUsuario(SafeBusMainActivity.this, Scalificacion, comentario.getText().toString());
				
				customDialog.dismiss();
			}
		});
			

		return (customDialog = builder.create());
	}
	
	/**
	 * escucha para saber cuantos caracteres quedan al dejar un comentario
	 */
	private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        	dialogo_califica_tv_caracteres.setText(String.valueOf(s.length()+"/50"));
        }

        public void afterTextChanged(Editable s) {
        }
};

	
	
}