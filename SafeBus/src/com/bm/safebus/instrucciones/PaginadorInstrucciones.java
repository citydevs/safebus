package com.bm.safebus.instrucciones;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bm.safebus.R;
import com.bm.safebus.SafeBusMainActivity;
import com.bm.safebus.instrucciones.adaptadores.FragmentPagerAdapterDialog;
import com.bm.safebus.instrucciones.adaptadores.ScreenSlidePageFragmentDialog;
import com.bm.safebus.instrucciones.paginas.PaginaDosGuia;
import com.bm.safebus.instrucciones.paginas.PaginaDosGuia.OnListenerMas;
import com.bm.safebus.registro.ContactoActivity;
import com.bm.savebus.utilerias.Utils;



public class PaginadorInstrucciones extends FragmentActivity  implements OnListenerMas {
	
	public final String TAG = this.getClass().getSimpleName();

	private ViewPager pager = null;

	


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	     requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.setContentView(R.layout.paginador_activity);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		
		
		pager = (ViewPager)findViewById(R.id.pager_dialog);
		pager.setOffscreenPageLimit(4);
		
		
		FragmentPagerAdapterDialog adapter = new FragmentPagerAdapterDialog(getSupportFragmentManager());
		ScreenSlidePageFragmentDialog fragment = ScreenSlidePageFragmentDialog.newInstance(getResources().getColor(R.color.android_blue), 1,PaginadorInstrucciones.this);
		adapter.addFragment(fragment);
		adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources().getColor(R.color.android_red), 2,PaginadorInstrucciones.this));
		
		
		PaginaDosGuia.initialize( this );
		
		PaginadorInstrucciones.this.pager.setAdapter(adapter);
		
		ImageView btn_siguiente =(ImageView)findViewById(R.id.instrucciones_btn_siguiente);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width / 2,height / 3);
		btn_siguiente.setLayoutParams(lp);
		
		btn_siguiente.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(PaginadorInstrucciones.this.pager.getCurrentItem()==0){
					PaginadorInstrucciones.this.pager.setCurrentItem(1);
				}else{
					
					new Utils(PaginadorInstrucciones.this).setPreferenciasSplash();
					startActivity(new Intent(PaginadorInstrucciones.this,SafeBusMainActivity.class));
					finish();
				}
				
			}
		});
		
		
		
	}
	
       
	  
	
	@Override
	protected void onDestroy() {
		pager=null;
		super.onDestroy();
	}






	@Override
	public void onListenerMas() {
		startActivityForResult(new Intent(PaginadorInstrucciones.this,ContactoActivity.class),1);
		
	}




	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
	}


	
	
	
	
	   
}
