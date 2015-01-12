package com.bm.safebus;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.safebus.customes.CustomList;




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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		
	
		
		selectItem(0);

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
			selectItem(position);
		}
	}
	
	/**
	 * item de la lista seleccionado
	 * @param position
	 */
	private void selectItem(int position) {
		
		if(position==0){
			supportInvalidateOptionsMenu();
			 fragment = new SafeBusDashboardFragment(SafeBusMainActivity.this);
			Bundle args = new Bundle();
			fragment.setArguments(args);
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			navList.setItemChecked(position, true);
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
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	
	
	
	
}