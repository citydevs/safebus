package com.bm.safebus.instrucciones.paginas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.bm.safebus.R;
import com.bm.savebus.utilerias.Fonts;
import com.bm.savebus.utilerias.Utils;

/**
 * pagina que muestra en una lista los adeudos de un carro con las secretarias
 * 
 * @author mikesaurio
 *
 */
@SuppressLint("ViewConstructor")
public class PaginaMando extends View {

	
	private View view;
	private Activity context;

	
	
	public PaginaMando(Activity context) {
		super(context);
		this.context = context;
		init();
	}

	public PaginaMando(Activity context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public PaginaMando(Activity context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}


	
	/**
	 * init de la pagina
	 */
	public void init() {


		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.instrucciones_pag_mando, null);
		
		((TextView)view.findViewById(R.id.instrucciones_pag_1_tv_titulo)).setTypeface(new Fonts(context).getTypeFace(Fonts.FLAG_BLACK));
		((TextView)view.findViewById(R.id.instrucciones_pag_1_tv_contenido)).setTypeface(new Fonts(context).getTypeFace(Fonts.FLAG_LIGHT));
		
		Switch  mySwitch = (Switch) view.findViewById(R.id.mySwitch);
		  

		  mySwitch.setChecked(new Utils(context).getPreferenciasMando());
		 

		  mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		 
		   @Override
		   public void onCheckedChanged(CompoundButton buttonView,
		     boolean isChecked) {

		    	new Utils(context).setPreferenciasMando(isChecked);

		   }
		  });
		 }
		

	


	/**
	 * GET view
	 * @return (view) vista inflada
	 */
	public View getView() {
		return view;
	}

}
