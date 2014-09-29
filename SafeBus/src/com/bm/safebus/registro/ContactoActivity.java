package com.bm.safebus.registro;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.bm.savebus.R;

public class ContactoActivity extends Activity {
private ImageButton btn_contacto;
private ArrayList<String> listaCels;
private EditText et_telefono,et_correo;
AlertDialog customDialog= null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacto);
		
		
		et_telefono=(EditText)findViewById(R.id.registro_et_telefono);
		et_correo=(EditText)findViewById(R.id.registro_et_correo);
		
		
		btn_contacto=(ImageButton)findViewById(R.id.registro_btn_contacto);
		btn_contacto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);
				startActivityForResult(intent, 0);
				
			}
		});
		
		
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
			       Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);

			       while (emails.moveToNext()) 
			       {
			        String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			       
			        et_correo.setText(emailAddress);
			        
			        break;
			       }
			       
			       emails.close();
			       break;
			  }  
			  }
		}catch(Exception e){
		
		}
	}
	
	
	/**
	 * agrega la vista del contacto de emergencua
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
	
}
