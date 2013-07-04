package com.keba.teachdroid.data;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import android.util.Log;

import com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater;
import com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.KvtAlarmUpdaterListener;
import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.KvtMainModeListener;
import com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator.KvtMotionModeListener;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener;
import com.keba.kemro.serviceclient.alarm.KMessage;
import com.keba.kemro.teach.dfl.KTcDfl;

public class RobotControlProxy implements KvtTeachviewConnectionListener, KvtMainModeListener, KvtMotionModeListener,
		KvtProjectAdministratorListener, KvtAlarmUpdaterListener {

	/**
	 * 
	 */
	private static final String					ROBOT_CONTROL_LOGTAG	= "RobotControl";
	private static String						mClientID;
	private static boolean						mConnected;
	private static RobotControlProxy			mLocalInstance;
	/**
	 * reference to the data function layer
	 */
	private KTcDfl								mDfl;
	/**
	 * the current main mode, could be 4 (AE), 1 (A) or 2 (T1)
	 */
	private int									mCurrentMainMode		= -1;
	/**
	 * This list is populated as messages are reported. all past messages are
	 * stored there, they are never deleted
	 */
	private Hashtable<String, List<KMessage>>	mMessageHistory			= new Hashtable<String, List<KMessage>>();
	private Object								mMsgHistoryLock			= new Object();
	private Object								mMsgBufferLock			= new Object();
	/**
	 * new messages are stored here, and are removed from the list when they are
	 * confirmed via a button
	 */
	private Hashtable<String, List<KMessage>>	mMessageQueue			= new Hashtable<String, List<KMessage>>();

	private RobotControlProxy() {
		mClientID = null;
		mConnected = false;

	}

	/**
	 * Initializes all administrators which load information from the PLC
	 */
	public static void startup() {
		mLocalInstance = new RobotControlProxy();
		// add all listeners, consolidate here
		KvtSystemCommunicator.addConnectionListener(mLocalInstance);
		KvtMainModeAdministrator.addListener(mLocalInstance);
		KvtMotionModeAdministrator.addListener(mLocalInstance);
		KvtProjectAdministrator.addProjectListener(mLocalInstance);
		KvtAlarmUpdater.addListener(mLocalInstance);
	}

	/**
	 * returns a list with the names of all available robots as strings
	 * 
	 * @return a String array with all robots' names
	 */
	public static String[] getRobotNames() {
		return new String[] { "ArtarmTX60L", "ArtarmTX40", "Scara RS80" };

	}

	public static boolean isConnected() {
		return mConnected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener#
	 * teachviewConnected()
	 */
	public void teachviewConnected() {
		mDfl = KvtSystemCommunicator.getTcDfl();
		mClientID = KvtSystemCommunicator.getClientID();
		Log.d(ROBOT_CONTROL_LOGTAG, "Teachview Connected, client-ID is " + mClientID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener#
	 * teachviewDisconnected()
	 */
	public void teachviewDisconnected() {
		mDfl = null;
		mClientID = null;
		mMessageQueue.clear();
		mMessageHistory.clear();
		Log.d(ROBOT_CONTROL_LOGTAG, "Teachview Disonnected");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator.
	 * KvtMotionModeListener#motionModeChanged()
	 */
	public void motionModeChanged(int _newProgmode) {
		Log.d(ROBOT_CONTROL_LOGTAG, "Motion mode changed to " + _newProgmode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.KvtMainModeListener
	 * #mainModeChanged(int)
	 */
	public void mainModeChanged(int _newMainMode) {
		if (_newMainMode != mCurrentMainMode) {
			Log.d(ROBOT_CONTROL_LOGTAG, "New Mainmode: " + _newMainMode);
			mCurrentMainMode = _newMainMode;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #projectStateChanged(com.keba.kemro.kvs.teach.data.project.KvtProject)
	 */
	public void projectStateChanged(KvtProject _prj) {
		Log.d(ROBOT_CONTROL_LOGTAG, "Project " + _prj.getName() + "'s state is " + _prj.getProjectStateString());
		int prgCount = _prj.getProgramCount();
		for (int i = 0; i < prgCount; i++) {
			KvtProgram prg = _prj.getProgram(i);
			Log.d(ROBOT_CONTROL_LOGTAG, "         -> " + _prj.getName() + "." + prg.getName() + " (" + prg.getProgramStateString() + ")");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #programStateChanged(com.keba.kemro.kvs.teach.data.project.KvtProgram)
	 */
	public void programStateChanged(KvtProgram _prg) {
		Log.d(ROBOT_CONTROL_LOGTAG, "Program " + _prg.getName() + "'s state is " + _prg.getProgramStateString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #projectListChanged()
	 */
	public void projectListChanged() {
		Log.d(ROBOT_CONTROL_LOGTAG, "Project list hast changed!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.KvtAlarmUpdaterListener
	 * #messageUpdated(int, java.lang.Object)
	 */
	public void messageUpdated(int _lastMessageType, Object _lastMessage) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.KvtAlarmUpdaterListener
	 * #messageAdded(java.lang.String,
	 * com.keba.kemro.serviceclient.alarm.KMessage)
	 */
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.KvtAlarmUpdaterListener
	 * #messageRemoved(java.lang.String,
	 * com.keba.kemro.serviceclient.alarm.KMessage)
	 */
	public void messageRemoved(String _bufferName, KMessage _msg) {
		synchronized (mMsgBufferLock) {
			List<KMessage> q = mMessageQueue.get(_bufferName);
			if (q != null)
				q.remove(_msg);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.KvtAlarmUpdaterListener
	 * #messageChanged(java.lang.String,
	 * com.keba.kemro.serviceclient.alarm.KMessage)
	 */
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
	}

	/**
	 * @return The project that is currently loaded (except system and global
	 *         project), null if none is loaded
	 */
	public static KvtProject getLoadedProject() {

		KvtProject[] list = KvtProjectAdministrator.getCurrentRunningProjects();
		if (list != null) {
			for (KvtProject proj : list)
				if (proj != null && !proj.isGlobalProject() && !proj.isSystemProject())
					return proj;
		}
		return null;
	}
}
