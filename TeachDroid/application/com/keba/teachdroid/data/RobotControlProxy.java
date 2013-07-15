package com.keba.teachdroid.data;

import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.util.Log;

import com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater;
import com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.KvtAlarmUpdaterListener;
import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener;
import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor;
import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor.KvtDriveStateListener;
import com.keba.kemro.kvs.teach.util.KvtExecutionMonitor;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.KvtMainModeListener;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.SafetyState;
import com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator.KvtMotionModeListener;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor.KvtPositionMonitorListener;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.KvtProgramStateListener;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.ProgramMode;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.ProgramState;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener;
import com.keba.kemro.serviceclient.alarm.KMessage;
import com.keba.kemro.teach.dfl.KTcDfl;

/**
 * Class that administers all data related to the robot controller, such as
 * available programs, execution states, connection state, available robots,
 * etc. This class is also responsible for pulling up all internal administrator
 * classes and thus establishing data connections to the robot controller.
 * 
 * From a MVC point of view, this class is the data model of any application
 * 
 * @author ltz
 * @since 04.07.2013
 * 
 */
public class RobotControlProxy {

	private static final String				ROBOT_CONTROL_LOGTAG	= "RobotControl";
	private static String					mClientID;
	private static boolean					mConnected;
	private static RobotControlDataListener	mDataListener;

	/**
	 * connects to all classes which load information from the PLC by
	 * registering to them as a listener with the respective listener interfaces
	 */
	public synchronized static void startup() {
		mDataListener = new RobotControlDataListener();
		mDataListener.addObserver(new Observer() {

			public void update(Observable _observable, Object _data) {
				// Log.d(ROBOT_CONTROL_LOGTAG, _observable + " updated!");
			}
		});

		// add all listeners, consolidate here
		KvtSystemCommunicator.addConnectionListener(mDataListener);
		KvtMainModeAdministrator.addListener(mDataListener);
		KvtMotionModeAdministrator.addListener(mDataListener);
		KvtProjectAdministrator.addProjectListener(mDataListener);
		KvtAlarmUpdater.addListener(mDataListener);
		KvtPositionMonitor.addListener(mDataListener);
		KvtDriveStateMonitor.addListener(mDataListener);

	}

	/**
	 * returns a list with the names of all available robots as strings
	 * 
	 * @return a String array with all robots' names
	 */
	public static String[] getRobotNames() {
		return new String[] { "ArtarmTX60L", "ArtarmTX40", "Scara RS80" };
	}

	/**
	 * Indicates whether the client is connected to the robot controller
	 * 
	 * @return true if true, false if false :-)
	 */
	public static boolean isConnected() {
		return mConnected;
	}

	/**
	 * returns a list of strings which hold the current message backlog at the
	 * time when the method is called. it is possible, that two subsequent calls
	 * produce different results!
	 * 
	 * @return a list of strings, each of which contains one RC/MCU message (no
	 *         Info-Traces!)
	 */
	public static List<String> getMessageBacklog() {
		//
		final List<String> list = new Vector<String>();

		try {
			return new AsyncTask<Void, Void, List<String>>() {

				@Override
				protected List<String> doInBackground(Void... _params) {
					String filter = "RC";

					Set<Entry<String, List<KMessage>>> s = mDataListener.mMessageQueue.entrySet();

					for (Entry<String, List<KMessage>> e : s) {
						if (e.getKey().contains(filter)) {

							for (KMessage msg : e.getValue()) {
								list.add(msg.toString());
							}

							return list;

						}
					}
					return null;
				}
			}.execute((Void) null).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} catch (ExecutionException e) {
			return null;
		}

	}

	/**
	 * returns a list of strings which hold the current trace backlog at the
	 * time when the method is called. it is possible, that two subsequent calls
	 * produce different results!
	 * 
	 * @return a list of strings, each of which contains one trace line (no
	 *         RC/MCU messages!)
	 */
	public static List<String> getTraceBacklog() {
		final List<String> list = new Vector<String>();

		try {
			return new AsyncTask<Void, Void, List<String>>() {

				@Override
				protected List<String> doInBackground(Void... _params) {
					String filter = "Info";

					Set<Entry<String, List<KMessage>>> s = mDataListener.mMessageQueue.entrySet();

					for (Entry<String, List<KMessage>> e : s) {
						if (e.getKey().equals(filter)) {

							for (KMessage msg : e.getValue()) {
								list.add(msg.toString());
							}

							return list;

						}
					}
					return null;
				}

			}.execute((Void) null).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
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

	public static KvtProgram getLoadedProgram() {
		KvtProject proj = getLoadedProject();
		if (proj != null) {
			// TODO: implement
		}

		return null;
	}

	public static String getRefsysName() {
		// return KvtMainModeAdministrator.getChosenRefSys();
		return mDataListener.mChosenRefsys;
	}

	public static void setRefsysName(String _newRefsys) {
		KvtPositionMonitor.buildModels();
		// TODO: change
	}

	public static String getToolName() {
		// return KvtMainModeAdministrator.getChosenTool();
		return mDataListener.mToolName;
	}

	public static SafetyState getSafetyState() {
		// return KvtMainModeAdministrator.getSafetyState();
		return mDataListener.mSafetyState;
	}

	public synchronized static int getOverride() {
		if (mDataListener == null)
			return 0;
		return mDataListener.mOverride.intValue();
	}

	public static boolean drivesReady() {
		return mDataListener.mDrivesReady;
	}

	public static boolean drivesReferenced() {
		return mDataListener.mDrivesRerenced;
	}

	public static boolean drivesPower() {
		return mDataListener.mHasPower;
	}

	public static ProgramMode getProgramMode() {
		return mDataListener.mProgMode;
	}

	public static ProgramState getProgramState() {
		return mDataListener.mProgState;
	}

	public static boolean isAnyProgramRunning() {
		return mDataListener.mIsAnyProgRunning;
	}

	/**
	 * Quits and dismisses the last alarm message that has been reported.
	 * 
	 * @return True if confirming the message was successful, false otherwise
	 * 
	 * @throws IllegalAccessException
	 *             Thrown if the message cannot be quitted.
	 */
	public static boolean confirmLastMessage() throws IllegalAccessException {
		// Object msg = KvtAlarmUpdater.getLastMessage();

		KMessage msg = mDataListener.mLastMessage;
		if (msg != null) {
			final KMessage lMsg = (KMessage) msg;

			if (lMsg.confirmAllowed()) {

				new Thread(new Runnable() {

					public void run() {
						lMsg.quitMessage();
					}
				}).start();

				return true;

			} else
				throw new IllegalAccessException("Confirming message " + lMsg + " not allowed!");
		}
		return false;

	}

	public static void addObserver(Observer _obs) {
		mDataListener.addObserver(_obs);
	}

	/**
	 * General listener class, which consolidates all DFL listeners, thus
	 * collects and processes all information coming from the PLC/robot
	 * controller. All callback methods from those listeners must be
	 * implemented.
	 * 
	 * @author ltz
	 * @since 04.07.2013
	 */
	private static class RobotControlDataListener extends Observable implements KvtTeachviewConnectionListener, KvtMainModeListener,
			KvtMotionModeListener, KvtProjectAdministratorListener, KvtAlarmUpdaterListener, KvtPositionMonitorListener, KvtDriveStateListener,
			KvtProgramStateListener {

		/**
		 * reference to the data function layer
		 */
		private KTcDfl								mDfl;
		/**
		 * the current main mode, could be 4 (AE), 1 (A) or 2 (T1)
		 */
		private int									mCurrentMainMode	= -1;
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
		private KMessage							mLastMessage		= null;
		private SafetyState							mSafetyState;
		private String								mChosenRefsys;
		private String								mToolName;
		private boolean								mIsAnyProgRunning;
		private ProgramState						mProgState;
		private ProgramMode							mProgMode;
		private String								mLoadedProgram;
		private Boolean								mDrivesRerenced;
		private Boolean								mDrivesReady;
		private boolean								mHasPower;
		private Number								mOverride			= 0;
		private List<KvtProject>					mProjects			= new Vector<KvtProject>();

		public void teachviewConnected() {
			mConnected = true;
			mDfl = KvtSystemCommunicator.getTcDfl();
			mClientID = KvtSystemCommunicator.getClientID();
			Log.d(ROBOT_CONTROL_LOGTAG, "Teachview Connected, client-ID is " + mClientID);
			notifyObservers();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener#
		 * teachviewDisconnected()
		 */
		public void teachviewDisconnected() {
			mConnected = false;
			mDfl = null;
			mClientID = null;
			mMessageQueue.clear();
			mMessageHistory.clear();
			Log.d(ROBOT_CONTROL_LOGTAG, "Teachview Disconnected");

			notifyObservers();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator.
		 * KvtMotionModeListener#motionModeChanged()
		 */
		public void motionModeChanged(int _newProgmode) {
			Log.d(ROBOT_CONTROL_LOGTAG, "Motion mode changed to " + _newProgmode);
			notifyObservers();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.
		 * KvtMainModeListener #mainModeChanged(int)
		 */
		public void mainModeChanged(int _newMainMode) {
			if (_newMainMode != mCurrentMainMode) {
				Log.d(ROBOT_CONTROL_LOGTAG, "New Mainmode: " + _newMainMode);
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
		public void projectStateChanged(KvtProject _prj) {
			Log.d(ROBOT_CONTROL_LOGTAG, "Project " + _prj.getName() + "'s state is " + _prj.getProjectStateString());
			if (_prj.isSystemProject())
				return;
			int prgCount = _prj.getProgramCount();
			for (int i = 0; i < prgCount; i++) {
				KvtProgram prg = _prj.getProgram(i);
				Log.d(ROBOT_CONTROL_LOGTAG, "         -> " + _prj.getName() + "." + prg.getName() + " (" + prg.getProgramStateString() + ")");
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
		public void programStateChanged(KvtProgram _prg) {
			Log.d(ROBOT_CONTROL_LOGTAG, "Program " + _prg.getName() + "'s state is " + _prg.getProgramStateString());

			if (_prg.getProgramState() == KvtProgram.LOADED) {
				KvtExecutionMonitor.getTextForProgram(_prg);
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
		public synchronized void projectListChanged() {
			Log.d(ROBOT_CONTROL_LOGTAG, "Project list hast changed!");
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
		public void cartesianPositionChanged(String _compName, Number _value) {
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
		public void chosenToolChanged(String _toolName) {
			mToolName = _toolName;
			notifyObservers();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.
		 * KvtMainModeListener#chosenRefSysChanged(java.lang.String)
		 */
		public void chosenRefSysChanged(String _refsysName) {
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
		public void safetyStateChanged(SafetyState _state) {
			mSafetyState = _state;
			notifyObservers();
		}

		@Override
		public String toString() {
			return "RobotControlDataListener @TC_ID " + mClientID;
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
		public void pathVelocityChanged(float _velocityMms) {
		}
	}

	/**
	 * Switches the drives' power either on or off, depending on the a-priori
	 * state
	 */
	public static void toggleDrivesPower() {
		KvtDriveStateMonitor.toggleDrivesPower();
	}

	/**
	 * @param _value
	 * @throws IllegalArgumentException
	 *             If the value is out of range [0...100]
	 */
	public static void setOverride(int _value) {
		if (_value < 0 || _value > 100)
			throw new IllegalArgumentException("Override value cannot be outside [0...100]!");

		KvtPositionMonitor.setOverride(_value);
	}

	/**
	 * @return
	 */
	public static List<KvtProject> getProjects() {
		return mDataListener.mProjects;
	}

}
