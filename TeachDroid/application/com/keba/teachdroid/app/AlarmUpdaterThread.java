package com.keba.teachdroid.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater;
import com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.KvtAlarmUpdaterListener;
import com.keba.kemro.kvs.teach.controller.KvtTraceUpdater;
import com.keba.kemro.kvs.teach.controller.KvtTraceUpdater.KvtTraceUpdateListener;
import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor;
import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener;
import com.keba.kemro.serviceclient.alarm.KMessage;

public class AlarmUpdaterThread extends Thread implements KvtAlarmUpdaterListener, KvtTraceUpdateListener {

	public interface AlarmUpdaterListener {
		public void queueChanged();

		public void historyChanged();

		public void traceChanged(String _line);
	}

	/**
	 * This list is populated as messages are reported. all past messages are
	 * stored there, they are never deleted
	 */
	private static Hashtable<String, List<KMessage>> mMessageHistory = new Hashtable<String, List<KMessage>>();
	private Object mMsgHistoryLock = new Object();
	private Object mMsgBufferLock = new Object();
	/**
	 * new messages are stored here, and are removed from the list when they are
	 * confirmed via a button
	 */
	private static Hashtable<String, List<KMessage>> mMessageQueue = new Hashtable<String, List<KMessage>>();
	private KMessage mLastMessage = null;

	private static List<String> tracedLines = null;
	private static List<AlarmUpdaterListener> mListeners;

	private Context context;
	private int notificationCount = 0;

	public AlarmUpdaterThread(Context _context) {
		super("Notifications");
		context = _context;
		KvtAlarmUpdater.addListener(this);
		KvtTraceUpdater.addListener(this);
		tracedLines = new Vector<String>();
		mListeners = new Vector<AlarmUpdaterListener>();
	}

	public static List<Message> getMessageQueue() {
		List<Message> list = new LinkedList<Message>();
		String filter = "RC";
		Set<Entry<String, List<KMessage>>> s = mMessageQueue.entrySet();
		for (Entry<String, List<KMessage>> e : s) {
			if (e.getKey().contains(filter)) {
				for (KMessage msg : e.getValue()) {
					((LinkedList<Message>) list).addFirst(new Message(msg));
				}
			}
		}
		return list;
	}
	
	public static List<KMessage> getKMessageQueue() {
		List<KMessage> list = new LinkedList<KMessage>();
		String filter = "RC";
		Set<Entry<String, List<KMessage>>> s = mMessageQueue.entrySet();
		for (Entry<String, List<KMessage>> e : s) {
			if (e.getKey().contains(filter)) {
				for (KMessage msg : e.getValue()) {
					((LinkedList<KMessage>) list).addFirst(msg);
				}
			}
		}
		return list;
	}

	public static List<Message> getMessageHistory() {
		List<Message> list = new LinkedList<Message>();
		Set<Entry<String, List<KMessage>>> s = mMessageHistory.entrySet();
		for (Entry<String, List<KMessage>> e : s) {
			for (KMessage msg : e.getValue()) {
				((LinkedList<Message>) list).addFirst(new Message(msg));
			}
		}
		return list;
	}

	public static List<String> getTracedLines() {
		return tracedLines;
	}

	public void messageUpdated(int lastMessageType, Object lastMessage) {
		// empty interface implementation
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
				for (AlarmUpdaterListener l : mListeners) {
					l.historyChanged();
				}
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
				for (AlarmUpdaterListener l : mListeners) {
					l.queueChanged();
				}
			}
		}

		// set the last message and send notification
		// if message concerns RC
		if (_bufferName.contains("RC")) {
			mLastMessage = _msg;
			sendNotification(_msg);
		} else
			mLastMessage = null;
	}

	public void messageRemoved(String _bufferName, KMessage _msg) {
		synchronized (mMsgBufferLock) {
			// remove message from queue
			List<KMessage> q = mMessageQueue.get(_bufferName);
			if (q != null){
				q.remove(_msg);
				for (AlarmUpdaterListener l : mListeners) {
					l.queueChanged();
				}
			}

			// update last message
			if (q != null && !q.isEmpty() && _bufferName.contains("RC")) {
				mLastMessage = q.get(0);
			} else
				mLastMessage = null;
		}
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
				for (AlarmUpdaterListener l : mListeners) {
					l.queueChanged();
				}
			}

		}
	}

	@Override
	public void lineReceived(String _line) {
		if (tracedLines != null) {
			tracedLines.add(_line);
			for (AlarmUpdaterListener l : mListeners) {
				l.traceChanged(_line);
			}
		}
	}

	private void sendNotification(KMessage _msg) {
		Message msg = new Message(_msg);
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setPriority(Notification.PRIORITY_HIGH).setSmallIcon(msg.getImageID()).setContentTitle(df.format(msg.getDate()).toString()).setContentText(_msg.getMessageText())
				.setSound(soundUri);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, InfoActivity.class);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(InfoActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = mBuilder.build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.ledARGB = 0xff0000ff; // color, in this case, red
		notification.ledOnMS = 1000; // light on in milliseconds
		notification.ledOffMS = 4000; // light off in milliseconds
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.number = notificationCount;

		mNotificationManager.notify(msg.getID(), notification);
		notificationCount++;
	}

	public static synchronized void addListener(AlarmUpdaterListener _listener) {
		if (mListeners == null)
			mListeners = new Vector<AlarmUpdaterListener>();

		if (!mListeners.contains(_listener))
			mListeners.add(_listener);
	}

	public static boolean removeListener(AlarmUpdaterListener _listener) {
		if (mListeners != null)
			return mListeners.remove(_listener);
		return false;
	}
}
