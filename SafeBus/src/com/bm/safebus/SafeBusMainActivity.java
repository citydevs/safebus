package com.bm.safebus;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.safebus.customes.CustomList;
import com.bm.safebus.fragments.SafeBusDashboardFragment;
import com.bm.safebus.registro.ContactoActivity;
import com.bm.safebus.splash.SplashActivity;
import com.bm.savebus.utilerias.Utils;
import com.mikesaurio.mensajesydialogos.Mensajes;




/**
 * Clase principal que contiene las diferentes opciones que se pueden hacer 
 * 
 * @author mikesaurio
 * 
 */
public class SafeBusMainActivity extends ActionBarActivity {
	
	private DrawerLayout drawerLayout;
	private ListView navList;
	private  CustomList adapter;

	private ActionBarDrawerToggle drawerToggle;
	private SafeBusDashboardFragment fragment;
	
	private Menu menu;
	String[] info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (new Utils(SafeBusMainActivity.this).getPreferenciasGCM()==null) {//si ya registro
			startActivity(new Intent().setClass(SafeBusMainActivity.this, SplashActivity.class));
			this.finish();
		} 
			
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_dashboard);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.marco));
		LayoutInflater mInflater = LayoutInflater.from(this);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		this.setTheme(R.style.AppTheme);
		
		
		int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
		TextView abTitle = (TextView) findViewById(titleId);
		abTitle.setTextColor(getResources().getColor(R.color.color_safebus_rojo));
		
		
		this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		this.navList = (ListView) findViewById(R.id.left_drawer);

		final String[] names = getResources().getStringArray(R.array.nav_options);


		adapter = new CustomList(SafeBusMainActivity.this,names);
		navList.setAdapter(adapter);
		navList.setOnItemClickListener(new DrawerItemClickListener());
		
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.drawable.ic_launcher_logo, R.string.app_name,R.string.app_name) {
			public void onDrawerClosed(View view) {
				supportInvalidateOptionsMenu();
			}
			public void onDrawerOpened(View drawerView) {
				supportInvalidateOptionsMenu();
			}
		};
		
		drawerLayout.setDrawerListener(drawerToggle);
		
	
		
		selectItem(-1,null);

			
	}

	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			
			return true;
		}
		 switch (item.getItemId()){
		 case R.id.menuadd:
		    	startActivity(new Intent(SafeBusMainActivity.this,ContactoActivity.class));
		      return true;
		    case R.id.menuabouth:
		    	Mensajes.mostrarAercaDe(SafeBusMainActivity.this).show();
		    	return true;
		    }

		return super.onOptionsItemSelected(item);
	}

	

/**
 * Clase que implementa escucha a la lista
 * @author mikesaurio
 *
 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position==0) {
				selectItem(position,getString(R.string.tel_inmujeres));
			}else if (position==1) {
				selectItem(position,getString(R.string.tel_policia));
			}else if (position==2) {
				selectItem(position,getString(R.string.tel_casssp));
			}
			
		}
	}
	
	/**
	 * item de la lista seleccionado
	 * @param position
	 */
	private void selectItem(int position, String tel) {
		
		if(position==-1){
			supportInvalidateOptionsMenu();
			fragment = new SafeBusDashboardFragment(SafeBusMainActivity.this);
			Bundle args = new Bundle();
			fragment.setArguments(args);
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			navList.setItemChecked(position, true);
			drawerLayout.closeDrawer(navList);

		}else {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
			startActivity(intent);
			drawerLayout.closeDrawer(navList);
		}
		
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = drawerLayout.isDrawerOpen(navList);
		menu.findItem(R.id.menu).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.menu_main, menu);
		  this.menu=menu;
		  MenuItem bedMenuItem = menu.findItem(R.id.menuadd);
		 	info= new Utils(SafeBusMainActivity.this).getPreferenciasContacto();
		  		if(info[0]!=null){
		  			 bedMenuItem.setTitle(getResources().getString(R.string.main_editar_contacto));
		  				
		  		} else {
		  			 bedMenuItem.setTitle(getResources().getString(R.string.main_agregar_contacto));
		  		}
		    return true;
	}

	
	@Override
	protected void onResume() {
		if(menu!=null){
		 MenuItem bedMenuItem = menu.findItem(R.id.menuadd);
		 	info= new Utils(SafeBusMainActivity.this).getPreferenciasContacto();
		  		if(info[0]!=null){
		  			 bedMenuItem.setTitle(getResources().getString(R.string.main_editar_contacto));
		  				
		  		} else {
		  			 bedMenuItem.setTitle(getResources().getString(R.string.main_agregar_contacto));
		  		}
	}
		super.onResume();
	}

	
	
}