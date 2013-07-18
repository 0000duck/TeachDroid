package com.keba.teachdroid.app;

import java.text.MessageFormat;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.teachdroid.app.fragments.ConnectFragment;
import com.keba.teachdroid.app.fragments.OverviewFragment;
import com.keba.teachdroid.app.fragments.projectview.ProjectDetailFragment;
import com.keba.teachdroid.app.fragments.projectview.ProjectListActivity;
import com.keba.teachdroid.app.fragments.projectview.ProjectListFragment;
import com.keba.teachdroid.app.fragments.projectview.ProjectListFragment.Callbacks;
import com.keba.teachdroid.data.InitializationTask;
import com.keba.teachdroid.data.InitializationTask.InitializationListener;
import com.keba.teachdroid.data.RobotControlProxy;
import com.keba.teachdroid.util.PreferenceManager;

public class MainActivity extends FragmentActivity implements InitializationListener, KvtPositionMonitorListener, Callbacks, IConnectCallback {

	/**
	 * 
	 */
	private static final long			serialVersionUID		= 1L;
	// private String m_host = "10.0.0.5";s
	final String						m_connectFormatString	= "Connecting... ";
	protected volatile ProgressDialog	m_dlg;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter				mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager							mViewPager;
	private long						mStartTime;
	private String						mSelectedProject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// add listeners for rc data
		KvtPositionMonitor.addListener(this);

		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	/**
	 * 
	 */
	private void checkWifiAndConnect() {
		if (!RobotControlProxy.isConnected()) {
			final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			if (!wifi.isWifiEnabled()) {

				AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

				dlgAlert.setMessage("Click OK to enable WiFi, Cancel to dismiss!");

				dlgAlert.setTitle("Info");
				dlgAlert.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface _dialog, int _which) {
						wifi.setWifiEnabled(true);
						connectToPlc();
					}
				});
				dlgAlert.setNegativeButton("Cancel", /*
													 * new android.content.
													 * DialogInterface
													 * .OnClickListener() {
													 * 
													 * public void
													 * onClick(DialogInterface
													 * _dialog, int _which) {
													 * finish(); } }
													 */null);
				dlgAlert.setCancelable(true);
				dlgAlert.create().show();

			} else
				connectToPlc();
		}
	}

	/**
	 * 
	 */
	private void connectToPlc() {
		// always read host from preferences, just in case someone has modified
		// it in the meantime
		String host = PreferenceManager.getInstance().getHostname();
		mStartTime = System.currentTimeMillis();
		InitializationTask itask = new InitializationTask(this);
		itask.execute(host);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem _item) {

		if (_item.getTitle().equals(getString(R.string.menu_settings))) {
			Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
			startActivity(settingsActivity);

		} else if (_item.getTitle().equals(getString(R.string.action_connect))) {
			if (!RobotControlProxy.isConnected()) {
				checkWifiAndConnect();
			} else {
				disconnectFromPLC();
			}
		}
		return super.onOptionsItemSelected(_item);
	}

	/**
	 * 
	 */
	private void disconnectFromPLC() {
		KvtSystemCommunicator.disconnect();
	}

	public void onConnStateClick(View _v) {
		if (RobotControlProxy.isConnected()) {
			disconnectFromPLC();
		} else
			checkWifiAndConnect();
	}

	public void onShowProjects(View _v) {
		Intent projectsActivity = new Intent(this, ProjectListActivity.class);
		// projectsActivity.putExtra("projects",
		// RobotControlProxy.getProjects());
		startActivity(projectsActivity);
	}

	public void onGenericButtonClick(View _view) {
		RobotControlProxy.setRefsysName("foo");
		List rs = KvtPositionMonitor.getAvailableRefsys();
		List ts = KvtPositionMonitor.getAvailableTools();

		// ArrayAdapter adapter = new ArrayAdapter(this,
		// android.R.layout.simple_spinner_item, ts);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Spinner toolSel = (Spinner) findViewById(R.id.toolSelectionCB);
		// toolSel.setAdapter(adapter);
		//
		// ArrayAdapter adapter2 = new ArrayAdapter(this,
		// android.R.layout.simple_spinner_item, rs);
		// adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Spinner refsyssel = (Spinner) findViewById(R.id.refsysSelectionCB);
		// refsyssel.setAdapter(adapter2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
	 * #initializationBegin()
	 */
	public void initializationBegin() {

		runOnUiThread(new Runnable() {

			public void run() {

				// m_dlg = ProgressDialog.show(MainActivity.this,
				// "Connecting...", "Connecting to " + m_host, true, true);
				m_dlg = new ProgressDialog(MainActivity.this, ProgressDialog.STYLE_SPINNER);
				m_dlg.setTitle("Connecting...");
				// m_dlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

				m_dlg.setCancelable(true);
				m_dlg.setCanceledOnTouchOutside(false);
				m_dlg.setMessage("Connecting to " + PreferenceManager.getInstance().getHostname());

				m_dlg.setIndeterminate(true);
				m_dlg.show();

				// m_dlg.setMessage("Begin initialization...");

			}

		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
	 * #initializationComplete()
	 */
	public void initializationComplete(final boolean _success) {

		runOnUiThread(new Runnable() {
			public void run() {
				long duration = System.currentTimeMillis() - mStartTime;
				System.out.println("Connecting took me " + duration / 1000 + " ms");
				m_dlg.dismiss();
				mViewPager.setCurrentItem(1, true);
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
	 * #setInitializationProgress(int)
	 */
	public void setInitializationProgress(final Object _progress) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (_progress instanceof Integer)
					m_dlg.setProgress((Integer) _progress);
				else
					m_dlg.setTitle(MessageFormat.format(m_connectFormatString + "{0}"/*
																					 * %
																					 * complete
																					 * "
																					 */, _progress));

			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #cartesianPositionChanged(java.lang.String, java.lang.Number)
	 */
	public void cartesianPositionChanged(String _compName, Number _value) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #axisPositionChanged(int, java.lang.Number, java.lang.String)
	 */
	public void axisPositionChanged(int _axisNo, Number _value, String _axisName) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #overrideChanged(java.lang.Number)
	 */
	public void overrideChanged(final Number _override) {
		runOnUiThread(new Runnable() {

			public void run() {
				// set override label'S text
				// TextView t = (TextView) findViewById(R.id.overrideLabel);
				// t.setText("Override " + _override + "%");
				// ((SeekBar)
				// findViewById(R.id.overrideBar)).setProgress(_override.intValue());
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #pathVelocityChanged(float)
	 */
	public void pathVelocityChanged(float _velocityMms) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #chosenRefSysChanged(java.lang.String)
	 */
	public void chosenRefSysChanged(String _refsysName) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener
	 * #chosenToolChanged(java.lang.String)
	 */
	public void chosenToolChanged(String _toolName) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			Fragment fragment;
			Bundle args = new Bundle();

			switch (position) {
			case 0:
				fragment = new ConnectFragment();

				args.putSerializable("connector", MainActivity.this);
				fragment.setArguments(args);
				break;
			case 1:
				fragment = new OverviewFragment();
				break;
			case 2:
				fragment = new ProjectListFragment();
				break;
			case 3:
				KvtProject p = KvtProjectAdministrator.getProject(mSelectedProject);
				if (p != null) {
					fragment = new ProjectDetailFragment();
					args = new Bundle();
					args.putSerializable("project", p);
					fragment.setArguments(args);
				} else
					fragment = new Fragment();// show empty screen
				break;

			default:
				fragment = new Fragment();
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// if (RobotControlProxy.isConnected())
			return 4; // if projects AND programs are available
			// return 3; // if not connected
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section_connect);
			case 1:
				return getString(R.string.title_section_overview);
			case 2:
				return getString(R.string.title_section_programs);
			default:
				return "section_" + position;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.teachdroid.app.ConnectCallback#connect()
	 */
	public void connect() {
		mSelectedProject = null;
		checkWifiAndConnect();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.teachdroid.app.IConnectCallback#disconnect()
	 */
	public void disconnect() {
		mSelectedProject = null;
		disconnectFromPLC();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.fragments.projectview.ProjectListFragment.Callbacks
	 * #onItemSelected(java.lang.String)
	 */
	public void onItemSelected(String _id) {
		mSelectedProject = _id;
		KvtProject p = KvtProjectAdministrator.getProject(mSelectedProject);
		if (p != null) {
			Fragment f = mSectionsPagerAdapter.getItem(3);
			if (f instanceof ProjectDetailFragment) {
				((ProjectDetailFragment) f).setProject(p);
			}
			mViewPager.setCurrentItem(3);
		}
	}
}
