package com.keba.teachdroid.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.teachdroid.app.fragments.Touchable;
import com.keba.teachdroid.data.RobotControlProxy;

public class MainActivity extends BaseActivity /*
												 * implements
												 * InitializationListener ,
												 * IConnectCallback
												 */{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

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

		// findViewById(R.id.fragment_prog_code_main).setOnClickListener(new
		// View.OnClickListener() {
		// // findViewById(R.id.programNameLabel).setOnClickListener(new
		// // View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// // onShowProjects(v);
		// ProgramCodeFragmentMain f = (ProgramCodeFragmentMain)
		// getSupportFragmentManager().findFragmentById(R.id.fragment_prog_code_main);
		// f.handleTouch(v);
		//
		// }
		// });
		Touchable f = null;
		f = (Touchable) getSupportFragmentManager().findFragmentById(R.id.fragment_prog_code_main);
		findViewById(R.id.fragment_prog_code_main).setOnTouchListener(new TouchHandler(f));

		// @ltz: replaced by onTouchListener
		// findViewById(R.id.fragment_robot_main).setOnClickListener(new
		// View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// onShowPositions(v);
		// // KvtDriveStateMonitor.toggleDrivesPower();
		// }
		// });

		f = (Touchable) getSupportFragmentManager().findFragmentById(R.id.fragment_robot_main);
		findViewById(R.id.fragment_robot_main).setOnTouchListener(new TouchHandler(f));

		findViewById(R.id.fragment_load_info_main).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onShowInfos();
			}
		});
	}

	public void onShowProjects() {
		// KvtProject global = KvtProjectAdministrator.getGlobalProject();

		// if (global != null && global.getProjectState() ==
		// KvtProject.SUCCESSFULLY_LOADED) {
		if (RobotControlProxy.isGlobalLoaded()) {
			Intent projectsActivity = new Intent(this, ProjectActivity.class);
			startActivity(projectsActivity);
		} else {
			KvtProjectAdministrator.reloadProjectList();
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(MainActivity.this, "Projects still loading", Toast.LENGTH_SHORT).show();
				}
			});
		}

	}

	public void onShowPositions() {
		Intent robotActivity = new Intent(this, RobotActivity.class);
		startActivity(robotActivity);
	}

	public void onShowInfos() {
		Intent infoActivity = new Intent(this, InfoActivity.class);
		startActivity(infoActivity);
	}

	private final static class TouchHandler implements OnTouchListener {

		private Touchable	mTarget;

		private TouchHandler(Touchable _target) {
			mTarget = _target;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
		 * android.view.MotionEvent)
		 */
		@Override
		public boolean onTouch(View _v, MotionEvent _event) {

			mTarget.handleTouch(_event);

			return false;
		}

	}
}
