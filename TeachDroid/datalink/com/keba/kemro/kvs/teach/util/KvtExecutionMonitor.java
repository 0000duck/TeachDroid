/**
 * 
 */
package com.keba.kemro.kvs.teach.util;

import java.util.Vector;

import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener;
import com.keba.kemro.kvs.teach.model.KvtRoutineModel;
import com.keba.kemro.kvs.teach.model.KvtRoutineModelListener;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.KvtProgramStateListener;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.ProgramMode;
import com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.ProgramState;
import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.execution.KExecAdministratorListener;
import com.keba.kemro.teach.dfl.execution.KExecUnitRoutine;
import com.keba.kemro.teach.dfl.structural.KStructNode;
import com.keba.kemro.teach.dfl.structural.KStructProgram;
import com.keba.kemro.teach.dfl.structural.routine.KStructRoutine;

/**
 * @author ltz
 * 
 */
//@formatter:off
public class KvtExecutionMonitor implements KvtTeachviewConnectionListener, 
		KvtRoutineModelListener, //notice changes in the routine model
		KvtProjectAdministratorListener, //notice changes, when projects or programs states change
		KvtProgramStateListener,  //notice, when the RC sets the executing program's names
		KExecAdministratorListener //needed for the updateState() methods 
{	// @formatter:on
	private KvtRoutineModel				mRoutineModel;
	private KvtProject					mLoadedProject;
	private KvtProgram					mProgInExecution;
	private String						mLoadedProgramName;
	private KExecUnitRoutine			m_execRoutine;
	private KStructProgram				m_structProgram;
	private KStructRoutine				m_structRoutine;
	private static KvtExecutionMonitor	mInstance;
	private static KTcDfl				mDfl;

	protected KvtExecutionMonitor() {
		getRoutineModel();
	}

	public static void init() {
		mInstance = new KvtExecutionMonitor();
		KvtSystemCommunicator.addConnectionListener(mInstance);
		KvtProjectAdministrator.addProjectListener(mInstance);
		KvtProgramStateMonitor.addListener(mInstance);
	}

	protected KvtRoutineModel getRoutineModel() {
		if (mRoutineModel == null) {
			mRoutineModel = new KvtRoutineModel();
			mRoutineModel.addRoutineModelListener(this);
		}
		return mRoutineModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener#
	 * teachviewConnected()
	 */
	public void teachviewConnected() {
		mDfl = KvtSystemCommunicator.getTcDfl();
		if (mDfl != null)
			mDfl.execution.addListener(mInstance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener#
	 * teachviewDisconnected()
	 */
	public void teachviewDisconnected() {
		if (mDfl != null)
			mDfl.execution.removeListener(mInstance);
		mDfl = null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.model.KvtRoutineModelListener#modelChanged()
	 */
	public void modelChanged() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.model.KvtRoutineModelListener#routineChanged()
	 */
	public void routineChanged() {
		int lines = mRoutineModel.getLineCount();

		for (int i = 0; i < lines; i++) {
			String line = mRoutineModel.getLine(i);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.model.KvtRoutineModelListener#breakpointAdded
	 * (int)
	 */
	public void breakpointAdded(int _lineOfView) {
		// not needed
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.model.KvtRoutineModelListener#breakpointRemoved
	 * (int)
	 */
	public void breakpointRemoved(int _lineOfView) {
		// not needed
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.model.KvtRoutineModelListener#breakpointChanged
	 * (int)
	 */
	public void breakpointChanged(int _lineOfView) {
		// not needed
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.model.KvtRoutineModelListener#mainFlowPointerChanged
	 * ()
	 */
	public void mainFlowPointerChanged() {
		System.out.println("Mainflow pointer set to " + mRoutineModel.getMainFlowPointer());
	}

	public static String getTextForProgram(KvtProgram _prog) {

		KExecUnitRoutine rout = _prog.getUnnamedExecUnitRoutine();
		if (rout != null) {
			mInstance.mRoutineModel.setKStructRoutine((KStructRoutine) rout.getKStructNode());

			int start = mInstance.mRoutineModel.getLineOfFirstStatement();
			int lines = mInstance.mRoutineModel.getLineCount();
			String str = "";
			for (int i = start; i < lines; i++) {
				str += mInstance.mRoutineModel.getLine(i);
			}
			return str;
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.teach.dfl.execution.KExecAdministratorListener#updateState
	 * ()
	 */
	public void updateState() {

		// if we have a project loaded, and also RC tells us that a program is
		// loaded, try to obtain the loaded program
		if (mLoadedProject != null && mLoadedProgramName != null && mProgInExecution == null) {
			if (mLoadedProgramName.contains(".")) {
				String pn = mLoadedProgramName.substring(mLoadedProgramName.indexOf(".") + 1);
				mProgInExecution = mLoadedProject.getProgram(pn);
			}
		}

		// check that the KvtProgram and the string coming from RC match -->
		// execute loading the file contents
		if (mLoadedProject != null && mProgInExecution != null) {
			if (mLoadedProgramName != null && mLoadedProgramName.contains(mProgInExecution.getName())) {
				setTraceRoutine();
			}
		}

		if (m_execRoutine != null) {
			int mf = m_execRoutine.getMainFlowLine();
			mRoutineModel.setMainFlowPointer(mf);
		}
	}

	/**
	 * sets routine due to automatic routine tracing and displays it in the
	 * routineview. only unnamed routines will be traced.
	 */
	private void setTraceRoutine() {
		KTcDfl d = mDfl;
		if (d != null) {
			synchronized (d.getLockObject()) {
				if ((mProgInExecution != null)) {
					KExecUnitRoutine routine = getTraceRoutine(mProgInExecution.getUnnamedExecUnitRoutine());
					if ((routine != null) && (!routine.equals(m_execRoutine))) {

						setExecUnitRoutine(routine);
						m_execRoutine = routine;
					}
					if (routine == null) {
						KStructProgram sp = mProgInExecution.getStructProgram();
						KStructRoutine sr = (sp != null) ? sp.getUnNamedRoutine() : null;
						if (sr != null && !sr.equals(m_structRoutine)) {
							m_structProgram = sp;
							m_structRoutine = sr;
							m_execRoutine = null;
							getRoutineModel().setKStructRoutine(m_structRoutine);
							mRoutineModel.setMainFlowPointer(-1);
						} else if (m_execRoutine != null) {
							m_execRoutine = null;
							mRoutineModel.setMainFlowPointer(-1);
						}
					}
				}
			}
		}
	}

	private KExecUnitRoutine getTraceRoutine(KExecUnitRoutine routine) {
		if (routine != null) {
			if (0 <= routine.getMainFlowLine()) {
				return routine;
			}
			for (int i = 0; i < routine.getExecUnitRoutineCount(); i++) {
				KExecUnitRoutine r = routine.getExecUnitRoutine(i);
				KStructRoutine sr = (KStructRoutine) r.getKStructNode();
				if ((sr != null) && sr.isUnnamedRoutine()) {
					KExecUnitRoutine found = getTraceRoutine(r);
					if (found != null) {
						return found;
					}
				}
			}
		}
		return null;
	}

	/**
	 * sets the currently executed routine, so that it can be displayed
	 */
	private void setExecUnitRoutine(KExecUnitRoutine routine) {
		if (routine != null) {
			m_execRoutine = routine;
			KStructNode node = routine.getKStructNode();
			if (node instanceof KStructRoutine) {
				m_structRoutine = (KStructRoutine) node;
				node = m_structRoutine.getParent();
				if (node instanceof KStructProgram) {
					m_structProgram = (KStructProgram) node;
					getRoutineModel().setKStructRoutine(m_structRoutine);
				}

			}
		} else if (mProgInExecution != null) {
			m_structProgram = mProgInExecution.getStructProgram();
			m_structRoutine = (m_structProgram != null) ? m_structProgram.getUnNamedRoutine() : null;
			getRoutineModel().setKStructRoutine(m_structRoutine);
			mRoutineModel.setMainFlowPointer(-1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #projectStateChanged(com.keba.kemro.kvs.teach.data.project.KvtProject)
	 */
	public void projectStateChanged(KvtProject _prj) {
		if (_prj.getProjectState() == KvtProject.SUCCESSFULLY_LOADED) {
			mLoadedProject = _prj;
			mProgInExecution = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #programStateChanged(com.keba.kemro.kvs.teach.data.project.KvtProgram)
	 */
	public void programStateChanged(KvtProgram _prg) {
		m_execRoutine = null;
		if (_prg.getProgramState() >= KvtProgram.STOPPED) {
			if (_prg.getParent().equals(mLoadedProject)) {
				mProgInExecution = _prg;
			} else {
				mProgInExecution = null;
				throw new IllegalArgumentException("KvtExecutionMonitor should not load a program, whose parent isnt loaded!");
			}
		} else if (_prg.getProgramState() == KvtProgram.LOADED)
			mProgInExecution = null;
		else if (_prg.getProgramState() < KvtProgram.LOADED) {
			mProgInExecution = null;
			mLoadedProject = null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.data.project.KvtProjectAdministratorListener
	 * #projectListChanged()
	 */
	public void projectListChanged() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.KvtProgramStateListener
	 * #loadedProgramNameChanged(java.lang.String)
	 */
	public void loadedProgramNameChanged(String _programName) {
		mLoadedProgramName = _programName;
		m_execRoutine = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.KvtProgramStateListener
	 * #
	 * loadedProgramModeChanged(com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor
	 * .ProgramMode)
	 */
	public void loadedProgramModeChanged(ProgramMode _newMode) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.KvtProgramStateListener
	 * #loadedProgramStateChanged(com.keba.kemro.kvs.teach.util.
	 * KvtProgramStateMonitor.ProgramState)
	 */
	public void loadedProgramStateChanged(ProgramState _newState) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.KvtProgramStateListener
	 * #isAnyProgramRunning(boolean)
	 */
	public void isAnyProgramRunning(boolean _isAnyRunning) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.teach.dfl.execution.KExecAdministratorListener#
	 * execUnitsRemovedAdded(java.util.Vector, java.util.Vector)
	 */
	public void execUnitsRemovedAdded(Vector _toRemove, Vector _toInsert) {
		// not needed
	}

	public static interface KvtExecutionListener {
		void programCounterChanged(int _line);

		void programCodeChanged(String _source);

	}
}
