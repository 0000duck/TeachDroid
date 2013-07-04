package com.keba.kemro.kvs.teach.controller;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import android.util.Log;

import com.keba.kemro.kvs.teach.data.rc.KvtMessageFilterAdministrator;
import com.keba.kemro.kvs.teach.data.rc.KvtMessageFilterListener;
import com.keba.kemro.serviceclient.alarm.KAlarmConnectionListener;
import com.keba.kemro.serviceclient.alarm.KMessage;
import com.keba.kemro.serviceclient.alarm.KMessageBuffer;
import com.keba.kemro.serviceclient.alarm.KMessageChangeListener;
import com.keba.kemro.serviceclient.alarm.KMessageService;

public class KvtAlarmUpdater implements KAlarmConnectionListener, KMessageChangeListener,/*
																						 * UserChangedListener
																						 * ,
																						 * LanguageChangedListener
																						 * ,
																						 */KvtMessageFilterListener {
	private static Object m_actualMessage;
	private static int m_actualMessageType;
	private final Vector							m_messages			= new Vector();
	private int[] m_alarmTypes;
	private int MAX_MESSAGE_CLASSES = 32;
	private boolean					LAST_MESSAGE_FIRST	= true;				// Config.getBooleanProperty("MsgDisplayNewest",
																			// true);
	
	protected static final int ALARM = 1;
	protected static final int WARNING = 2;
	protected static final int INFO = 3;
	protected static final int NONE = 0;
	private static KvtAlarmUpdater updater;
	private static Vector<KvtAlarmUpdaterListener>	m_listener			= new Vector<KvtAlarmUpdaterListener>(5);
	
	public static interface KvtAlarmUpdaterListener {
		public void messageUpdated(int lastMessageType, Object lastMessage);

		public void messageAdded(String _bufferName, KMessage _msg);

		public void messageRemoved(String _bufferName, KMessage _msg);

		public void messageChanged(String _bufferName, KMessage _msg);
	}
	
	public static void addListener(KvtAlarmUpdaterListener listener) {
		if (updater == null){
			updater = new KvtAlarmUpdater();
		}
		if (!m_listener.contains(listener)){
    		m_listener.addElement(listener);
		}
		listener.messageUpdated(m_actualMessageType, m_actualMessage);
	}

	private KvtAlarmUpdater() {
		// UserManager.getSharedInstance().addUserChangedListener(this);
		// LanguageManager.sharedInstance().addLanguageChangedListener(this);
		KvtMessageFilterAdministrator.addCoordinateListener(this);
		KMessageService.addConnectionListener(this);
	}

	private void updateLastError() {
		m_actualMessage = null;
		int actualMessageId = -1;
		long actualMessageDate = 0;
		for (int i = m_messages.size() - 1; 0 <= i; i--) {
			KMessage msg = (KMessage) m_messages.elementAt(i);
			int id = getTypeOf(msg.m_ClassId);
			if (id >= 0) {
				long time = msg.getTimeMSec();
				if (((m_actualMessage == null) || (id < actualMessageId)
						|| ((LAST_MESSAGE_FIRST) && (id == actualMessageId) && (time > actualMessageDate)) || ((!LAST_MESSAGE_FIRST)
						&& (id == actualMessageId) && (time < actualMessageDate)))
						&& isMessageValid(msg)) {
					m_actualMessage = msg;
					actualMessageId = id;
					actualMessageDate = time;
				}
			}
		}
		m_actualMessageType = NONE;
		if ((actualMessageId >= 0) && (actualMessageId <= 100)) {
			m_actualMessageType = ALARM;
		} else if ((actualMessageId >= 100) && (actualMessageId <= 200)) {
			m_actualMessageType = WARNING;
		} else if ((actualMessageId >= 200) && (actualMessageId <= 300)) {
			m_actualMessageType = INFO;
		}
		for (int i = 0; i < m_listener.size(); i++) {
			((KvtAlarmUpdaterListener) m_listener.elementAt(i)).messageUpdated(m_actualMessageType, m_actualMessage);
		}
	}

	public static Object getLastMessage() {
		return m_actualMessage;
	}

	/**
	 * @see com.keba.kemro.serviceclient.alarm.KAlarmConnectionListener#connected()
	 */
	public void connected() {
		Vector buffers = KMessageService.getCreatedBuffers();
		if (buffers != null) {
			for (int i = 0; i < buffers.size(); i++) {
				KMessageBuffer buffer = (KMessageBuffer) buffers.elementAt(i);
				if (buffer.isReportBuffer()) {
					buffer.addChangeListener(this);
				}
			}
		}
		updateLastError();
	}

	/**
	 * @see com.keba.kemro.serviceclient.alarm.KMessageChangeListener#messagesChanged(java.lang.String,
	 *      java.util.Vector, java.util.Vector, java.util.Vector)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void messagesChanged(String bufferName, Vector addedMessages, Vector removedMessages, Vector changedMessages) {
		Enumeration e = addedMessages.elements();
		while (e.hasMoreElements()) {
			KMessage msg = (KMessage) e.nextElement();
			if (!m_messages.contains(msg)) {
				m_messages.addElement(msg);
			}

			// notify listeners
			for (KvtAlarmUpdaterListener l : m_listener)
				l.messageAdded(bufferName, msg);
		}
		e = changedMessages.elements();
		while (e.hasMoreElements()) {
			KMessage msg = (KMessage) e.nextElement();
			if (m_messages.contains(msg)) {
				m_messages.removeElement(msg);
				KMessageService.clearMessageText(msg);
				m_messages.insertElementAt(msg, 0);
			}

			// notify listeners
			for (KvtAlarmUpdaterListener l : m_listener)
				l.messageChanged(bufferName, msg);
		}
		e = removedMessages.elements();
		while (e.hasMoreElements()) {
			KMessage msg = (KMessage) e.nextElement();
			m_messages.removeElement(msg);
			// notify listeners
			for (KvtAlarmUpdaterListener l : m_listener)
				l.messageRemoved(bufferName, msg);
		}
		updateLastError();
	}

	// public void languageChanged(LanguageChangedEvent event) {
	// KMessageService.languageChanged();
	// updateLastError();
	// }

	// public void userChanged(UserChangedEvent e) {
	// updateLastError();
	// }

	public void messageFilterChanged() {
		// KvUtilities.invokeLater(new Runnable() {
		// public void run() {
		// updateLastError();
		// }
		// });
		new Thread(new Runnable() {
			public void run() {
				updateLastError();
			}
		});
	}

	private int getClassString(int cId) { // Weiss der Geier warum, kopiert
											// aus dem AlarmController !
		long classId = cId & 0xFFFFFFFFL;
		if (classId < -1) {
			classId = -2L * classId + 1L;
		}

		return (int) (Math.round(Math.log(classId) / Math.log(2)) + 1);
	}

	private void readInfo(String propertyName, String defaults, int value) {
		String errors = defaults;// Config.getStringProperty(propertyName,
									// defaults);
		StringTokenizer tokens = new StringTokenizer(errors, "|");
		int index = 1;
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			try {
				int i = Integer.parseInt(token);
				if ((i <= MAX_MESSAGE_CLASSES) && (m_alarmTypes[i] == -1)) {
					m_alarmTypes[i] = value + index;
				}
			} catch (NumberFormatException nfe) {
				// KvtLogger.error(this, "isError: error in " + propertyName +
				// " definition");
				Log.e("KvtAlarmUpdater", "isError: error in " + propertyName + " definition");
			}
			index++;
		}
	}

	private int getTypeOf(int classId) {
		int id = getClassString(classId);

		if (m_alarmTypes == null) {
			readMessageTypes();
		}
		if ((id >= 0) && (id < m_alarmTypes.length)) {
			return m_alarmTypes[id];
		}
		return -1;
	}

	private void readMessageTypes() {
		m_alarmTypes = new int[MAX_MESSAGE_CLASSES + 1];
		for (int i = 0; i < m_alarmTypes.length; i++) {
			m_alarmTypes[i] = -1;
		}
		readInfo("ErrorMsgClasses", "1|2|3|6", 0);
		readInfo("WarningMsgClasses", "", 100);
		readInfo("InfoMsgClasses", "", 200);
	}

	/**
	 * @see com.keba.kemro.serviceclient.alarm.KMessageChangeListener#allMessagesRemoved(java.lang.String)
	 */
	public void allMessagesRemoved(String bufferName) {
		m_messages.removeAllElements();
		updateLastError();
	}

	private boolean isMessageValid(KMessage msg) {
		int msgClass = (int) (Math.round(Math.log(msg.m_ClassId) / Math.log(2)) + 1);
		int msgCompNr = msg.m_CompNr;
		int msgNr = msg.m_Nr;
		int msgInstNr = msg.m_InstNr;
		return KvtMessageFilterAdministrator.isMessageValid(msgClass, msgCompNr, msgNr, msgInstNr);
	}

	/**
	 * @see com.keba.kemro.serviceclient.alarm.KAlarmConnectionListener#disconnected()
	 */
	public void disconnected() {
		// interface method not needed
		m_messages.setSize(0);
		m_actualMessage = null;
		updateLastError();
	}

}
