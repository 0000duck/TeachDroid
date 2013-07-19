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
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.teachdroid.app.fragments.ConnectFragment;
import com.keba.teachdroid.app.fragments.OverviewFragment;
import com.keba.teachdroid.app.fragments.projectview.ProjectDetailFragment;
import com.keba.teachdroid.app.fragments.projectview.ProjectListActivity;
import com.keba.teachdroid.app.fragments.projectview.ProjectListFragment;
import com.keba.teachdroid.app.fragments.projectview.ProjectListFragment.SelectionCallback;
import com.keba.teachdroid.data.InitializationTask;
import com.keba.teachdroid.data.InitializationTask.InitializationListener;
import com.keba.teachdroid.data.RobotControlProxy;
import com.keba.teachdroid.util.PreferenceManager;

public class MainActivity extends FragmentActivity implements InitializationListener, SelectionCallback, IConnectCallback {
String xyz = "geht das?";
	/**
	 * 
	 */
	private static final long			serialVersionUID		= 1L;
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
	private KvtProject					mSelectedProject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	/**
	 * @param _host
	 * 
	 */
	private void checkWifiAndConnect(final String _host) {
		if (!RobotControlProxy.isConnected()) {
			final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			if (!wifi.isWifiEnabled()) {

				AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

				dlgAlert.setMessage("Click OK to enable WiFi, Cancel to dismiss!");

				dlgAlert.setTitle("Info");
				dlgAlert.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface _dialog, int _which) {
						wifi.setWifiEnabled(true);
						connectToPlc(_host);
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

			} else {
				connectToPlc(_host);
			}
		}
	}

	/**
	 * @param _host
	 * 
	 */
	private void connectToPlc(String _host) {
		// always read host from preferences, just in case someone has modified
		// it in the meantime
		// String host = PreferenceManager.getInstance().getHostname();
		mStartTime = System.currentTimeMillis();
		InitializationTask itask = new InitializationTask(this);
		itask.execute(_host);

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
				String host = PreferenceManager.getInstance().getHostname();
				checkWifiAndConnect(host);
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
		} else {
			String host = PreferenceManager.getInstance().getHostname();
			checkWifiAndConnect(host);
		}

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

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private Fragment[]	mFragments;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);

		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			// Fragment fragment;
			// Bundle args = new Bundle();
			//
			// switch (position) {
			// case 0:
			// fragment = new ConnectFragment();
			//
			// args.putSerializable("connector", MainActivity.this);
			// fragment.setArguments(args);
			// break;
			// case 1:
			// fragment = new OverviewFragment();
			// break;
			// case 2:
			// fragment = new ProjectListFragment();
			// break;
			// case 3:
			// fragment = new ProjectDetailFragment();
			// KvtProject p =
			// KvtProjectAdministrator.getProject(mSelectedProject);
			// if (p != null) {
			//
			// args = new Bundle();
			// args.putSerializable("project", p);
			// fragment.setArguments(args);
			// }
			// break;
			//
			// default:
			// fragment = new Fragment();
			// break;
			// }
			// return fragment;

			if (position > getCount() - 1)
				return null;

			if (mFragments == null) {
				Bundle args = new Bundle();

				mFragments = new Fragment[] { new ConnectFragment(), new OverviewFragment(), new ProjectListFragment(),
						new ProjectDetailFragment() };

				args.putSerializable("connector", MainActivity.this);
				mFragments[0].setArguments(args);
			}

			// if (position == 3 && mSelectedProject != null) {
			// KvtProject p =
			// KvtProjectAdministrator.getProject(mSelectedProject);
			// if (p != null) {
			//
			// Bundle args = new Bundle();
			// args.putSerializable("project", p);
			// mFragments[3].setArguments(args);
			// }
			// }

			return mFragments[position];
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
				return getString(R.string.title_section_projects);
			case 3:
				return getString(R.string.title_section_programs);
			default:
				return "NOT_DEFINED_" + position;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.teachdroid.app.ConnectCallback#connect()
	 */
	public void connect(String _host) {
		mSelectedProject = null;
		checkWifiAndConnect(_host);
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
	public void onItemSelected(KvtProject _entry) {
		mSelectedProject = _entry;

		if (mSelectedProject != null) {
			Fragment f = mSectionsPagerAdapter.getItem(3);
			if (f instanceof ProjectDetailFragment) {
				((ProjectDetailFragment) f).setProject(mSelectedProject);
			}
			mViewPager.setCurrentItem(3);
		}
	}
}
