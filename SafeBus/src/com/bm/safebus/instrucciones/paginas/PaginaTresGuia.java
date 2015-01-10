package com.bm.safebus.instrucciones.paginas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bm.safebus.R;

/**
 * pagina que muestra en una lista los adeudos de un carro con las secretarias
 * 
 * @author mikesaurio
 *
 */
@SuppressLint("ViewConstructor")
public class PaginaTresGuia extends View  {

	
	private View view;
	private Activity context;
	private TextView p2_tv_titulo,p2_tv_da_click;
	
	
	public PaginaTresGuia(Activity context) {
		super(context);
		this.context = context;
		init();
	}

	public PaginaTresGuia(Activity context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public PaginaTresGuia(Activity context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}


	
	/**
	 * init de la pagina
	 */
	public void init() {
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.instrucciones_pag_tres, null);
		
		final TextView instrucciones_pag_3_tv_tel_inmujeres = (TextView)view.findViewById(R.id.instrucciones_pag_3_tv_tel_inmujeres);
		instrucciones_pag_3_tv_tel_inmujeres.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:+"+instrucciones_pag_3_tv_tel_inmujeres.getText().toString().trim()));
				context.startActivity(callIntent );
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
