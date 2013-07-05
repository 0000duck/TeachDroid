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
 * @author ltz
 * 
 */
public class KvtPositionMonitor implements KVariableGroupListener, KvtTeachviewConnectionListener {

	private static KvtPositionMonitor				mInstance;
	private KTcDfl									mDfl;
	private KVariableGroup							mVarGroup;
	private final String							mAxisNameVarnameStub		= "_system.gRcSelectedRobotData.axesName[{0}]";
	private final String							mAxisPosValueVarnameStub	= "_system.gRcSelectedRobotData.axisPosValue[{0}]";

	private final String							mCartPosNameVarnameStub		= "_system.gRcSelectedRobotData.cartCompName[{0}]";
	private final String							mCartPosVarVarnameStub		= "_system.gRcSelectedRobotData.worldPosValue[{0}]";

	private final String							mOverrideVarname			= "_system.gRcData.override";

	private List<KStructVarWrapper>					mPositionVars				= new Vector<KStructVarWrapper>();
	private List<KStructVarWrapper>					mNameVars					= new Vector<KStructVarWrapper>();

	private List<KStructVarWrapper>					mCartPosVars				= new Vector<KStructVarWrapper>();
	private List<KStructVarWrapper>					mCartNameVars				= new Vector<KStructVarWrapper>();
	private KStructVarWrapper						mOverrideVar;

	private static List<KvtPositionMonitorListener>	mListeners					= new Vector<KvtPositionMonitor.KvtPositionMonitorListener>();

	public static void init() {
		mInstance = new KvtPositionMonitor();
		KvtSystemCommunicator.addConnectionListener(mInstance);
	}

	protected KvtPositionMonitor() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.teach.dfl.value.KVariableGroupListener#changed(com.keba
	 * .kemro.teach.dfl.value.KStructVarWrapper)
	 */
	public void changed(KStructVarWrapper _variable) {
		int index = mPositionVars.indexOf(_variable);
		if (index >= 0) {
			String name = mNameVars.get(index).readActualValue(null).toString();
			for (KvtPositionMonitorListener l : mListeners)
				l.axisPositionChanged(index, (Number) _variable.readActualValue(null), name);
			// Log.d("KvtPositionMonitor", "Axis " + (index + 1) + " [" + name +
			// "] has position " + _variable.getActualValue());
			return;
		}

		index = mCartPosVars.indexOf(_variable);
		if (index >= 0) {
			String name = mCartNameVars.get(index).readActualValue(null).toString();
			for (KvtPositionMonitorListener l : mListeners)
				l.cartesianPositionChanged(name, (Number) _variable.readActualValue(null));
			// Log.d("KvtPositionMonitor", "Component " + name + ": " +
			// _variable.getActualValue());
			return;
		}

		if (_variable.equals(mOverrideVar)) {
			for (KvtPositionMonitorListener l : mListeners)
				l.overrideChanged(((Number) _variable.readActualValue(null)).floatValue() / 10);
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

	protected int getNumAxes() {
		return 6;
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

			// create variable group
			mVarGroup = mDfl.variable.createVariableGroup("KvtPositionMonitor");
			mVarGroup.addListener(this);

			createAxisPosVariables();
			createCartPosVariables();
			createOverrideVariables();

			// activate
			mVarGroup.setPollInterval(100);
			mVarGroup.activate();
		}
	}

	/**
	 * 
	 */
	private void createCartPosVariables() {
		for (int i = 0; i < 6; i++) {
			// cartesian component name vars
			String compVar = MessageFormat.format(mCartPosNameVarnameStub, i);
			KStructVarWrapper w1 = mDfl.variable.createKStructVarWrapper(compVar);
			if (w1 != null) {
				mCartNameVars.add(w1);
				mVarGroup.add(w1);
			}

			// cartesian position vars
			String posVar = MessageFormat.format(mCartPosVarVarnameStub, i);
			KStructVarWrapper w2 = mDfl.variable.createKStructVarWrapper(posVar);
			if (w2 != null) {
				mCartPosVars.add(w2);
				mVarGroup.add(w2);
			}

		}
	}

	/**
	 * 
	 */
	private void createAxisPosVariables() {
		// create variables
		int numAxes = getNumAxes();
		for (int i = 0; i < numAxes; i++) {

			// position variable
			String posVar = MessageFormat.format(mAxisPosValueVarnameStub, i);
			KStructVarWrapper wrpP = mDfl.variable.createKStructVarWrapper(posVar);
			if (wrpP != null) {
				mPositionVars.add(wrpP);
				mVarGroup.add(wrpP);
			}

			// name variable
			String nameVar = MessageFormat.format(mAxisNameVarnameStub, i);
			KStructVarWrapper wrpN = mDfl.variable.createKStructVarWrapper(nameVar);
			if (wrpN != null) {
				mNameVars.add(wrpN);
				mVarGroup.add(wrpN);
			}
		}
	}

	private void createOverrideVariables() {

		KStructVarWrapper wrp = mDfl.variable.createKStructVarWrapper(mOverrideVarname);
		if (wrp != null) {
			mOverrideVar = wrp;
			mVarGroup.add(wrp);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener#
	 * teachviewDisconnected()
	 */
	public void teachviewDisconnected() {

		mPositionVars.clear();
		mNameVars.clear();

		mVarGroup.release();
		mVarGroup.reset();

	}

	public synchronized static void addListener(KvtPositionMonitorListener _listener) {
		if (!mListeners.contains(_listener))
			mListeners.add(_listener);
	}

	public synchronized static void removeListener(KvtPositionMonitorListener _listener) {
		mListeners.remove(_listener);
	}

	public static interface KvtPositionMonitorListener {
		public void cartesianPositionChanged(String _compName, Number _value);

		public void axisPositionChanged(int axisNo, Number _value, String _axisName);

		public void overrideChanged(Number _override);
	}
}
