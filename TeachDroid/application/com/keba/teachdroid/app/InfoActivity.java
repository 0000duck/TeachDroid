package com.keba.teachdroid.app;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.keba.kemro.kvs.teach.controller.KvtTraceUpdater.KvtTraceUpdateListener;
import com.keba.teachdroid.app.fragments.AlarmInfoFragment;
import com.keba.teachdroid.app.fragments.HistoryInfoFragment;
import com.keba.teachdroid.app.fragments.TraceInfoFragment;

public class InfoActivity extends BaseActivity implements Serializable, Observer, KvtTraceUpdateListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 289874216604394469L;
	private transient ViewPager mViewPager;
	private transient SectionsPagerAdapter mSectionsPagerAdapter;

	private List<String> mTrace = new Vector<String>();
	// private transient AlarmInfoFragment.MessageUpdateListener
	// mInfoUpdateListener;

	private transient TraceInfoFragment tif;
	private transient AlarmInfoFragment aif;
	private transient HistoryInfoFragment hif;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_info);

		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mNavigationStrings = getResources().getStringArray(R.array.navigation_array);

		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mNavigationStrings));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new MyActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		);
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// mInfoUpdateListener = new MessageUpdateListener();
		// mInfoUpdateListener.addObserver(this);
		//
		// KvtAlarmUpdater.addListener(mInfoUpdateListener);
		//
		// KvtTraceUpdater.setLoadMode(KvtTraceUpdater.MODE_FROMNOW);
		// KvtTraceUpdater.addListener(this);

		// mTrace = RobotControlProxy.getTraceBacklog();

		tif = new TraceInfoFragment();
		aif = new AlarmInfoFragment();
		hif = new HistoryInfoFragment();

	}

	@Override
	public boolean onKeyDown(int _code, KeyEvent _evt) {
		if (mViewPager.getCurrentItem() != 0 && KeyEvent.KEYCODE_BACK == _code) {
			mViewPager.setCurrentItem(0, true);
			return true;
		}
		return super.onKeyDown(_code, _evt);
	}

	@Override
	public void update(Observable _observable, Object _data) {
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.logo).setContentTitle("My notification").setContentText("Hello World!");
//		// Creates an explicit intent for an Activity in your app
//		Intent resultIntent = new Intent(this, InfoActivity.class);
//
//		// The stack builder object will contain an artificial back stack for
//		// the
//		// started Activity.
//		// This ensures that navigating backward from the Activity leads out of
//		// your application to the Home screen.
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//		// Adds the back stack for the Intent (but not the Intent itself)
//		stackBuilder.addParentStack(InfoActivity.class);
//		// Adds the Intent that starts the Activity to the top of the stack
//		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//		mBuilder.setContentIntent(resultPendingIntent);
//		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//		Notification notification = mBuilder.build();
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//		mNotificationManager.notify(1, notification);

		// tif.setAdapter(mTrace);
		// aif.setAdapter(getMessageQueue());
		// hif.setAdapter(getMessageHistory());
		//
		// tif.update();
		// aif.update();
		// hif.update();
	}

	public List<String> getTraceList() {
		return mTrace;
	}

	// public synchronized List<String> getMessageHistory() {
	// return mInfoUpdateListener.getMessageHistory();
	// }
	//
	// public synchronized List<String> getMessageQueue() {
	// return mInfoUpdateListener.getMessageQueue();
	// }

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private Fragment[] mFragments;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);

		}

		@Override
		public Fragment getItem(int position) {

			if (position > getCount() - 1)
				return null;

			if (mFragments == null) {
				Bundle args = new Bundle();
				mFragments = new Fragment[] { aif, hif, tif };
				args.putSerializable("connector", InfoActivity.this);
				mFragments[0].setArguments(args);
				mFragments[1].setArguments(args);
				mFragments[2].setArguments(args);
			}

			return mFragments[position];
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section_alarms);
			case 1:
				return getString(R.string.title_section_history);
			case 2:
				return getString(R.string.title_section_trace);
			default:
				return "NOT_DEFINED_" + position;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.controller.KvtTraceUpdater.KvtTraceUpdateListener
	 * #lineReceived(java.lang.String)
	 */
	@Override
	public synchronized void lineReceived(String _line) {
		mTrace.add(_line);
		System.out.println(_line);
	}

}
