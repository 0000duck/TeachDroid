package com.keba.teachdroid.app;

import java.io.Serializable;

import com.keba.teachdroid.app.ProjectActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public abstract class BaseActivity extends FragmentActivity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 127495936935186417L;
	protected transient DrawerLayout mDrawerLayout;
	protected transient ListView mDrawerList;
	protected transient ActionBarDrawerToggle mDrawerToggle;
	protected CharSequence mDrawerTitle;
	protected CharSequence mTitle;
	protected String[] mNavigationStrings;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_action_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	protected void switchActivity(int position) {
		Intent intent = null;
		switch (position) {
		case 0:
			if (!(this instanceof ProjectActivity)) {
				intent = new Intent(getBaseContext(), ProjectActivity.class);
			} else {
				mDrawerLayout.closeDrawers();
			}
			break;
		case 1:
			if (!(this instanceof RobotActivity)) {
				intent = new Intent(getBaseContext(), RobotActivity.class);
			} else {
				mDrawerLayout.closeDrawers();
			}
			break;
		case 2:
			if (!(this instanceof InfoActivity)) {
				intent = new Intent(getBaseContext(), InfoActivity.class);
			} else {
				mDrawerLayout.closeDrawers();
			}
			break;
		default:
			mDrawerLayout.closeDrawers();
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	protected class MyActionBarDrawerToggle extends ActionBarDrawerToggle {

		public MyActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int drawerImageRes, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
			super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes, closeDrawerContentDescRes);
		}

		public void onDrawerClosed(View view) {
			getActionBar().setTitle(mTitle);
			invalidateOptionsMenu(); // creates call to
										// onPrepareOptionsMenu()
		}

		public void onDrawerOpened(View drawerView) {
			getActionBar().setTitle(mDrawerTitle);
			invalidateOptionsMenu(); // creates call to
										// onPrepareOptionsMenu()
		}
	}

	/* The click listener for ListView in the navigation drawer */
	protected class DrawerItemClickListener implements ListView.OnItemClickListener, Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6643918920693315503L;

		public DrawerItemClickListener() {
			super();
		}

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mDrawerList.setItemChecked(position, true);
			switchActivity(position);
		}
	}
}
