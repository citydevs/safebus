package com.bm.safebus.instrucciones.paginas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.safebus.R;
import com.bm.safebus.instrucciones.PaginadorInstrucciones;
import com.bm.safebus.instrucciones.PaginadorInstrucciones.OnListenerCambiarTexto;
import com.bm.safebus.registro.ContactoActivity;
import com.bm.savebus.utilerias.Utils;

/**
 * pagina que muestra en una lista los adeudos de un carro con las secretarias
 * 
 * @author mikesaurio
 *
 */
@SuppressLint("ViewConstructor")
public class PaginaDosGuia extends View implements OnListenerCambiarTexto {

	
	private View view;
	private Activity context;
	private static OnListenerMas onListenerMas;
	private TextView p2_tv_titulo,p2_tv_da_click;
	
	
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
		
		
		PaginadorInstrucciones.setOnClickCambiarTextoListener(this); //escucha del cambio de texto
		
		p2_tv_titulo =(TextView)view.findViewById(R.id.instrucciones_p2_tv_titulo);
		p2_tv_da_click =(TextView)view.findViewById(R.id.instrucciones_p2_tv_da_click);
		
		String[] info= new Utils(context).getPreferenciasContacto();
		if(info[0]!=null){
			cambiarTexto(PaginadorInstrucciones.CONTACTO_NO_GUARDADO);
				
		} else {
			cambiarTexto(PaginadorInstrucciones.CONTACTO_GUARDADO);
		}
		
		
		
		
		ImageView instrucciones_iv_mas= (ImageView)view.findViewById(R.id.instrucciones_iv_mas);
		instrucciones_iv_mas.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onListenerMas.onListenerMas();
				
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
	
	
	/**
	 * Interface que comunica la pagina con la actividad 
	 * @author mikesaurio
	 *
	 */
	 public interface OnListenerMas
	    {
	        void onListenerMas();
	    }

	 /**
	  * escucha para poder dar click a siguiente pagina
	  * @param listener
	  */
	public static void setOnClickSiguienteListener( OnListenerMas listener)
	{
		onListenerMas = listener;
	}

	@Override
	public void onListenerCambiarTexto(int tipo) {
		
		cambiarTexto( tipo);
	}
	
	
	public void cambiarTexto(int tipo){
		switch (tipo) {
		case PaginadorInstrucciones.CONTACTO_GUARDADO:
			p2_tv_titulo.setText(getResources().getString(R.string.paginas_instrucciones_texto_pag_2_1));
			p2_tv_da_click.setText(getResources().getString(R.string.paginas_instrucciones_texto_pag_2_2));
			break;
		case PaginadorInstrucciones.CONTACTO_NO_GUARDADO:
			p2_tv_titulo.setText(getResources().getString(R.string.paginas_instrucciones_texto_pag_2_1_listo));
			p2_tv_da_click.setText(getResources().getString(R.string.paginas_instrucciones_texto_pag_2_2_listo));
			break;

		default:
			break;
		}
	}
}
