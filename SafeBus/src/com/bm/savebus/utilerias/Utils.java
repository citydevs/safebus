package com.bm.savebus.utilerias;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.view.Display;

public class Utils {

	Activity activity;

	public Utils(Activity activity) {
		this.activity = activity;

	}

	public void setPreferenciasSplash() {

		SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBus", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("splash", true);
		editor.commit();
	}

	public boolean getPreferenciasSplash() {

		SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBus", Context.MODE_PRIVATE);
		return prefs.getBoolean("splash", false);
	}
	

	public void setPreferenciasContacto(String[] info) {

		SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBus", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("telefono", info[0]);
		editor.putString("mensaje", info[1]);
		editor.commit();
	}

	public String[] getPreferenciasContacto() {

		SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBus", Context.MODE_PRIVATE);
		String[] info = new String[2];
		info[0]=prefs.getString("telefono", null);
		info[1]=prefs.getString("mensaje", null);
		return info;
	}
	
	/**
	 * metodo que vaida que el telefono tenga internet
	 * 
	 * @param a
	 * @return
	 */
	public static boolean hasInternet(Activity a) {
		boolean hasConnectedWifi = false;
		boolean hasConnectedMobile = false;
		ConnectivityManager cm = (ConnectivityManager) a
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("wifi"))
				if (ni.isConnected())
					hasConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("mobile"))
				if (ni.isConnected())
					hasConnectedMobile = true;
		}
		return hasConnectedWifi || hasConnectedMobile;
	}
	
	/**
	 * metodo que vaida que el telefono tenga GPS encendido
	 * @param a
	 * @return
	 */
	public static boolean  hasGPS(Activity a){
		LocationManager mLocationManager = (LocationManager) a.getSystemService(Context.LOCATION_SERVICE);
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true;
		}
		return false;
	}
	
	/**
	 * metodo que hace la conexion al servidor con una url especifica
	 * 
	 * @param url
	 *            (String) ruta del web service
	 * @return (String) resultado del service
	 */
	public static String doHttpConnection(String url) {
		HttpClient Client = new DefaultHttpClient();
		try {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			HttpGet httpget = new HttpGet(url);
			HttpResponse hhrpResponse = Client.execute(httpget);
			HttpEntity httpentiti = hhrpResponse.getEntity();
			// Log.d("RETURN HTTPCLIENT", EntityUtils.toString(httpentiti));
			return EntityUtils.toString(httpentiti);
		} catch (ParseException e) {
			
			return null;
		} catch (IOException e) {
			
			return null;
		}
	}

	/**
	 * obtienes el tama�o de pantalla
	 * @param (activity) Activity
	 * @return (Point) .x = width
	 * 					.y = height 
	 */
		public static Point getTamanoPantalla(Activity activity){
			Display display = activity.getWindowManager().getDefaultDisplay();
			Point size = new Point();
			 display.getSize(size);
			int width = size.x;
			int height = size.y;
			return (new Point (width,height));
		}
	

}
