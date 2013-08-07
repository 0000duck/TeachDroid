package com.keba.teachdroid.app.fragments;

import com.keba.teachdroid.app.ProjectActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public abstract class BaseActivity extends FragmentActivity {
	protected DrawerLayout mDrawerLayout;
	protected ListView mDrawerList;
	protected ActionBarDrawerToggle mDrawerToggle;
	protected CharSequence mDrawerTitle;
	protected CharSequence mTitle;
	protected String[] mNavigationStrings;

	protected void switchActivity(int position) {
		Intent intent = null;
		switch (position) {
		case 0:
			if (!(this instanceof ProjectActivity)) {
				intent = new Intent(this, ProjectActivity.class);
			}
			break;
		case 1:
			break;
		case 2:
			break;
		default:
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
	protected class DrawerItemClickListener implements ListView.OnItemClickListener {
		public DrawerItemClickListener() {
			super();
		}

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mDrawerList.setItemChecked(position, true);
			switchActivity(position);
		}
	}
}
