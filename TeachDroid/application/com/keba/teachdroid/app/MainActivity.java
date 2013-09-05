package com.keba.teachdroid.app;

import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor;

public class MainActivity extends BaseActivity /*
												 * implements
												 * InitializationListener,
												 * IConnectCallback
												 */{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Thread mNotificationThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mNavigationStrings = getResources().getStringArray(R.array.navigation_array);

		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mNavigationStrings));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// // enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new MyActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		);
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		findViewById(R.id.fragment_prog_code_main).setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				onShowProjects(v);
			}
		});
		findViewById(R.id.fragment_robot_main).setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				onShowPositions(v);
//				KvtDriveStateMonitor.toggleDrivesPower();
			}
		});

		findViewById(R.id.fragment_load_info_main).setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				onShowInfos(v);
			}
		});
		
		
		mNotificationThread = new NotificationThread(getBaseContext());
		mNotificationThread.start();
	}
	
	

	public void onShowProjects(View _v) {
		Intent projectsActivity = new Intent(this, ProjectActivity.class);
		startActivity(projectsActivity);
	}

	public void onShowPositions(View _v) {
		Intent robotActivity = new Intent(this, RobotActivity.class);
		startActivity(robotActivity);
	}

	public void onShowInfos(View _v) {
		Intent infoActivity = new Intent(this, InfoActivity.class);
		startActivity(infoActivity);
	}
}

// /**
// * @param _host
// *
// */
// private void checkWifiAndConnect(final String _host) {
// if (!RobotControlProxy.isConnected()) {
// final WifiManager wifi = (WifiManager)
// getSystemService(Context.WIFI_SERVICE);
// if (!wifi.isWifiEnabled()) {
//
// AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
//
// dlgAlert.setMessage("Click OK to enable WiFi, Cancel to dismiss!");
//
// dlgAlert.setTitle("Info");
// dlgAlert.setPositiveButton("OK", new
// android.content.DialogInterface.OnClickListener() {
//
// public void onClick(DialogInterface _dialog, int _which) {
// wifi.setWifiEnabled(true);
// connectToPlc(_host);
// }
// });
// dlgAlert.setNegativeButton("Cancel", /*
// * new android.content.
// * DialogInterface
// * .OnClickListener() {
// *
// * public void
// * onClick(DialogInterface
// * _dialog, int _which) {
// * finish(); } }
// */null);
// dlgAlert.setCancelable(true);
// dlgAlert.create().show();
//
// } else {
// connectToPlc(_host);
// }
// }
// }
//
// /**
// * @param _host
// *
// */
// private void connectToPlc(String _host) {
// // always read host from preferences, just in case someone has modified
// // it in the meantime
// // String host = PreferenceManager.getInstance().getHostname();
// mStartTime = System.currentTimeMillis();
// InitializationTask itask = new InitializationTask(this);
// itask.execute(_host);
//
// }
//
// @Override
// public boolean onCreateOptionsMenu(Menu menu) {
// // Inflate the menu; this adds items to the action bar if it is present.
// getMenuInflater().inflate(R.menu.main, menu);
//
// return true;
// }
//
// @Override
// public boolean onOptionsItemSelected(MenuItem _item) {
// // The action bar home/up action should open or close the drawer.
// // ActionBarDrawerToggle will take care of this.
// if (mDrawerToggle.onOptionsItemSelected(_item)) {
// return true;
// }
//
// if (_item.getTitle().equals(getString(R.string.menu_settings))) {
// Intent settingsActivity = new Intent(getBaseContext(),
// SettingsActivity.class);
// startActivity(settingsActivity);
//
// } else if (_item.getTitle().equals(getString(R.string.action_connect))) {
// if (!RobotControlProxy.isConnected()) {
// String host = PreferenceManager.getInstance().getHostname();
// checkWifiAndConnect(host);
// } else {
// disconnectFromPLC();
// }
// }
// return super.onOptionsItemSelected(_item);
// }
//
// /**
// *
// */
// private void disconnectFromPLC() {
// KvtSystemCommunicator.disconnect();
// }
//
// public void onConnStateClick(View _v) {
// if (RobotControlProxy.isConnected()) {
// disconnectFromPLC();
// } else {
// String host = PreferenceManager.getInstance().getHostname();
// checkWifiAndConnect(host);
// }
//
// }
//
//
//
// public void onGenericButtonClick(View _view) {
// RobotControlProxy.setRefsysName("foo");
// List rs = KvtPositionMonitor.getAvailableRefsys();
// List ts = KvtPositionMonitor.getAvailableTools();
//
// // ArrayAdapter adapter = new ArrayAdapter(this,
// // android.R.layout.simple_spinner_item, ts);
// //
// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// // Spinner toolSel = (Spinner) findViewById(R.id.toolSelectionCB);
// // toolSel.setAdapter(adapter);
// //
// // ArrayAdapter adapter2 = new ArrayAdapter(this,
// // android.R.layout.simple_spinner_item, rs);
// //
// adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// // Spinner refsyssel = (Spinner) findViewById(R.id.refsysSelectionCB);
// // refsyssel.setAdapter(adapter2);
// }
//
// /*
// * (non-Javadoc)
// *
// * @see
// * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
// * #initializationBegin()
// */
// public void initializationBegin() {
//
// runOnUiThread(new Runnable() {
//
// public void run() {
//
// // m_dlg = ProgressDialog.show(MainActivity.this,
// // "Connecting...", "Connecting to " + m_host, true, true);
// m_dlg = new ProgressDialog(MainActivity.this, ProgressDialog.STYLE_SPINNER);
// m_dlg.setTitle("Connecting...");
// // m_dlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//
// m_dlg.setCancelable(true);
// m_dlg.setCanceledOnTouchOutside(false);
// m_dlg.setMessage("Connecting to " +
// PreferenceManager.getInstance().getHostname());
//
// m_dlg.setIndeterminate(true);
// m_dlg.show();
//
// // m_dlg.setMessage("Begin initialization...");
//
// }
//
// });
//
// }
//
// /*
// * (non-Javadoc)
// *
// * @see
// * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
// * #initializationComplete()
// */
// public void initializationComplete(final boolean _success) {
//
// runOnUiThread(new Runnable() {
// public void run() {
// long duration = System.currentTimeMillis() - mStartTime;
// System.out.println("Connecting took me " + duration / 1000 + " ms");
// m_dlg.dismiss();
// // mViewPager.setCurrentItem(1, true);
// }
// });
//
// }
//
// /*
// * (non-Javadoc)
// *
// * @see
// * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
// * #setInitializationProgress(int)
// */
// public void setInitializationProgress(final Object _progress) {
// runOnUiThread(new Runnable() {
// public void run() {
// if (_progress instanceof Integer)
// m_dlg.setProgress((Integer) _progress);
// else
// m_dlg.setTitle(MessageFormat.format(m_connectFormatString + "{0}"/*
// * %
// * complete
// * "
// */, _progress));
//
// }
// });
// }
//
// /*
// * (non-Javadoc)
// *
// * @see com.keba.teachdroid.app.ConnectCallback#connect()
// */
// public void connect(String _host) {
// mSelectedProject = null;
// checkWifiAndConnect(_host);
// }
//
// /*
// * (non-Javadoc)
// *
// * @see com.keba.teachdroid.app.IConnectCallback#disconnect()
// */
// public void disconnect() {
// mSelectedProject = null;
// disconnectFromPLC();
// }
//
// /*
// * (non-Javadoc)
// *
// * @see
// * com.keba.teachdroid.app.fragments.projectview.ProjectListFragment.Callbacks
// * #onItemSelected(java.lang.String)
// */
// // public void onItemSelected(KvtProject _entry) {
// // mSelectedProject = _entry;
// //
// // if (mSelectedProject != null) {
// // Fragment f = mSectionsPagerAdapter.getItem(3);
// // if (f instanceof ProjectDetailFragment) {
// // ((ProjectDetailFragment) f).setProject(mSelectedProject);
// // }
// // mViewPager.setCurrentItem(3);
// // }
// // }
// }
