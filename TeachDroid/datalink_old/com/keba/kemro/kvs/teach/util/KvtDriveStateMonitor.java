/**
 * 
 */
package com.keba.kemro.kvs.teach.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.Vector;

import android.util.Log;

import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.value.KStructVarWrapper;
import com.keba.kemro.teach.dfl.value.KVariableGroup;
import com.keba.kemro.teach.dfl.value.KVariableGroupListener;

/**
 * Class to observe the state of the selected robot's mechanical drives. A
 * robot's collective state (the summed up state of all its drives) consists of
 * the following flags:
 * <ul>
 * <li><code>isReady</code> if the drives is ready to move</li>
 * <li><code>drivesOn</code> if the drives have power</li>
 * <li><code>isReferenced</code> if the drives are referenced</li>
 * </ul>
 * 
 * use the {@link KvtDriveStateListener} to be notified about such state
 * changes.
 * 
 * @author ltz
 * @since 04.07.2013
 */
public class KvtDriveStateMonitor implements KvtTeachviewConnectionListener, KVariableGroupListener {

	/**
	 * @author ltz
	 * 
	 */
	public interface KvtDriveStateListener {

		/**
		 * Called when a drive compound (i.e. robot) has gained or lost power
		 * supply.
		 * 
		 * @param _hasPower
		 *            indicates whether the respective robot now has power or
		 *            not
		 */
		public void drivePowerChanged(boolean _hasPower);

		/**
		 * Called when the ready-state of at least one drive has changed
		 * 
		 * @param isReady
		 *            indicates whether the drive compound is now referenced of
		 *            not
		 */
		public void driveIsReadyChanged(Boolean isReady);

		/**
		 * Called when the referenced-state of a at least one drive has changed.
		 * 
		 * @param _isRef
		 *            indicates whether the drive compound is now referenced or
		 *            not
		 */
		public void driveIsReferencedChanged(Boolean _isRef);

	}

	private final String mDrivePowerVarname = "_system.gRcDataRobot[{0}].drivesOn";
	private final String mDriveIsReadyVarname = "_system.gRcDataRobot[{0}].isReady";
	private final String mDriveIsRefVarname = "_system.gRcDataRobot[{0}].isReferenced";
	private final String mDriveSwitchOnVarname = "_system.RcHtControl.simuKeys.drivesOn";

	private KStructVarWrapper mDrivesPowerVar, mDriveIsReadyVar, mDriveIsRefVar;
	private boolean mDrivesPower, mDriveIsReady, mDriveIsRef;

	private KTcDfl mDfl;
	private KVariableGroup mVarGroup;
	private KStructVarWrapper mDrivesSwitchOnVar;
	private static List<KvtDriveStateListener> mListeners = new Vector<KvtDriveStateListener>();

	private static KvtDriveStateMonitor mInstance;

	/**
	 * Initializes the KvtDriveStateMonitor by taking note of
	 * connect-/disconnect events
	 */
	public static void init() {
		mInstance = new KvtDriveStateMonitor();
		KvtSystemCommunicator.addConnectionListener(mInstance);
	}
	
	public static boolean getDrivesPower(){
		return mInstance.mDrivesPower;
	}
	
	public static boolean getDrivesReady(){
		return mInstance.mDriveIsReady;
	}

	public static boolean getDrivesRef(){
		return mInstance.mDriveIsRef;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.teach.dfl.value.KVariableGroupListener#changed(com.keba
	 * .kemro.teach.dfl.value.KStructVarWrapper)
	 */
	public void changed(KStructVarWrapper _variable) {
		if (_variable.equals(mDrivesPowerVar)) {
			mDrivesPower = (Boolean) _variable.readActualValue(null);
			for (KvtDriveStateListener l : mListeners) {
				l.drivePowerChanged(mDrivesPower);
			}
		} else if (_variable.equals(mDriveIsReadyVar)) {
			mDriveIsReady = (Boolean) _variable.readActualValue(null);
			for (KvtDriveStateListener l : mListeners) {
				l.driveIsReadyChanged(mDriveIsReady);
			}
		} else if (_variable.equals(mDriveIsRefVar)) {
			mDriveIsRef = (Boolean) _variable.readActualValue(null);
			for (KvtDriveStateListener l : mListeners) {
				l.driveIsReferencedChanged(mDriveIsRef);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.teach.dfl.value.KVariableGroupListener#allActualValuesUpdated
	 * ()
	 */
	public void allActualValuesUpdated() {
		// changed(mDriveIsReadyVar);
		// changed(mDriveIsRefVar);
		// changed(mDrivesPowerVar);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener#
	 * teachviewConnected()
	 */
	public void teachviewConnected() {
		mDfl = KvtSystemCommunicator.getTcDfl();
		if (mDfl != null) {
			if (mVarGroup != null)
				mVarGroup.release();
			mVarGroup = mDfl.variable.createVariableGroup("KvtDriveStateMonitor");
			mVarGroup.addListener(this);

			createVariables();

			mVarGroup.setPollInterval(100);
			mVarGroup.activate();
		}
	}

	/**
	 * Create instances of a {@link KStructVarWrapper} for the drivesOn,
	 * isReferenced and isReady - flags. If each variable wrapper is created, it
	 * is automatically added to the variable group
	 */
	private void createVariables() {

		int kin = KvtMultiKinematikAdministrator.getKinematicIndex();
		if (kin < 0) {
			Log.e("KvtDriveStateMonitor", "Kinematic index is wrong!");
			return;
		}

		String str = MessageFormat.format(mDriveIsReadyVarname, kin);
		mDriveIsReadyVar = mDfl.variable.createKStructVarWrapper(str);
		if (mDriveIsReadyVar != null) {
			mVarGroup.add(mDriveIsReadyVar);
			mDriveIsReady = (Boolean) mDriveIsReadyVar.readActualValue(null);
		}

		String str2 = MessageFormat.format(mDriveIsRefVarname, kin);
		mDriveIsRefVar = mDfl.variable.createKStructVarWrapper(str2);
		if (mDriveIsRefVar != null) {
			mVarGroup.add(mDriveIsRefVar);
			mDriveIsRef = (Boolean) mDriveIsRefVar.readActualValue(null);
		}
		String str3 = MessageFormat.format(mDrivePowerVarname, kin);
		mDrivesPowerVar = mDfl.variable.createKStructVarWrapper(str3);
		if (mDrivesPowerVar != null) {
			mVarGroup.add(mDrivesPowerVar);
			mDrivesPower = (Boolean) mDrivesPowerVar.readActualValue(null);
		}

		mDrivesSwitchOnVar = mDfl.variable.createKStructVarWrapper(mDriveSwitchOnVarname);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener#
	 * teachviewDisconnected()
	 */
	public void teachviewDisconnected() {
	}

	/**
	 * @param _listener
	 */
	public static synchronized void addListener(KvtDriveStateListener _listener) {
		if (mListeners == null)
			mListeners = new Vector<KvtDriveStateMonitor.KvtDriveStateListener>();

		if (!mListeners.contains(_listener))
			mListeners.add(_listener);
	}

	public static boolean removeListener(KvtDriveStateListener _listener) {
		if (mListeners != null)
			return mListeners.remove(_listener);
		return false;
	}

	public static void toggleDrivesPower() {
		if (mInstance.mDrivesSwitchOnVar != null) {
			mInstance.mDrivesSwitchOnVar.setActualValue(true);

			// wait and reset
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mInstance.mDrivesSwitchOnVar.setActualValue(false);
		}

	}
}
