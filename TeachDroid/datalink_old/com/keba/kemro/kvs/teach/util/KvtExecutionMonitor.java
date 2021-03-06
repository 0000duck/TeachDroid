/**
 * 
 */
package com.keba.kemro.kvs.teach.util;

import java.util.List;
import java.util.Vector;

import com.keba.kemro.kvs.teach.data.program.ProgramStarter;
import com.keba.kemro.kvs.teach.data.program.ProgramStopper;
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
	private KvtRoutineModel						mRoutineModel;
	private KvtProject							mLoadedProject;
	private KvtProgram							mProgInExecution;
	private String								mLoadedProgramName;
	private KExecUnitRoutine					m_execRoutine;
	private KStructProgram						m_structProgram;
	private KStructRoutine						m_structRoutine;
	private static KvtExecutionMonitor			mInstance;
	private static KTcDfl						mDfl;
	private static List<KvtExecutionListener>	mListeners;

	protected KvtExecutionMonitor() {
		getRoutineModel();

	}

	public static void init() {
		mInstance = new KvtExecutionMonitor();
		mListeners = new Vector<KvtExecutionListener>();
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
	@Override
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
	@Override
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
	@Override
	public void modelChanged() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.model.KvtRoutineModelListener#routineChanged()
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public void mainFlowPointerChanged() {
		System.out.println("Mainflow pointer set to " + mRoutineModel.getMainFlowPointer());
	}

	/**
	 * Reads the program sourcecode for a given Teachcontrol program. Lines are
	 * separated with a "\n". Note that a network connection is established
	 * within this method, so on some Operating Systems (e.g. Android) this
	 * method must not be invoked from the UI thread!
	 * 
	 * @param _prog
	 *            the {@link KvtProgram} whose source code is to be obtained
	 * @return A newline-separated string containing the program lines
	 */
	public static String getProgramSourceCode(KvtProgram _prog) {

		KvtProject p = KvtProjectAdministrator.getProject(_prog.getParent().getName());
		_prog = p.getProgram(_prog.getName());
		KExecUnitRoutine rout = _prog.getUnnamedExecUnitRoutine();
		KStructRoutine kstructroutine = null;

		if (rout == null) {
			KStructProgram sp = _prog.getStructProgram();
			KStructRoutine sr = (sp != null) ? sp.getUnNamedRoutine() : null;
			kstructroutine = sr;

		} else /* (rout != null) */{
			kstructroutine = (KStructRoutine) rout.getKStructNode();

		}
		mInstance.mRoutineModel.setKStructRoutine(kstructroutine);

		int start = mInstance.mRoutineModel.getLineOfFirstStatement();
		int lines = mInstance.mRoutineModel.getLineCount();
		String str = "";
		for (int i = start; i < lines; i++) {
			str += mInstance.mRoutineModel.getLine(i);
			str += "\n";
		}
		return str;

	}

	public static void addListener(KvtExecutionListener _listener) {
		if (!mListeners.contains(_listener))
			mListeners.add(_listener);
	}

	public static boolean removeListener(KvtExecutionListener _listener) {
		return mListeners.remove(_listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.teach.dfl.execution.KExecAdministratorListener#updateState
	 * ()
	 */
	@Override
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
			for (KvtExecutionListener l : mListeners) { // its ok to use a
														// static variable since
														// we're using a
														// singleton
				l.programCounterChanged(mf);
			}
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
	@Override
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
	@Override
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
	@Override
	public void projectListChanged() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.KvtProgramStateListener
	 * #loadedProgramNameChanged(java.lang.String)
	 */
	@Override
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
	@Override
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
	@Override
	public void loadedProgramStateChanged(ProgramState _newState) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.kemro.kvs.teach.util.KvtProgramStateMonitor.KvtProgramStateListener
	 * #isAnyProgramRunning(boolean)
	 */
	@Override
	public void isAnyProgramRunning(boolean _isAnyRunning) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.teach.dfl.execution.KExecAdministratorListener#
	 * execUnitsRemovedAdded(java.util.Vector, java.util.Vector)
	 */
	@Override
	public void execUnitsRemovedAdded(Vector _toRemove, Vector _toInsert) {
		// not needed
	}

	/**
	 * Starts the program which is currently loaded. Note that if more than one
	 * program is currently loaded, the call will fail an no program will be
	 * launched
	 * 
	 * @param _prg
	 *            The program which to start
	 */
	public static void startProgram(KvtProgram _prg) {
		new Thread(new ProgramStarter(_prg)).start();
	}

	/**
	 * Stops the execution of a program and pauses its main run.
	 * 
	 * @param _prg
	 *            The program, which to stop
	 */
	public static void stopProgram(KvtProgram _prg) {
		new Thread(new ProgramStopper(_prg)).start();
	}

	public static interface KvtExecutionListener {
		/**
		 * Called when the position of the program counter has changed
		 * 
		 * @param _line
		 *            the line number to which it has changed
		 */
		void programCounterChanged(int _line);

		/**
		 * 
		 * @param _source
		 */
		void programCodeChanged(String _source);

	}


	/**
	 * Closes ("kills") the specified program. The program will then be in the
	 * {@link KvtProgram#OPEN} state, regardless of pre-condition state.
	 * 
	 * @param _prg
	 *            The program which is to be closed.
	 */
	public static void closeProgram(KvtProgram _prg) {
		KvtProjectAdministrator.stopProgram(_prg);
	}
}
