package com.keba.kemro.kvs.teach.util;

import java.util.Vector;

import com.keba.kemro.kvs.teach.data.rc.KvtRcAdministrator;
import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.execution.KExecUnitRoutine;
import com.keba.kemro.teach.dfl.structural.KMultikinematicListener;
import com.keba.kemro.teach.dfl.value.KStructVarWrapper;
import com.keba.kemro.teach.dfl.value.KVariableGroup;
import com.keba.kemro.teach.dfl.value.KVariableGroupListener;

/**
 * Class that determines in which <em>motion mode</em> the robot currently is.
 * That means, whether program execution is done in continuous ("CONT"),
 * line-stepping ("STEP") or motion stepping ("MSTEP") mode.
 * 
 * @author ltz
 * @date 05.07.2013
 * 
 */
public class KvtMotionModeAdministrator implements KMultikinematicListener, KVariableGroupListener, KvtTeachviewConnectionListener {
	private static KStructVarWrapper				progModeVar;
	private static KStructVarWrapper				actRobotFlowHdlVar;
	private static KStructVarWrapper				nextRobotFlowHdlVar;

	private static KVariableGroup					varGroup;
	private static int								progMode;
	private static KvtMotionModeAdministrator		admin;
	private static Vector<KvtMotionModeListener>	m_listener			= new Vector<KvtMotionModeListener>();
	private static boolean							changed				= false;
	public static String							CONT_STR			= "CONT";
	public static String							STEP_STR			= "STEP";
	public static String							MOTION_STR			= "MSTP";

	private static final int						MODE_STEP			= 2;
	private static final int						MODE_MOTION_STEP	= 3;
	private static final int						MODE_CONT			= 1;
	public static String getMainFlowState(KExecUnitRoutine execRoutine) {
		// if (execRoutine != null) {
		// if (execRoutine.isMainFlowStepping()) {
		// return STEP_STR;
		// }
		// if (progMode == 3) {
		// TcExecutionUnit execUnit = execRoutine.getTcExecutionUnit();
		// if ((execUnit != null) && (actRobotFlowHdl == execUnit.getHandle())
		// || ((actRobotFlowHdl == 0) && (nextRobotFlowHdl ==
		// execUnit.getHandle()))) {
		// return MOTION_STR;
		// }
		// }
		// return CONT_STR;
		// }

		// @ltz: CR_0050518: motion mode icon in stateline controller and in the
		// program mask's header label might get inconsistent
		if (execRoutine != null) {
			switch (progMode) {
			case MODE_CONT:
				return CONT_STR;
			case MODE_STEP:
				return STEP_STR;
			case MODE_MOTION_STEP:
				return MOTION_STR;
			}
			// e.g. for Dürr-Teachview, where "progMode" is always 0
			if (execRoutine.isMainFlowStepping())
				return STEP_STR;
			else
				return CONT_STR;
		}
		return "";
	}

	public static KvtMotionModeAdministrator init() {
		if (admin == null) {
			admin = new KvtMotionModeAdministrator();
		}
		return admin;
	}

	public static interface KvtMotionModeListener {
		public void motionModeChanged(int _progMode);
	}

	public static void addListener(KvtMotionModeListener listener) {
		if (m_listener == null)
			m_listener = new Vector<KvtMotionModeListener>();

		if (!m_listener.contains(listener)) {
			m_listener.addElement(listener);
		}
		if (m_listener.size() > 0) {
			if (varGroup != null)
				varGroup.activate();
		}
	}

	public static void removeListener(KvtMotionModeListener listener) {
		m_listener.removeElement(listener);
		if (m_listener.size() == 0) {
			varGroup.deactivate();
		}
	}

	private KvtMotionModeAdministrator() {
		KvtSystemCommunicator.addConnectionListener(this);
	}

	public void teachviewConnected() {
		synchronized (this) {
			KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
			if (dfl != null) {
				synchronized (dfl.getLockObject()) {
					if (varGroup != null)
						varGroup.release();

					varGroup = dfl.variable.createVariableGroup("KvtMotionModeAdministrator");
					varGroup.addListener(this);
					varGroup.setPollInterval(250);
					dfl.structure.addMultikinematikListener(this);
					kinematikChanged();
					varGroup.activate();
				}
			}
		}
	}

	public void teachviewDisconnected() {
		KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
		if (dfl != null) {
			dfl.structure.removeMultikinematicListener(this);
			if (varGroup != null) {
				varGroup.release();
			}
			progModeVar = null;
			nextRobotFlowHdlVar = null;
			actRobotFlowHdlVar = null;
			varGroup = null;
		}
	}

	public void kinematikChanged() {
		createVariable();
	}

	private synchronized void createVariable() {
		if (varGroup != null) {
			varGroup.release();
			KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
			if (dfl != null) {
				int kin = KvtMultiKinematikAdministrator.getKinematicIndex();
				if (kin >= 0) {
					actRobotFlowHdlVar = dfl.variable.createKStructVarWrapper(KvtRcAdministrator.RCDATA_PREFIX + "gRcDataRobot[" + kin
							+ "].robotFlowHdl");
					nextRobotFlowHdlVar = dfl.variable.createKStructVarWrapper(KvtRcAdministrator.RCDATA_PREFIX + "gRcDataRobot[" + kin
							+ "].nextRobotFlowHdl");
					progModeVar = dfl.variable.createKStructVarWrapper(KvtRcAdministrator.RCDATA_PREFIX + "gRcDataRobot[" + kin + "].progMode");
					if ((progModeVar != null) && (actRobotFlowHdlVar != null) && (nextRobotFlowHdlVar != null)) {
						varGroup.add(progModeVar);
						varGroup.add(actRobotFlowHdlVar);
						varGroup.add(nextRobotFlowHdlVar);
					}
				}
			}
		}
	}

	public void allActualValuesUpdated() {
		if (changed) {
			for (int i = 0; i < m_listener.size(); i++) {
				((KvtMotionModeListener) m_listener.elementAt(i)).motionModeChanged(progMode);
			}
		}
		changed = false;
	}

	public void changed(KStructVarWrapper variable) {
		if (progModeVar == variable) {
			Number n = (Number) progModeVar.getActualValue();
			if (n != null) {
				progMode = n.intValue();
			}
			changed = true;
		} else if (nextRobotFlowHdlVar == variable) {
			Number n = ((Number) nextRobotFlowHdlVar.getActualValue());
			if (n != null) {
				n.intValue();
			}
			changed = true;
		} else if (actRobotFlowHdlVar == variable) {
			Number n = ((Number) actRobotFlowHdlVar.getActualValue());
			if (n != null) {

				n.intValue();
			}
			changed = true;
		}
	}

}