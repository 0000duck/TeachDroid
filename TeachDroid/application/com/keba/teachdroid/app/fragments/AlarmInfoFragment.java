package com.keba.teachdroid.app.fragments;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater;
import com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.KvtAlarmUpdaterListener;
import com.keba.kemro.kvs.teach.util.Log;
import com.keba.kemro.serviceclient.alarm.KMessage;
import com.keba.teachdroid.app.Message;
import com.keba.teachdroid.app.MessageTypes;
import com.keba.teachdroid.app.R;

public class AlarmInfoFragment extends Fragment implements Serializable, Observer, KvtAlarmUpdaterListener {

	/**
	 * @author ltz
	 * 
	 */
	private class MessageHolder {
		ImageView	mIcon;
		TextView	mDateView;
		TextView	mMessageText;
	}

	/**
	 * @author ltz
	 * 
	 */
	private class MessageArrayAdapter extends ArrayAdapter<Message> {

		private int				mLayoutResourceId;
		private Context			mContext;
		private List<Message>	mData;

		/**
		 * @param _activity
		 * @param _layoutId
		 */
		public MessageArrayAdapter(Context _context, int _layoutId, List<Message> _objects) {
			super(_context, _layoutId, _objects);
			mLayoutResourceId = _layoutId;
			mContext = _context;
			mData = _objects;
		}

		@Override
		public View getView(int _position, View _convertView, ViewGroup _parent) {
			View row = _convertView;
			MessageHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
				row = inflater.inflate(mLayoutResourceId, _parent, false);

				holder = new MessageHolder();
				holder.mIcon = (ImageView) row.findViewById(R.id.alarmIcon);
				holder.mMessageText = (TextView) row.findViewById(R.id.alarmText);
				holder.mDateView = (TextView) row.findViewById(R.id.alarmDate);
				row.setTag(holder);
			} else {
				holder = (MessageHolder) row.getTag();
			}

			if (_position >= 0 && _position < mData.size()) {
				Message line = mData.get(_position);
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());

				holder.mDateView.setText(df.format(line.getDate()));
				holder.mMessageText.setText(line.toString());
				holder.mIcon.setImageResource(line.getImageID());
			}
			return row;
		}
	}

	/**
	 * @deprecated Functionality has been migrated to the enclosing type,
	 *             {@link AlarmInfoFragment}
	 * @author ltz
	 * 
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private class MessageUpdateListener extends Observable implements Serializable, KvtAlarmUpdaterListener {

		private static final long					serialVersionUID	= 7810826760470192309L;
		/**
		 * This list is populated as messages are reported. all past messages
		 * are stored there, they are never deleted
		 */
		private Hashtable<String, List<KMessage>>	mMessageHistory		= new Hashtable<String, List<KMessage>>();
		private Object								mMsgHistoryLock		= new Object();
		private Object								mMsgBufferLock		= new Object();
		/**
		 * new messages are stored here, and are removed from the list when they
		 * are confirmed via a button
		 */
		private Hashtable<String, List<KMessage>>	mMessageQueue		= new Hashtable<String, List<KMessage>>();

		public MessageUpdateListener(Observer _obs) {
			addObserver(_obs);
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

			if (!appliesFilter(_bufferName)) {
				setChanged();
				notifyObservers(_msg);
			}

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

			List<Message> toRemove = new ArrayList<Message>();

			for (Message m : mMessages) {
				if (m.getID() == _msg.hashCode()) {
					// mMessages.remove(m); //will provoke a
					// ConcurrentModificationException
					toRemove.add(m);
				}
			}

			mMessages.removeAll(toRemove);

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
			if (!appliesFilter(_bufferName)) {
				setChanged();
				notifyObservers(_msg);
			}
		}

	}

	private static final long					serialVersionUID	= -1436151971908046236L;
	private ListView							mList;
	// private MessageUpdateListener mMsgUpdaterListener;
	private LinkedList<Message>					mMessages			= new LinkedList<Message>();
	private MessageArrayAdapter					mAdapter;
	private HashMap<Long, Long>					mTypeMap;

	private Hashtable<String, List<KMessage>>	mMessageHistory		= new Hashtable<String, List<KMessage>>();
	private Object								mMsgHistoryLock		= new Object();
	private Object								mMsgBufferLock		= new Object();
	/**
	 * new messages are stored here, and are removed from the list when they are
	 * confirmed via a button
	 */
	private Hashtable<String, List<KMessage>>	mMessageQueue		= new Hashtable<String, List<KMessage>>();

	public AlarmInfoFragment() {
		initMsgTypeMap();
		// mMsgUpdaterListener = new MessageUpdateListener(this);
		KvtAlarmUpdater.addListener(this);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mRootView = inflater.inflate(R.layout.fragment_alarm_info, container, false);
		mList = (ListView) mRootView.findViewById(R.id.alarmList);

		mAdapter = new MessageArrayAdapter(getActivity(), R.layout.fragment_alarm_row_layout, mMessages);
		// mAdapter = new MessageAdapter<Message>(getActivity(),
		// R.layout.fragment_alarm_row_layout, mMessages);

		mList.setAdapter(mAdapter);
		mList.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> _parent, View _view, final int _position, long _id) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

				// set title
				alertDialogBuilder.setTitle(getString(R.string.dialog_confirm_message_title));

				// set dialog message
				alertDialogBuilder.setMessage(getString(R.string.dialog_confirm_message_text)).setCancelable(false)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Message m = mMessages.get(_position);

								boolean success = false;
								if (m != null) {
									try {
										success = new AsyncTask<Message, Integer, Boolean>() {

											@Override
											protected Boolean doInBackground(Message... _params) {
												KMessage toConfirm = findInQueue(_params[0]);

												return toConfirm != null ? toConfirm.quitMessage() : false;
											}
										}.execute(m).get();
									} catch (InterruptedException e) {
										e.printStackTrace();
									} catch (Exception e) {
										e.printStackTrace();
									}

								}
								Log.i("AlarmInfoFragment", "confirming was " + (success ? "successful" : "unsuccessful"));
							}
						}).setNegativeButton("No", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

				return false;
			}

		});
		return mRootView;
	}

	private KMessage findInQueue(Message _m) {
		int hash = _m.getID();

		for (List<KMessage> curBuffer : /* mMsgUpdaterListener. */mMessageQueue.values()) {
			for (KMessage msg : curBuffer) {
				if (msg.hashCode() == hash)
					return msg;
			}
		}

		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable _observable, Object _data) {

		if (/* _observable == mMsgUpdaterListener && */_data != null && _data instanceof KMessage) {
			String text = _data.toString();
			KMessage km = (KMessage) _data;
			final Message m = new Message(text, getMessageType(km));

			m.setID(km.hashCode());
			m.setDate(new Date(km.getTimeMSec()));
			mMessages.offerFirst(m);
		}
		Handler mainLooper = new Handler(Looper.getMainLooper());
		mainLooper.post(new Runnable() {

			public void run() {
				mAdapter.notifyDataSetChanged();
				mList.invalidate();
			}
		});

	}

	private MessageTypes getMessageType(KMessage _data) {
		long classId = _data.getBuffer().getClassId() & 0xFFFFFFFFL;
		if (classId < -1) {
			classId = -2L * classId + 1L;
		}

		classId = mTypeMap.get(classId);

		// if (classId > 32)
		// return MessageTypes.DEFAULT;

		switch ((int) classId) {
		case 1:
		case 2:
		case 3:
		case 6:
		case 7:
		case 10:
			return MessageTypes.ALARM;
		case 4:
		case 8:
		case 11:
			return MessageTypes.WARNING;

		default:
			return MessageTypes.DEFAULT;
		}
	}

	private void initMsgTypeMap() {
		mTypeMap = new HashMap<Long, Long>();

		for (long i = 1; i <= 32; i++) {
			mTypeMap.put(i, i);
			mTypeMap.put((1L << (i - 1)), i);
		}
	}

	protected boolean appliesFilter(String _input) {
		return _input.equals("Info");
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

		if (!appliesFilter(_bufferName)) {
			update(null, _msg);
		}

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

		List<Message> toRemove = new ArrayList<Message>();

		for (Message m : mMessages) {
			if (m.getID() == _msg.hashCode()) {
				// mMessages.remove(m); //will provoke a
				// ConcurrentModificationException
				toRemove.add(m);
			}
		}

		mMessages.removeAll(toRemove);
		update(null, _msg);

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
		if (!appliesFilter(_bufferName)) {
			update(null, _msg);
		}
	}

}
