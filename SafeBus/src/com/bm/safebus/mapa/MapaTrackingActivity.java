package com.bm.safebus.mapa;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.bm.safebus.R;
import com.bm.safebus.dialogos.Mensajes;
import com.bm.savebus.utilerias.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikesaurio.modulolocalizacion.ServicioLocalizacion;

/**
 * 
 * @author mikesaurio
 *
 */
public class MapaTrackingActivity extends Activity {
	    private GoogleMap map;
	  	private ProgressDialog pDialog;
		private MarkerOptions marker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chofer_main);
		
		//cargamos el mapa
		setUpMapIfNeeded();
		
		//iniciamos el servicio de localizacion
		ServicioLocalizacion.activity = MapaTrackingActivity.this;
		startService(new Intent(MapaTrackingActivity.this,ServicioLocalizacion.class));
		
		
		String url= "http://cryptic-peak-2139.herokuapp.com/buses.json";
		String strigJson = new  Utils(MapaTrackingActivity.this).doHttpConnection(url);
		
		 	
	}
	
	
	public void setUpMapIfNeeded() {
		if (map == null) {
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			if (map != null) {
				if(setUpMap()) {
					initMap();
				}
			}
		}
	}
	
	public void initMap() {
		//iniciamos un anillo de espera
		pDialog=Mensajes.ringDialog(MapaTrackingActivity.this, "espera....");
		pDialog.show();
		
		map.setMyLocationEnabled(false);//quitar circulo azul;
		map.setBuildingsEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.getUiSettings().setZoomControlsEnabled(true); //ZOOM
		map.getUiSettings().setCompassEnabled(true); //COMPASS
		map.getUiSettings().setZoomGesturesEnabled(true); //GESTURES ZOOM
		map.getUiSettings().setRotateGesturesEnabled(true); //ROTATE GESTURES
		map.getUiSettings().setScrollGesturesEnabled(true); //SCROLL GESTURES
		map.getUiSettings().setTiltGesturesEnabled(true); //TILT GESTURES
		map.getUiSettings().setZoomControlsEnabled(false);
	}
	
	
	
	public void actualizarMapa(LatLng latLng){
			// create marker
		map.clear();
		
		marker = new MarkerOptions().position(latLng).title("ubicaci—n");
		marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		
		CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(18).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		 
		// adding marker
		map.addMarker(marker);
		
		if(pDialog!=null){
	    	pDialog.dismiss();
	    	}
	}
	
	/**
	 * revisa si el mapa esta 
	 * @return (boolean) true si el mapa esta listo 
	 */
	public boolean setUpMap() {
		if (!checkReady()) {
            return false;
        } else {
        	return true;
        }
	}
    
	/**
	 * revisa si el mapa esta listo
	 * @return (boolea) si esta listo TRUE
	 */
	private boolean checkReady() {
        if (map == null) {
            return false;
        }
        return true;
    }
	
	
	
	/**
	 * manejo de transmiciones
	 */
	
	private BroadcastReceiver onBroadcast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context ctxt, Intent t) {

			 ArrayList<String> pointsLat = t.getStringArrayListExtra("latitud");
			 ArrayList<String> pointsLon = t.getStringArrayListExtra("longitud");
			 
			 actualizarMapa(new LatLng(Double.parseDouble(pointsLat.get(pointsLat.size()-1)),
					 Double.parseDouble(pointsLon.get(pointsLon.size()-1))));
			
		}
	};


	@Override
	protected void onPause() {
		unregisterReceiver(onBroadcast);
		super.onPause();
	}

	@Override
	protected void onResume() {
		registerReceiver(onBroadcast, new IntentFilter("key"));
		super.onResume();
	}
	
	 @Override
		protected void onDestroy() {
		if(pDialog!=null){
	    	pDialog.dismiss();
	    	}
		stopService(new Intent(MapaTrackingActivity.this,ServicioLocalizacion.class));
			super.onDestroy();
		}
	
	 
	 
	

}
