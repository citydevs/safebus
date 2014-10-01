package com.bm.safebus.dialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.safebus.R;
/**
 * Clase dedicada a mostrar diversos tios de mensajes tanto al usuario como al programador
 * @author mikesaurio
 *
 */
public class Mensajes {
	private static Activity activity_mensajes;
	private static AlertDialog customDialog= null;
	public static final int FLAG_GPS=1;
	public static final int FLAG_WIFI=2;
	
	
	/**
	 * MEnsaje simple tipo Toast
	 * @param mensaje
	 * @param tiempo
	 */
	public  static void simpleToast(Context context,String mensaje,int tiempo){
		Toast.makeText(context, mensaje, tiempo).show();
	}
	
	/**
	 * Log sobreescrito para mostrar en logCat
	 * @param context  (Activity) actividad que lo llama
	 * @param text (String) texto a mostrar
	 * @param type (int) tipo del log
	 */
	public static void Log(Context context, String text, int type) {
		if(type == Log.DEBUG) {
			Log.d(context.getClass().getName().toString(), text);
		} else if(type == Log.ERROR) {
			Log.e(context.getClass().getName().toString(), text);
		} else if(type == Log.INFO) {
			Log.i(context.getClass().getName().toString(), text);
		} else if(type == Log.VERBOSE) {
			Log.v(context.getClass().getName().toString(), text);
		} else if(type == Log.WARN) {
			Log.w(context.getClass().getName().toString(), text);
		}
	}
	
	/**
	 * crea el dialogo 
	 */
	public static ProgressDialog ringDialog(Context context,String texto) {

		ProgressDialog pDialog = new ProgressDialog(context);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.setMessage(texto);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setCancelable(false);
		return pDialog;
	}	
	
	/**
	 * Dialogo para asegurar que quieres salir de la app
	 *
	 * @param Activity (actividad que llama al di‡logo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	
	public static Dialog showDialogGPSWiFi(final Activity activity,final int tipo){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    View view = activity.getLayoutInflater().inflate(R.layout.dialogo_gps_wifi, null);
	    builder.setView(view);
	    builder.setCancelable(true);
	    
	    if(tipo==FLAG_GPS){
	    	((TextView)view.findViewById(R.id.dialogo_gps_wifi_tv_descripcion)).setText("ÀDeseas encender el GPS?");
	    }else if(tipo==FLAG_WIFI){
	    	((TextView)view.findViewById(R.id.dialogo_gps_wifi_tv_descripcion)).setText("ÀDeseas encender alguna conexi—n a internet?");
	    }

        ((Button) view.findViewById(R.id.dialogo_gps_wifi_btn_aceptar)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
            	 customDialog.dismiss(); 
            	 if(tipo==FLAG_GPS){
	            	Intent settingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	 				settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	 				activity.startActivity(settingsIntent);
            	 }else if(tipo==FLAG_WIFI){
            		 activity.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            	 }
            }
        });

        ((Button) view.findViewById(R.id.dialogo_gps_wifi_btn_cancelar)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();    
            }
        });
        return (customDialog=builder.create());// return customDialog;//regresamos el di‡logo
    }   
	
	
	
	
}
