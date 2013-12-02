/**
 * 
 */
package com.keba.kemro.kvs.teach.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.Vector;

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

	private final String						mDrivePowerVarname		= "_system.gRcDataRobot[{0}].drivesOn";
	private final String						mDriveIsReadyVarname	= "_system.gRcDataRobot[{0}].isReady";
	private final String						mDriveIsRefVarname		= "_system.gRcDataRobot[{0}].isReferenced";
	// private final String mDriveSwitchOnVarname =
	// "_system.RcHtControl.simuKeys.drivesOn";
	private final String						mDriveSwitchOnVarname	= "_system.RcHtControl.uiKeys.drivesOn";
	// private final String mDriveSwitchOnVarname = "_system.foo";

	private KStructVarWrapper					mDrivesPowerVar, mDriveIsReadyVar, mDriveIsRefVar;
	private boolean								mDrivesPower, mDriveIsReady, mDriveIsRef;

	private KTcDfl								mDfl;
	private KVariableGroup						mVarGroup;
	private KStructVarWrapper					mDrivesSwitchOnVar;
	private static List<KvtDriveStateListener>	mListeners				= new Vector<KvtDriveStateListener>();

	private static KvtDriveStateMonitor			mInstance;

	/**
	 * Initializes the KvtDriveStateMonitor by taking note of
	 * connect-/disconnect events
	 */
	public static void init() {
		mInstance = new KvtDriveStateMonitor();
		KvtSystemCommunicator.addConnectionListener(mInstance);
	}

	public static Boolean getDrivesPower() {
		Boolean pwr = mInstance.readDrivesPower();
		if (pwr == null)
			pwr = Boolean.FALSE;
		return pwr;
	}

	/**
	 * @return a Boolean to indicate whether drives have power or not, and null if the information could not be obtained
	 */
	private Boolean readDrivesPower() {

		if (mDrivesPowerVar != null) {
			Object res = mDrivesPowerVar.readActualValue(null);
			if (res instanceof Boolean)
				return (Boolean) res;
		}
		return null;
	}

	public static Boolean getDrivesReady() {
		// return mInstance.mDriveIsReady;
		return mInstance.readDrivesReady();
	}

	/**
	 * @return
	 */
	private Boolean readDrivesReady() {
		if (mDriveIsReadyVar != null) {
			Object res = mDriveIsReadyVar.readActualValue(null);
			if (res instanceof Boolean) {
				return (Boolean) res;
			}
		}
		return null;
	}

	public static Boolean getDrivesRef() {
		return mInstance.readDrivesRef();
	}

	/**
	 * @return
	 */
	private Boolean readDrivesRef() {
		if (mDriveIsRefVar != null) {
			Object res = mDriveIsRefVar.readActualValue(null);
			if (res instanceof Boolean) {
				return (Boolean) res;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.teach.dfl.value.KVariableGroupListener#changed(com.keba
	 * .kemro.teach.dfl.value.KStructVarWrapper)
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
	public void teachviewDisconnected() {
	}

	/**
	 * @param _listener
	 */
	public static synchronized Boolean addListener(KvtDriveStateListener _listener) {
		if (mListeners == null)
			mListeners = new Vector<KvtDriveStateMonitor.KvtDriveStateListener>();

		if (!mListeners.contains(_listener))
			return mListeners.add(_listener);
		return false;
	}

	public static boolean removeListener(KvtDriveStateListener _listener) {
		if (mListeners != null)
			return mListeners.remove(_listener);
		return false;
	}

	public static boolean toggleDrivesPower() {
		boolean res = true;
		if (mInstance.mDrivesSwitchOnVar != null) {
			res &= mInstance.mDrivesSwitchOnVar.setActualValue(true);

			// wait and reset
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			res &= mInstance.mDrivesSwitchOnVar.setActualValue(false);

		}
		return res;

	}
}
