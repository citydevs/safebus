package com.bm.safebus.instrucciones.paginas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bm.safebus.R;

/**
 * pagina que muestra en una lista los adeudos de un carro con las secretarias
 * 
 * @author mikesaurio
 *
 */
@SuppressLint("ViewConstructor")
public class PaginaDosGuia extends View {

	
	private View view;
	private Activity context;
	private static OnListenerMas listener;
	
	
	public PaginaDosGuia(Activity context) {
		super(context);
		this.context = context;
		init();
	}

	public PaginaDosGuia(Activity context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public PaginaDosGuia(Activity context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}


	
	/**
	 * init de la pagina
	 */
	public void init() {


		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.instrucciones_pag_dos, null);
		ImageView instrucciones_iv_mas= (ImageView)view.findViewById(R.id.instrucciones_iv_mas);
		instrucciones_iv_mas.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onListenerMas();
				
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
	 public interface OnListenerMas
	    {
	        void onListenerMas();
	    }

	 
	public static void initialize( OnListenerMas listener)
	{

		PaginaDosGuia.listener = listener;
	}//
}
