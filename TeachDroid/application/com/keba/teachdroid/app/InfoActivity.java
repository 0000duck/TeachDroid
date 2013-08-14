package com.keba.teachdroid.app;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater;
import com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.KvtAlarmUpdaterListener;
import com.keba.kemro.serviceclient.alarm.KMessage;
import com.keba.teachdroid.app.fragments.AlarmInfoFragment;
import com.keba.teachdroid.app.BaseActivity;
import com.keba.teachdroid.app.fragments.HistoryInfoFragment;
import com.keba.teachdroid.app.fragments.TraceInfoFragment;
import com.keba.teachdroid.data.RobotControlProxy;

public class InfoActivity extends BaseActivity implements Serializable, Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 289874216604394469L;
	private transient ViewPager mViewPager;
	private transient SectionsPagerAdapter mSectionsPagerAdapter;

	private List<String> mTrace;
	private transient InfoUpdateListener mInfoUpdateListener;

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
		mViewPager.setCurrentItem(1);

		mInfoUpdateListener = new InfoUpdateListener();
		mInfoUpdateListener.addObserver(this);

		KvtAlarmUpdater.addListener(mInfoUpdateListener);

		mTrace = RobotControlProxy.getTraceBacklog();

		tif = new TraceInfoFragment();
		aif = new AlarmInfoFragment();
		hif = new HistoryInfoFragment();

	}

	public void update(Observable _observable, Object _data) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentTitle("My notification").setContentText("Hello World!");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, InfoActivity.class);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(InfoActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = mBuilder.build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		mNotificationManager.notify(1, notification);
		tif.setAdapter(mTrace);
		aif.setAdapter(getMessageQueue());
		hif.setAdapter(getMessageHistory());
	}

	public List<String> getTraceList() {
		return mTrace;
	}

	public List<String> getMessageHistory() {
		return mInfoUpdateListener.getMessageHistory();
	}

	public List<String> getMessageQueue() {
		return mInfoUpdateListener.getMessageQueue();
	}

	private static class InfoUpdateListener extends Observable implements KvtAlarmUpdaterListener {
		/**
		 * This list is populated as messages are reported. all past messages
		 * are stored there, they are never deleted
		 */
		private Hashtable<String, List<KMessage>> mMessageHistory = new Hashtable<String, List<KMessage>>();
		private Object mMsgHistoryLock = new Object();
		private Object mMsgBufferLock = new Object();
		/**
		 * new messages are stored here, and are removed from the list when they
		 * are confirmed via a button
		 */
		private Hashtable<String, List<KMessage>> mMessageQueue = new Hashtable<String, List<KMessage>>();
		/**
		 * three latest messages are stored here, and are removed from the list
		 * when they are confirmed via a button
		 */
		private Hashtable<String, List<KMessage>> mLatestMessages = new Hashtable<String, List<KMessage>>();

		public List<String> getMessageQueue() {
			List<String> list = new Vector<String>();
			Set<Entry<String, List<KMessage>>> s = mMessageQueue.entrySet();
			for (Entry<String, List<KMessage>> e : s) {
				for (KMessage msg : e.getValue()) {
					list.add(msg.toString());
				}
			}
			return list;
		}

		public List<String> getMessageHistory() {
			List<String> list = new Vector<String>();
			Set<Entry<String, List<KMessage>>> s = mMessageHistory.entrySet();
			for (Entry<String, List<KMessage>> e : s) {
				for (KMessage msg : e.getValue()) {
					list.add(msg.toString());
				}
			}
			return list;
		}

		public void messageUpdated(int lastMessageType, Object lastMessage) {
			// do nothing
		}

		public void messageAdded(String _bufferName, KMessage _msg) {

			// add to unmodifiable history queue
			synchronized (mMsgHistoryLock) {
				List<KMessage> h = mMessageHistory.get(_bufferName);
				if (h == null) {
					mMessageHistory.put(_bufferName, new Vector<KMessage>());
					h = mMessageHistory.get(_bufferName);
				}
				if (h != null) {
					h.add(_msg);
				}

			}

			// add to temporary message buffer
			synchronized (mMsgBufferLock) {
				List<KMessage> q = mMessageQueue.get(_bufferName);
				if (q == null) {
					mMessageQueue.put(_bufferName, new Vector<KMessage>());
					q = mMessageQueue.get(_bufferName);
				}
				if (q != null) {
					q.add(_msg);
				}
			}

			// set the last message
			// if (_bufferName.contains("RC"))
			// mLastMessage = _msg;
			// else
			// mLastMessage = null;
			setChanged();
			notifyObservers();
		}

		public void messageRemoved(String _bufferName, KMessage _msg) {
			synchronized (mMsgBufferLock) {
				// remove message from queue
				List<KMessage> q = mMessageQueue.get(_bufferName);
				if (q != null)
					q.remove(_msg);

				// update last message
				// if (q != null && !q.isEmpty() && _bufferName.contains("RC"))
				// {
				// mLastMessage = q.get(0);
				// } else
				// mLastMessage = null;
			}
			setChanged();
			notifyObservers();
		}

		public void messageChanged(String _bufferName, KMessage _msg) {

			messageRemoved(_bufferName, _msg); // first remove the message...
			synchronized (mMsgBufferLock) {
				List<KMessage> q = mMessageQueue.get(_bufferName);
				if (q == null) {
					mMessageQueue.put(_bufferName, new Vector<KMessage>());
					q = mMessageQueue.get(_bufferName);
				}
				if (q != null) {
					q.add(_msg);
				}

			}
			setChanged();
			notifyObservers();
		}

	}

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
				mFragments = new Fragment[] { tif, aif, hif };
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
				return getString(R.string.title_section_trace);
			case 1:
				return getString(R.string.title_section_alarms);
			case 2:
				return getString(R.string.title_section_history);
			default:
				return "NOT_DEFINED_" + position;
			}
		}
	}

}
