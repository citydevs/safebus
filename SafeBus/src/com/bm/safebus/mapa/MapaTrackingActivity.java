package com.bm.safebus.mapa;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bm.safebus.R;
import com.bm.safebus.mapa.bean.MapaBean;
import com.bm.savebus.utilerias.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikesaurio.mensajesydialogos.Mensajes;
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
		private ArrayList<MapaBean>	mapaBeanArray= new ArrayList<MapaBean>();
		private boolean isFirstTime=true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chofer_main);
		
		//cargamos el mapa
		setUpMapIfNeeded();
		
		//iniciamos el servicio de localizacion
		ServicioLocalizacion.activity = MapaTrackingActivity.this;
		startService(new Intent(MapaTrackingActivity.this,ServicioLocalizacion.class));
		
	ImageView	mapa_iv_back =(ImageView)findViewById(R.id.mapa_iv_back);
	mapa_iv_back.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			onBackPressed();
			
		}
	});

		
		 	
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
		if(Utils.hasInternet(MapaTrackingActivity.this)){
			buscarBuses();
			
			map.clear();
			marker = new MarkerOptions().position(latLng).title("mi ubicaci—n");
			marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_usuario_chinche));
			
			if(isFirstTime){
				CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				isFirstTime=false;
			}else{
				CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(map.getCameraPosition().zoom).build();
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			}
			
			 
			// adding marker
			map.addMarker(marker);

			
			
			
			MarkerOptions[] markers= new MarkerOptions[mapaBeanArray.size()];
			
			for(int i=0;i<mapaBeanArray.size();i++){
				markers[i]=new MarkerOptions().position(mapaBeanArray.get(i).getPunto()).title(mapaBeanArray.get(i).getPlaca());
				markers[i].icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_bus_verde));
				map.addMarker(markers[i]);
			}
			
			
						
			
		}
		
		
		
		if(pDialog!=null){
	    	pDialog.dismiss();
	    	}
	}
	
	
	
	private void buscarBuses() {
		mapaBeanArray.clear();
		
		String url= "http://cryptic-peak-2139.herokuapp.com/buses.json";
		 final String strigJson = new  Utils(MapaTrackingActivity.this).doHttpConnection(url);
		JSONArray jObj;
		try {
			jObj = new JSONArray(strigJson);
			for (int i=0; i < jObj.length(); i++) {
				MapaBean mapaBean = new MapaBean();
				    JSONObject obj = jObj.getJSONObject(i);
				    String placa = obj.getString("placa");
				    mapaBean.setPlaca(placa);
				    
				    	JSONObject subObj = obj.getJSONObject("route");
				    	String id = subObj.getString("id");
				    	mapaBean.setRuta_id(id);
				    	
				    	JSONArray jArr = obj.getJSONArray("locations");
				    //	for (int j=0; j < jArr.length(); j++) {
				    	    	JSONObject objs = jArr.getJSONObject(jArr.length()-1);
						    	 String Slat=   objs.getString("lat");
						    	 String Slng=   objs.getString("lng");
						    	 mapaBean.setPunto(new LatLng(Double.parseDouble(Slat), Double.parseDouble(Slng)));
				    	 
						    	 mapaBeanArray.add(mapaBean);

				    //	}

				    	

				}


		} catch (JSONException e) {
			e.printStackTrace();
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
