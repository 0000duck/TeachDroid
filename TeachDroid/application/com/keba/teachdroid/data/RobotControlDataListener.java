/**
 * 
 */
package com.keba.teachdroid.data;

import java.util.Hashtable;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import android.util.Log;

import com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.KvtAlarmUpdaterListener;
import com.keba.kemro.kvs.teach.controller.KvtTraceUpdater.KvtTraceUpdateListener;
import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener;
import com.keba.kemro.kvs.teach.util.KvtExecutionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener;
import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.KvtMainModeListener;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.SafetyState;
import com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator.KvtMotionModeListener;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.KvtProgramStateListener;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.ProgramMode;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.ProgramState;
import com.keba.kemro.serviceclient.alarm.KMessage;
import com.keba.kemro.teach.dfl.KTcDfl;

/**
 * General listener class, which consolidates all DFL listeners, thus
 * collects and processes all information coming from the PLC/robot
 * controller. All callback methods from those listeners must be
 * implemented.
 * 
 * @author ltz
 * @since 04.07.2013
 */
class RobotControlDataListener extends Observable implements KvtTeachviewConnectionListener, KvtMainModeListener, KvtMotionModeListener, KvtProjectAdministratorListener, KvtAlarmUpdaterListener, KvtPositionMonitorListener,
		KvtDriveStateListener, KvtProgramStateListener, KvtTraceUpdateListener {

	/**
	 * reference to the data function layer
	 */
	private KTcDfl mDfl;
	/**
	 * the current main mode, could be 4 (AE), 1 (A) or 2 (T1)
	 */
	private int mCurrentMainMode = -1;
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
	Hashtable<String, List<KMessage>> mMessageQueue = new Hashtable<String, List<KMessage>>();
	KMessage mLastMessage = null;
	SafetyState mSafetyState;
	private String mChosenRefsys = KvtPositionMonitor.getChosenRefSys();
	private String mToolName = KvtPositionMonitor.getChosenTool();
	boolean mIsAnyProgRunning;
	ProgramState mProgState = null;
	ProgramMode mProgMode = null;
	private String mLoadedProgram;
	Boolean mDrivesRerenced;
	Boolean mDrivesReady;
	boolean mHasPower;
	Number mOverride = 0;
	List<KvtProject> mProjects = new Vector<KvtProject>();
	private float mPathVelocity;
	private String mJogTool = KvtPositionMonitor.getJogTool();
	private String mJogRefsys = KvtPositionMonitor.getJogRefSys();
	private boolean								mGlobalLoaded;

	@Override
	public void teachviewConnected() {
		RobotControlProxy.mConnected = true;
		mDfl = KvtSystemCommunicator.getTcDfl();
		RobotControlProxy.mClientID = KvtSystemCommunicator.getClientID();
		Log.d(RobotControlProxy.ROBOT_CONTROL_LOGTAG, "Teachview Connected, client-ID is " + RobotControlProxy.mClientID);
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener#
	 * teachviewDisconnected()
	 */
	@Override
	public void teachviewDisconnected() {
		RobotControlProxy.mConnected = false;
		mDfl = null;
		RobotControlProxy.mClientID = null;
		mMessageQueue.clear();
		mMessageHistory.clear();
		Log.d(RobotControlProxy.ROBOT_CONTROL_LOGTAG, "Teachview Disconnected");

		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator.
	 * KvtMotionModeListener#motionModeChanged()
	 */
	@Override
	public void motionModeChanged(int _newProgmode) {
		Log.d(RobotControlProxy.ROBOT_CONTROL_LOGTAG, "Motion mode changed to " + _newProgmode);
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.
	 * KvtMainModeListener #mainModeChanged(int)
	 */
	@Override
	public void mainModeChanged(int _newMainMode) {
		if (_newMainMode != mCurrentMainMode) {
			Log.d(RobotControlProxy.ROBOT_CONTROL_LOGTAG, "New Mainmode: " + _newMainMode);
			mCurrentMainMode = _newMainMode;
			notifyObservers();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #
	 * projectStateChanged(com.keba.kemro.kvs.teach.data.project.KvtProject)
	 */
	@Override
	public void projectStateChanged(KvtProject _prj) {

		Log.d(RobotControlProxy.ROBOT_CONTROL_LOGTAG, "Project " + _prj.getName() + "'s state is " + _prj.getProjectStateString());
		if (_prj.isSystemProject())
			return;
		if (_prj.isGlobalProject() || _prj.getName().equalsIgnoreCase("_global")) {
			mGlobalLoaded = true;
		}
		int prgCount = _prj.getProgramCount();
		for (int i = 0; i < prgCount; i++) {
			KvtProgram prg = _prj.getProgram(i);
			Log.d(RobotControlProxy.ROBOT_CONTROL_LOGTAG, "         -> " + _prj.getName() + "." + prg.getName() + " (" + prg.getProgramStateString() + ")");
		}
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #
	 * programStateChanged(com.keba.kemro.kvs.teach.data.project.KvtProgram)
	 */
	@Override
	public void programStateChanged(KvtProgram _prg) {
		Log.d(RobotControlProxy.ROBOT_CONTROL_LOGTAG, "Program " + _prg.getName() + "'s state is " + _prg.getProgramStateString());

		if (_prg.getProgramState() == KvtProgram.LOADED) {
			KvtExecutionMonitor.getProgramSourceCode(_prg);
		}

		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #projectListChanged()
	 */
	@Override
	public synchronized void projectListChanged() {
		Log.d(RobotControlProxy.ROBOT_CONTROL_LOGTAG, "Project list hast changed!");
		notifyObservers();
		KvtProject[] list = KvtProjectAdministrator.getAllProjects();

		mProjects.clear();
		for (KvtProject p : list) {
			for (int i = 0; i < p.getProgramCount(); i++) {
				KvtProgram proj = p.getProgram(i);
			}
			if (!p.isSystemProject() && !mProjects.contains(p))
				mProjects.add(p);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.
	 * KvtAlarmUpdaterListener #messageUpdated(int, java.lang.Object)
	 */
	@Override
	public void messageUpdated(int _lastMessageType, Object _lastMessage) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.
	 * KvtAlarmUpdaterListener #messageAdded(java.lang.String,
	 * com.keba.kemro.serviceclient.alarm.KMessage)
	 */
	@Override
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
		if (_bufferName.contains("RC"))
			mLastMessage = _msg;
		else
			mLastMessage = null;

		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.
	 * KvtAlarmUpdaterListener #messageRemoved(java.lang.String,
	 * com.keba.kemro.serviceclient.alarm.KMessage)
	 */
	@Override
	public void messageRemoved(String _bufferName, KMessage _msg) {
		synchronized (mMsgBufferLock) {
			// remove message from queue
			List<KMessage> q = mMessageQueue.get(_bufferName);
			if (q != null)
				q.remove(_msg);

			// update last message
			if (q != null && !q.isEmpty() && _bufferName.contains("RC")) {
				mLastMessage = q.get(0);
			} else
				mLastMessage = null;
		}
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.
	 * KvtAlarmUpdaterListener #messageChanged(java.lang.String,
	 * com.keba.kemro.serviceclient.alarm.KMessage)
	 */
	@Override
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
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtPositionMonitor.
	 * KvtPositionMonitorListener
	 * #cartesianPositionChanged(java.lang.String, java.lang.Number)
	 */
	@Override
	public void cartesianPositionChanged(int _cartNo, String _compName, Number _value) {
		Log.d("KvtPositionMonitor", "Component " + _compName + ": " + _value);
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtPositionMonitor.
	 * KvtPositionMonitorListener #axisPositionChanged(int,
	 * java.lang.Number, java.lang.String)
	 */
	@Override
	public void axisPositionChanged(int _axisNo, Number _value, String _axisName) {
		Log.d("KvtPositionMonitor", "Axis " + (_axisNo + 1) + " [" + _axisNo + "] has position " + _value);
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtPositionMonitor.
	 * KvtPositionMonitorListener#overrideChanged(java.lang.Number)
	 */
	public void overrideChanged(Number _override) {
		Log.d("KvtPositionMonitor", "Override of focussed robot changed to " + _override);
		mOverride = _override;
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener
	 * #drivePowerChanged(boolean)
	 */
	@Override
	public void drivePowerChanged(boolean _hasPower) {
		mHasPower = _hasPower;
		notifyObservers();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener
	 * #driveIsReadyChanged(java.lang.Boolean)
	 */
	@Override
	public void driveIsReadyChanged(Boolean _ready) {
		mDrivesReady = _ready;
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener
	 * #driveIsReferencedChanged(java.lang.Boolean)
	 */
	@Override
	public void driveIsReferencedChanged(Boolean _isRef) {
		mDrivesRerenced = _isRef;
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.
	 * KvtProgramStateListener#programNameChanged(java.lang.String)
	 */
	@Override
	public void loadedProgramNameChanged(String _programName) {
		mLoadedProgram = _programName;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.
	 * KvtProgramStateListener
	 * #programModeChanged(com.keba.kemro.kvs.teach.util
	 * .KvtProgramStateMonitor.ProgramMode)
	 */
	@Override
	public void loadedProgramModeChanged(ProgramMode _newMode) {
		mProgMode = _newMode;
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.
	 * KvtProgramStateListener
	 * #programStateChanged(com.keba.kemro.kvs.teach.util
	 * .KvtProgramStateMonitor.ProgramState)
	 */
	@Override
	public void loadedProgramStateChanged(ProgramState _newState) {
		mProgState = _newState;
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.
	 * KvtProgramStateListener#isProgramRunning(boolean)
	 */
	@Override
	public void isAnyProgramRunning(boolean _isAnyRunning) {
		mIsAnyProgRunning = _isAnyRunning;
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.
	 * KvtMainModeListener#chosenToolChanged(java.lang.String)
	 */
	@Override
	public void selectedToolChanged(String _toolName) {
		mToolName = _toolName;
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.
	 * KvtMainModeListener#chosenRefSysChanged(java.lang.String)
	 */
	@Override
	public void selectedRefSysChanged(String _refsysName) {
		mChosenRefsys = _refsysName;
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.
	 * KvtMainModeListener#safetyStateChanged(com.keba.kemro.kvs.teach.util.
	 * KvtMainModeAdministrator.SafetyState)
	 */
	@Override
	public void safetyStateChanged(SafetyState _state) {
		mSafetyState = _state;
		notifyObservers();
	}

	@Override
	public String toString() {
		return "RobotControlDataListener @TC_ID " + RobotControlProxy.mClientID;
	}

	@Override
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtPositionMonitor.
	 * KvtPositionMonitorListener#pathVelocityChanged(float)
	 */
	@Override
	public void pathVelocityChanged(float _velocityMms) {
		setPathVelocity(_velocityMms);
		setChanged();
		notifyObservers(_velocityMms);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.controller.KvtTraceUpdater.
	 * KvtTraceUpdateListener#lineReceived(java.lang.String)
	 */
	@Override
	public void lineReceived(String _line) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtPositionMonitor.
	 * KvtPositionMonitorListener#jogToolChanged(java.lang.String)
	 */
	@Override
	public void jogToolChanged(String _jogTool) {
		setJogTool(_jogTool);
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtPositionMonitor.
	 * KvtPositionMonitorListener#jogRefsysChanged(java.lang.String)
	 */
	@Override
	public void jogRefsysChanged(String _jogRefsys) {
		mJogRefsys = _jogRefsys;
		notifyObservers();
	}

	/**
	 * @return the pathVelocity
	 */
	public float getPathVelocity() {
		return mPathVelocity;
	}

	/**
	 * @param pathVelocity
	 *            the pathVelocity to set
	 */
	public void setPathVelocity(float pathVelocity) {
		mPathVelocity = pathVelocity;
	}

	/**
	 * @return the jogTool
	 */
	public String getJogTool() {
		return mJogTool;
	}

	/**
	 * @param jogTool
	 *            the jogTool to set
	 */
	public void setJogTool(String jogTool) {
		mJogTool = jogTool;
	}

	public boolean isGlobalLoaded() {
		return mGlobalLoaded;
	}
}