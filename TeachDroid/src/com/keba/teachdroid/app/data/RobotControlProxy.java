package com.keba.teachdroid.app.data;

import android.util.Log;

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
import com.keba.kemro.teach.dfl.KTcDfl;

public class RobotControlProxy implements KvtTeachviewConnectionListener, KvtMainModeListener, KvtMotionModeListener,
		KvtProjectAdministratorListener {

	/**
	 * 
	 */
	private static final String			ROBOT_CONTROL_LOGTAG	= "RobotControl";
	private static String				mClientID;
	private static boolean				mConnected;
	private static RobotControlProxy	mLocalInstance;

	static {
		mLocalInstance = new RobotControlProxy();
		KvtSystemCommunicator.addConnectionListener(mLocalInstance);
		KvtMainModeAdministrator.addListener(mLocalInstance);
		KvtMotionModeAdministrator.addListener(mLocalInstance);
		KvtProjectAdministrator.addProjectListener(mLocalInstance);

	}

	private KTcDfl						mDfl;

	private RobotControlProxy() {
		mClientID = null;
		mConnected = false;

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
		Log.d(ROBOT_CONTROL_LOGTAG, "New Mainmode: " + _newMainMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #projectStateChanged(com.keba.kemro.kvs.teach.data.project.KvtProject)
	 */
	public void projectStateChanged(KvtProject _prj) {
		Log.d(ROBOT_CONTROL_LOGTAG, "Project " + _prj.getName() + "'s state is " + _prj.getProjectState());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #programStateChanged(com.keba.kemro.kvs.teach.data.project.KvtProgram)
	 */
	public void programStateChanged(KvtProgram _prg) {
		Log.d(ROBOT_CONTROL_LOGTAG, "Program " + _prg.getName() + "'s state is " + _prg.getProgramState());
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

}
