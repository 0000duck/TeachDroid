/**
 * 
 */
package com.keba.kemro.kvs.teach.util;

import java.util.Vector;

import com.keba.kemro.kvs.teach.data.rc.KvtRcAdministrator;
import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.structural.KMultikinematicListener;
import com.keba.kemro.teach.dfl.value.KStructVarWrapper;
import com.keba.kemro.teach.dfl.value.KVariableGroup;
import com.keba.kemro.teach.dfl.value.KVariableGroupListener;

/**
 * @author ltz
 * 
 */
public class KvtMainModeAdministrator implements KMultikinematicListener, KVariableGroupListener, KvtTeachviewConnectionListener {

	private final Object					m_dlfLock			= new Object();
	private int								m_actualMainMode	= -1;
	private static KVariableGroup			m_varGroup;
	private static KStructVarWrapper		m_mainModeVar;
	private static KvtMainModeAdministrator	instance;
	private static Vector					m_listeners;

	protected KvtMainModeAdministrator() {
		KvtSystemCommunicator.addConnectionListener(this);
		m_listeners = new Vector();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener#teachviewConnected()
	 */
	public void teachviewConnected() {
		synchronized (m_dlfLock) {
			KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
			if (dfl != null) {
				synchronized (dfl.getLockObject()) {
					m_varGroup = dfl.variable.createVariableGroup("KvtMainModeAdministrator");
					m_varGroup.addListener(this);
					m_varGroup.setPollInterval(250);
					dfl.structure.addMultikinematikListener(this);
				}
			}
			createVariable();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener#teachviewDisconnected()
	 */
	public void teachviewDisconnected() {
		KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
		if (dfl != null) {
			dfl.structure.removeMultikinematicListener(this);
			if (m_varGroup != null) {
				m_varGroup.release();
			}
			m_varGroup = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.teach.dfl.value.KVariableGroupListener#changed(com.keba.kemro.teach.dfl.value.KStructVarWrapper)
	 */
	public void changed(KStructVarWrapper _variable) {
		if (_variable.equals(m_mainModeVar)) {
			Number mode = (Number) _variable.getActualValue();
			if (mode != null) {
				m_actualMainMode = mode.intValue();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.teach.dfl.value.KVariableGroupListener#allActualValuesUpdated()
	 */
	public void allActualValuesUpdated() {
		for (int i = 0; i < m_listeners.size(); i++) {
			((KvtMainModeListener) m_listeners.elementAt(i)).mainModeChanged(m_actualMainMode);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.teach.dfl.structural.KMultikinematicListener#kinematikChanged()
	 */
	public void kinematikChanged() {
		createVariable();
	}

	/**
	 * 
	 */
	private synchronized void createVariable() {
		if (m_varGroup != null) {
			m_varGroup.release();
			KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
			if (dfl != null) {
				int kin = KvtMultiKinematikAdministrator.getKinematicIndex();
				if (kin >= 0) {
					m_mainModeVar = dfl.variable.createKStructVarWrapper(KvtRcAdministrator.RCDATA_PREFIX + "gRcData.selectedMainMode");

					if (m_mainModeVar == null) { // try the gRcData-path
						m_mainModeVar = dfl.variable.createKStructVarWrapper(KvtRcAdministrator.RCDATA_PREFIX + "gRcDataRobot[" + kin + "].selectedMainMode");
					}

					if (m_mainModeVar != null) {
						m_varGroup.add(m_mainModeVar);
						m_varGroup.addListener(this);
						m_varGroup.activate();
						Object mm = m_mainModeVar.readActualValue(null);
					}
					// actRobotFlowHdlVar = dfl.variable.createKStructVarWrapper(KvtRcAdministrator.RCDATA_PREFIX + "gRcDataRobot[" + kin + "].robotFlowHdl");
					// nextRobotFlowHdlVar = dfl.variable.createKStructVarWrapper(KvtRcAdministrator.RCDATA_PREFIX + "gRcDataRobot[" + kin + "].nextRobotFlowHdl");
					// progModeVar = dfl.variable.createKStructVarWrapper(KvtRcAdministrator.RCDATA_PREFIX + "gRcDataRobot[" + kin + "].progMode");
					// if ((progModeVar != null) && (actRobotFlowHdlVar != null) && (nextRobotFlowHdlVar != null)) {
					// varGroup.add(progModeVar);
					// varGroup.add(actRobotFlowHdlVar);
					// varGroup.add(nextRobotFlowHdlVar);
					// }
				}
			}
		}
	}

	/**
	 * adds a listener to the administrator which is notified, whenever the main mode changes
	 * 
	 * @param _l
	 *            the new listener
	 * @return true if the listener could be added, false if it already was registered
	 */
	public static boolean addListener(KvtMainModeListener _l) {
		if (!m_listeners.contains(_l)) {
			m_listeners.addElement(_l);
			return true;
		}
		return false;

	}

	public static int getMainMode() {
		return instance.m_actualMainMode;
	}

	public static KvtMainModeAdministrator init() {
		if (instance == null) {
			instance = new KvtMainModeAdministrator();
		}

		return instance;
	}

	public static interface KvtMainModeListener {
		void mainModeChanged(int _newMainMode);
	}

	/**
	 * @param _l
	 */
	public static void removeListener(KvtMainModeListener _l) {
		if (m_listeners.contains(_l)) {
			m_listeners.removeElement(_l);
		}
	}
}
