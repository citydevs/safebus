package com.bm.safebus.instrucciones;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bm.safebus.R;
import com.bm.safebus.gcm.GCM;
import com.bm.safebus.instrucciones.adaptadores.FragmentPagerAdapterDialog;
import com.bm.safebus.instrucciones.adaptadores.ScreenSlidePageFragmentDialog;
import com.bm.safebus.instrucciones.paginas.PaginaDosGuia;
import com.bm.safebus.instrucciones.paginas.PaginaDosGuia.OnListenerMas;
import com.bm.safebus.registro.ContactoActivity;
import com.bm.savebus.utilerias.Utils;
import com.google.android.gms.gcm.GoogleCloudMessaging;


/**
 * Paginador que muestra el instructivo de la app
 * @author mikesaurio
 *
 */
public class PaginadorInstrucciones extends FragmentActivity  implements OnListenerMas ,OnClickListener,OnPageChangeListener{
	
	private ViewPager pager = null;
	private ImageView btn_siguiente;
	public final static int CONTACTO_GUARDADO=0;
	public final static int CONTACTO_NO_GUARDADO=1;
	private static OnListenerCambiarTexto onListenerCambiarTexto;
	
	
	//GCM
    	private GoogleCloudMessaging gcm;
		private GCM mGCM;

	  
	 


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	     requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.setContentView(R.layout.paginador_activity);
	
			
		pager = (ViewPager)findViewById(R.id.pager_dialog);
		pager.setOffscreenPageLimit(5);
		
		/*Creamos las paginas*/
		FragmentPagerAdapterDialog adapter = new FragmentPagerAdapterDialog(getSupportFragmentManager());
		adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources().getColor(R.color.android_blue), 1,PaginadorInstrucciones.this));
		adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources().getColor(R.color.android_red), 4,PaginadorInstrucciones.this));
		adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources().getColor(R.color.android_red), 2,PaginadorInstrucciones.this));
		adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources().getColor(R.color.android_red), 3,PaginadorInstrucciones.this));
		
		
		PaginaDosGuia.setOnClickSiguienteListener( this ); //escucha del boton siguiente
		
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(this);
		
		
		btn_siguiente =(ImageView)findViewById(R.id.instrucciones_btn_siguiente); 
		Point p = Utils.getTamanoPantalla(PaginadorInstrucciones.this); //tama��o de pantalla
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(p.x / 2, p.y / 3);
		btn_siguiente.setLayoutParams(lp);
		btn_siguiente.setOnClickListener(this);	
	}
	
	@Override
	protected void onDestroy() {
		pager=null;
		super.onDestroy();
	}

	@Override
	public void onListenerMas() {
		startActivityForResult(new Intent(PaginadorInstrucciones.this,ContactoActivity.class),9);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.instrucciones_btn_siguiente:
			if(PaginadorInstrucciones.this.pager.getCurrentItem()==0){
				PaginadorInstrucciones.this.pager.setCurrentItem(1);
			}else if(PaginadorInstrucciones.this.pager.getCurrentItem()==1){
				PaginadorInstrucciones.this.pager.setCurrentItem(2);
			}else if(PaginadorInstrucciones.this.pager.getCurrentItem()==2){
				PaginadorInstrucciones.this.pager.setCurrentItem(3);
			}else{
				//activar GSM 
				 mGCM= new GCM(PaginadorInstrucciones.this);
				  if (mGCM.checkPlayServices()) {
			            gcm = GoogleCloudMessaging.getInstance(this);
			            mGCM.registerInBackground(gcm);       
			        }
			}
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}
	@Override
	public void onPageSelected(int index) {

		if(index==0|| index == 1|| index == 2){
			btn_siguiente.setImageResource(R.drawable.boton_siguiente_selector);
		}else if(index==3){
			btn_siguiente.setImageResource(R.drawable.boton_entiendo_selector);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == 9) {
	       switch (resultCode) {
		case CONTACTO_GUARDADO:
			onListenerCambiarTexto.onListenerCambiarTexto(CONTACTO_NO_GUARDADO);
			break;
		case CONTACTO_NO_GUARDADO:
			onListenerCambiarTexto.onListenerCambiarTexto(CONTACTO_GUARDADO);	
					break;
		default:
			break;
		}
	    	
	    }
	}


		/**
		 * Interface que comunica la pagina con la actividad 
		 * @author mikesaurio
		 *
		 */
		 public interface OnListenerCambiarTexto
		    {
		        void onListenerCambiarTexto(int tipo);
		    }
		
		 /**
		  * escucha para poder cambair el texto de la pagina 2
		  * @param listener
		  */
		public static void setOnClickCambiarTextoListener( OnListenerCambiarTexto listener)
		{
		
			onListenerCambiarTexto = listener;
		}
			
		// You need to do the Play Services APK check here too.
		@Override
		protected void onResume() {
		    super.onResume();
		    if(mGCM!=null)
		    	mGCM.checkPlayServices();
		}
		
		
		
		
		
}
