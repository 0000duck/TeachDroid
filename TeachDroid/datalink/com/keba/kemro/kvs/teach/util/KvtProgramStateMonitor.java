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
 * Provides information about the program, that is currently in execution as
 * well as its execution state.
 * 
 * @author ltz
 * @since 05.07.2013
 */
public class KvtProgramStateMonitor implements KvtTeachviewConnectionListener, KVariableGroupListener {

	/**
	 * A listener, which is notified, whenever one of the following items change
	 * on the robot controller:
	 * <ul>
	 * <li><b>program name</b>: the name of the program, which is currently in
	 * execution</li>
	 * <li><b>program state</b>: the state of the program. Available states are
	 * listed in {@link ProgramState}</li>
	 * <li><b>program mode</b>: the execution mode of a program. Available
	 * states are listed in {@link ProgramMode}</li>
	 * <li><b>any program running</b>: whether there is at least one program
	 * currently executing or not</li>
	 * </ul>
	 * 
	 * @author ltz
	 * 
	 */
	public interface KvtProgramStateListener {
		/**
		 * The name of the program, which is loaded for the selected robot, has
		 * changed
		 * 
		 * @param _programName
		 *            The name of the program which was just loaded
		 */
		public void loadedProgramNameChanged(String _programName);

		/**
		 * The execution mode of the program, which is loaded for selected
		 * robot, has changed.
		 * 
		 * @see {@link KvtProgramStateMonitor.ProgramMode} for an enumeration of
		 *      available execution modes (continuous, interrupted,...)
		 * @param _newMode
		 *            the new execution mode of the program
		 */
		public void loadedProgramModeChanged(ProgramMode _newMode);

		/**
		 * The execution state of the loaded program, which is selected for
		 * focussed robot, has changed.
		 * 
		 * @see {@link KvtProgramStateMonitor.ProgramState} for an enumeration
		 *      of available execution states
		 * 
		 * @param _newState
		 *            the new execution mode of the program
		 */
		public void loadedProgramStateChanged(ProgramState _newState);

		/**
		 * Tells if there is any program currently in execution. This method is
		 * called, when there is a state change, i.e. if another program gets
		 * loaded, it is not invoked.
		 * 
		 * @param _isAnyRunning
		 *            true if there is a programm in execution, false if not
		 */
		public void isAnyProgramRunning(boolean _isAnyRunning);

	}

	private static KvtProgramStateMonitor			mInstance;

	private final String							mRobotDataPrefix			= "_system.gRcDataRobot[{0}]";
	private final String							mProgNamePostfix			= ".progName";
	private final String							mProgStatePostfix			= ".progState";
	private final String							mProgModePostfix			= ".progMode";
	private final String							mNoProgramRunningPostfix	= ".noProgramRunning";

	private KStructVarWrapper						mProgNameVar, mProgStateVar, mProgModeVar, mNoProgRunningVar;

	private KVariableGroup							mVarGroup;
	private KTcDfl									mDfl;
	private static List<KvtProgramStateListener>	mListeners					= new Vector<KvtProgramStateListener>();

	/**
	 * enumeration type for the program mode, i.e. in which execution mode the
	 * program is, such as stepping, continuous, etc.
	 * 
	 * @author ltz
	 * 
	 */
	public static enum ProgramMode {

		eProgModeNoProg("N/A"), eProgModeContinous("CONT"), eProgModeInterpreterStep("STEP"), eProgModeMotionStep("MSTEP"), eProgModeBackward(
				"BACK"), eMotionStepMax("STEMPAX");

		private String	mStringValue;

		private ProgramMode(String _val) {
			this.mStringValue=_val;
		}

		private static ProgramMode[]	allValues	= values();

		public static ProgramMode fromOrdinal(int n) {
			return allValues[n];
		}

		@Override
		public String toString() {
			return mStringValue;
		}
	};

	/**
	 * enumeration type for the program state, i.e. running, stopped, etc.
	 * 
	 * @author ltz
	 * 
	 */
	public static enum ProgramState {
		eProgStateNoProg, eProgStateStopped, eProgStateRunning, eProgStateInterrupted, eProgStateRepositioning, eMotionStateMax;
		private static ProgramState[]	allValues	= values();

		public static ProgramState fromOrdinal(int n) {
			return allValues[n];
		}
	}

	/**
	 * Initializes the {@link KvtProgramStateMonitor} by letting it listen to
	 * connect-/disconnect events
	 */
	public static void init() {

		mInstance = new KvtProgramStateMonitor();
		KvtSystemCommunicator.addConnectionListener(mInstance);

	}

	/**
	 * avoid instance creation by making the CTor invisible
	 */
	protected KvtProgramStateMonitor() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.teach.dfl.value.KVariableGroupListener#changed(com.keba
	 * .kemro.teach.dfl.value.KStructVarWrapper)
	 */
	public void changed(KStructVarWrapper _variable) {
		if (_variable.equals(mProgNameVar))
			for (KvtProgramStateListener l : mListeners) {
				l.loadedProgramNameChanged((String) _variable.readActualValue(null));
			}
		else if (_variable.equals(mProgModeVar)) {
			for (KvtProgramStateListener l : mListeners) {
				l.loadedProgramModeChanged(ProgramMode.fromOrdinal((Integer) _variable.readActualValue(null)));
			}
		} else if (_variable.equals(mProgStateVar)) {
			for (KvtProgramStateListener l : mListeners) {
				l.loadedProgramStateChanged(ProgramState.fromOrdinal((Integer) _variable.readActualValue(null)));
			}
		}

		else if (_variable.equals(mNoProgRunningVar)) {
			for (KvtProgramStateListener l : mListeners) {
				l.isAnyProgramRunning(!(Boolean) _variable.readActualValue(null));
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

			mVarGroup = mDfl.variable.createVariableGroup("KvtProgramStateMonitor");
			mVarGroup.addListener(this);

			createVariables();

			// mVarGroup.setPollInterval(100);
			mVarGroup.activate();

		}
	}

	/**
	 * Create instances of a {@link KStructVarWrapper} for the execution state,
	 * execution mode, and program name. If each variable wrapper is created, it
	 * is automatically added to the variable group
	 */
	private void createVariables() {

		if (mDfl != null) {
			String prefix = MessageFormat.format(mRobotDataPrefix, KvtMultiKinematikAdministrator.getKinematicIndex());

			mProgNameVar = mDfl.variable.createKStructVarWrapper(prefix + mProgNamePostfix);
			if (mProgNameVar != null)
				mVarGroup.add(mProgNameVar);

			mProgStateVar = mDfl.variable.createKStructVarWrapper(prefix + mProgStatePostfix);
			if (mProgStateVar != null)
				mVarGroup.add(mProgStateVar);

			mProgModeVar = mDfl.variable.createKStructVarWrapper(prefix + mProgModePostfix);
			if (mProgModeVar != null)
				mVarGroup.add(mProgModeVar);

			mNoProgRunningVar = mDfl.variable.createKStructVarWrapper(prefix + mNoProgramRunningPostfix);
			if (mNoProgRunningVar != null)
				mVarGroup.add(mNoProgRunningVar);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener#
	 * teachviewDisconnected()
	 */
	public void teachviewDisconnected() {
		mVarGroup.release();
		mVarGroup.reset();
		mVarGroup = null;
		mProgModeVar = mProgStateVar = mProgModeVar = mNoProgRunningVar = null;
		mDfl = null;
	}

	/**
	 * Adds a listener to the list of listeners, if not already contained
	 * 
	 * @param _l
	 *            an instance of a {@link KvtProgramStateListener}.
	 */
	public static void addListener(KvtProgramStateListener _l) {
		if (mListeners == null)
			mListeners = new Vector<KvtProgramStateMonitor.KvtProgramStateListener>();
		if (!mListeners.contains(_l))
			mListeners.add(_l);
	}

	/**
	 * Removes a listener from the list of listeners
	 * 
	 * @param _l
	 *            an instance of a {@link KvtProgramStateListener}.
	 */
	public static void removeListener(KvtProgramStateListener _l) {
		mListeners.remove(_l);
	}
}
