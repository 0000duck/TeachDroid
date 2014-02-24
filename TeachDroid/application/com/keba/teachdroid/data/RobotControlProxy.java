package com.keba.teachdroid.data;

import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;

import com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater;
import com.keba.kemro.kvs.teach.controller.KvtTraceUpdater;
import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.util.KvtDriveStateMonitor;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtMainModeAdministrator.SafetyState;
import com.keba.kemro.kvs.teach.util.KvtMotionModeAdministrator;
import com.keba.kemro.kvs.teach.util.KvtPositionMonitor;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.ProgramMode;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.ProgramState;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.serviceclient.alarm.KMessage;
import com.keba.teachdroid.app.Message;

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

	static final String ROBOT_CONTROL_LOGTAG = "RobotControl";
	static String mClientID;
	static boolean mConnected;
	private static RobotControlDataListener mDataListener;

	/**
	 * connects to all classes which load information from the PLC by
	 * registering to them as a listener with the respective listener interfaces
	 */
	public synchronized static void startup() {
		if (mDataListener == null) {
			mDataListener = new RobotControlDataListener();
			mDataListener.addObserver(new Observer() {

				@Override
				public void update(Observable _observable, Object _data) {
					// Log.d(ROBOT_CONTROL_LOGTAG, _observable + " updated!");
				}
			});
		}

		// add all listeners, consolidate here
		KvtSystemCommunicator.addConnectionListener(mDataListener);
		KvtMainModeAdministrator.addListener(mDataListener);
		KvtMotionModeAdministrator.addListener(mDataListener);
		KvtProjectAdministrator.addProjectListener(mDataListener);
		KvtAlarmUpdater.addListener(mDataListener);
		KvtPositionMonitor.addListener(mDataListener);
		KvtDriveStateMonitor.addListener(mDataListener);
		KvtTraceUpdater.addListener(mDataListener);
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
	public static List<Message> getMessageBacklog() {
		//
		final List<Message> list = new Vector<Message>();

		try {
			return new AsyncTask<Void, Void, List<Message>>() {

				@Override
				protected List<Message> doInBackground(Void... _params) {
					String filter = "RC";

					Set<Entry<String, List<KMessage>>> s = mDataListener.mMessageQueue.entrySet();

					for (Entry<String, List<KMessage>> e : s) {
						if (e.getKey().contains(filter)) {

							for (KMessage msg : e.getValue()) {
								list.add(new Message(msg));
							}

						}
					}
					return list;
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
		 return KvtPositionMonitor.getChosenRefSys();
//		return mDataListener.mChosenRefsys;
	}

	public static void setRefsysName(String _newRefsys) {
		KvtPositionMonitor.buildModels();
		// TODO: change
	}

	public static String getToolName() {
		return KvtPositionMonitor.getChosenTool();
//		return mDataListener.mToolName;
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
		if (mDataListener == null)
			return false;
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

					@Override
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

	public static boolean isGlobalLoaded() {
		return mDataListener.isGlobalLoaded();
	}

}
