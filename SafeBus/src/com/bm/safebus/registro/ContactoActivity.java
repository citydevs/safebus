package com.bm.safebus.registro;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bm.safebus.R;
import com.bm.safebus.instrucciones.PaginadorInstrucciones;
import com.bm.safebus.registro.validador.EditTextValidator;
import com.bm.savebus.utilerias.Utils;
import com.mikesaurio.mensajesydialogos.Mensajes;

/**
 * 
 * @author mikesaurio
 *
 */
public class ContactoActivity extends Activity  {
	private ImageButton btn_contacto;
	private ArrayList<String> listaCels;
	private EditTextBackEvent et_telefono,et_mensaje_emergencia;
	AlertDialog customDialog= null;
	private Button btn_guardar;
	private ImageView ImageView_titulo_contacto;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	     requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_contacto);
		
		
		et_telefono=(EditTextBackEvent)findViewById(R.id.registro_et_telefono);
		et_mensaje_emergencia=(EditTextBackEvent)findViewById(R.id.registro_et_mensaje_emergencia);
		
		btn_guardar=(Button)findViewById(R.id.registro_btn_guardar);
		btn_guardar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(validaEditText()){
					new Utils(ContactoActivity.this).setPreferenciasContacto(
							new String[]{et_telefono.getText().toString(),et_mensaje_emergencia.getText().toString()});
					Mensajes.Toast(ContactoActivity.this, "Contacto guardado", Toast.LENGTH_SHORT);
					
					Intent returnIntent = new Intent();
					setResult(PaginadorInstrucciones.CONTACTO_GUARDADO, returnIntent);
					finish();
				}
			}
		});
		
		
		btn_contacto=(ImageButton)findViewById(R.id.registro_btn_contacto);
		btn_contacto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);
				startActivityForResult(intent, 0);
			}
		});
		
		
		String[] info= new Utils(ContactoActivity.this).getPreferenciasContacto();
		if(info[0]!=null){
			et_telefono.setText(info[0]);
			et_mensaje_emergencia.setText(info[1]);
		}
		
		
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,Utils.getTamanoPantalla(ContactoActivity.this).y/10);
		ImageView_titulo_contacto=(ImageView)findViewById(R.id.ImageView_titulo_contacto);
		ImageView_titulo_contacto.setLayoutParams(lp);
		
	

				
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {	
			getContactInfo(data,0);
		}
 		
	}
	
	
	/**
	 * metodo que llena tanto el numero celular como correo de emergencia con los contactos del usuario
	 * @param intent
	 * @param tag
	 */
	public void getContactInfo(Intent intent, int tag)
	{
		try{
			
			
			@SuppressWarnings("deprecation")
			Cursor   cursor =  managedQuery(intent.getData(), null, null, null, null);      
			  if(!cursor.isClosed()&&cursor!=null){
			   while (cursor.moveToNext()) 
			   {           
			       String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			       String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
		
			       if ( hasPhone.equalsIgnoreCase("1")){
			           hasPhone = "true";
			           
			       }else{
			           hasPhone = "false" ;
			       }
			       if (Boolean.parseBoolean(hasPhone)) 
			       {
			        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
			       listaCels= new ArrayList<String>();
			        while (phones.moveToNext()) 
			        {
			     
			          String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			          phoneNumber=  phoneNumber.replaceAll(" ", "");
			          
			          final char c = phoneNumber.charAt(0);
			          if(c=='+'){
			        	  try{
			        		  phoneNumber =  phoneNumber.substring(3, 13); 
			        	  }catch(Exception e){
			        		 
			        	  }
			          }
			          
			          listaCels.add(phoneNumber);
			        }
			        if(listaCels.size()==1){ //si tiene solo un telefono
			        	et_telefono.setText(listaCels.get(0)); 
			        	
			        }else if(listaCels.size()==0){//si no tiene telefono
			        	et_telefono.setText("");
			        }else{
			        	dialogoLista(tag+"");
			        }
			        phones.close();
			       }
		
			       // Find Email Addresses
			    /*   Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);

			       while (emails.moveToNext()) 
			       {
			        String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			       
			        et_correo.setText(emailAddress);
			        
			        break;
			       }
			       
			       emails.close();*/
			       break;
			  }  
			  }
		}catch(Exception e){
		
		}
	}
	
	
	/**
	 * agrega la vista del contacto de emergencia
	 */
	public void dialogoLista(final String tag){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    View view = getLayoutInflater().inflate(R.layout.dialogo_contactos, null);
		    builder.setView(view);
		    builder.setCancelable(true);
			final ListView listview = (ListView) view.findViewById(R.id.dialogo_contacto_lv_contactos);
			final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, listaCels);
		    listview.setAdapter(adapter);
		    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {
		    final String item = (String) parent.getItemAtPosition(position);
		    et_telefono.setText(item.replaceAll(" ",""));
		         customDialog.dismiss();
		       }
		     });
	     customDialog=builder.create();
	     customDialog.show();
	}
	
	
	/**
	 * valida todos los editText 
	 * @return (true) si el editText esta bie llenado
	 */
	public boolean validaEditText() {
	
      	 //validamos que no esten vacios
      	  if(et_telefono.getText().toString().equals("")){
      		  et_telefono.setError(getResources().getString(R.string.registro_telefono_vacio));
      		//  Mensajes.simpleToast(ContactoActivity.this, getResources().getString(R.string.registro_telefono_vacio), Toast.LENGTH_LONG);
      		  return false;
      	  }
    	  if(et_mensaje_emergencia.getText().toString().equals("")){
    		  et_mensaje_emergencia.setError(getResources().getString(R.string.registro_correo_vacio));
    		 // Mensajes.simpleToast(ContactoActivity.this, getResources().getString(R.string.registro_correo_vacio), Toast.LENGTH_LONG);
    		  return false;
    	  }
    	  //validamos que esten bien escritos
    	  if(et_telefono.getText().toString().length()!=10){
    		  et_telefono.setError(getResources().getString(R.string.registro_telefono_largo));
    		//  Mensajes.simpleToast(ContactoActivity.this,getResources().getString(R.string.registro_telefono_largo), Toast.LENGTH_LONG);
      		  return false;
      	  }
    	  if(EditTextValidator.isNumeric(et_telefono.getText().toString())){
    		  et_telefono.setError(getResources().getString(R.string.registro_telefono_incorrecto));
      		  return false;  
    	  }
    	  
    	/*  if(!EditTextValidator.esCorreo(et_correo)){
    		  et_correo.setError(getResources().getString(R.string.registro_correo_incorrecto));
			  return false;
		  }*/
		return true;
	}



	@Override
	public void onBackPressed() {
		String[] info= new Utils(ContactoActivity.this).getPreferenciasContacto();
		if(info[0]!=null){
			Mensajes.Toast(ContactoActivity.this, "Datos actualizados", Toast.LENGTH_SHORT);
			Intent returnIntent = new Intent();
			setResult(PaginadorInstrucciones.CONTACTO_GUARDADO, returnIntent);
			finish();
				
		} else {
			Mensajes.Toast(ContactoActivity.this, "Datos NO guardados", Toast.LENGTH_SHORT);
			Intent returnIntent = new Intent();
			setResult(PaginadorInstrucciones.CONTACTO_NO_GUARDADO, returnIntent);
			finish();
		}
		
		
	}

	
	
	
	
	
}
