/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Projekt   : KEMRO.teachview.4
 *    Auftragsnr: 5500395
 *    Erstautor : sinn
 *    Datum     : 01.04.2003
 *--------------------------------------------------------------------------
 *      Revision:
 *        Author:  sinn
 *          Date:
 *------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl.execution;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.routine.*;
import com.keba.kemro.teach.dfl.util.*;
import com.keba.kemro.teach.dfl.value.*;
import com.keba.kemro.teach.network.*;

import java.util.*;

/**
 * Verwaltung von Ausführungseinheiten. Eine Ausführungseinheit sind Projekte,
 * Programme und und in Ausführung befindliche Routinen.
 * 
 */
public class KExecAdministrator {
	/** Kein Debug - Modus */
	public static final int EXECUTION_MODE_OFF = TcExecutionState.EXECUTION_MODE_OFF;
	/**
	 * Ablauf - Debug - Modus, in diesem Modus sind alle Routinen des Ablaufs im
	 * Debug - Modus.
	 */
	public static final int EXECUTION_MODE_FLOW = TcExecutionState.EXECUTION_MODE_FLOW;
	/**
	 * Routien - Debug - Modus, in diesem Modus sind nur eizelne Routinen im
	 * Debug - Modus. Routinen die von einer in Debug - Modus befindlichen
	 * Routine entstehen sind automatisch im Debug - Modus. Jeder beliebigen
	 * Routinen eines Ablaufs kann der Debug - Modus gesetzt oder zurückgesetzt
	 * werden.
	 */
	public static final int EXECUTION_MODE_ROUTINE = TcExecutionState.EXECUTION_MODE_ROUTINE;

	/** Kein Debug - Zustand */
	public static final int STEP_OFF = TcExecutionState.STEP_OFF;
	/** Debug - Zustand angehalten */
	public static final int STEP_BREAK = TcExecutionState.STEP_BREAK;
	/** Debug - Zustand Routine verfolgen */
	public static final int STEP_INTO = TcExecutionState.STEP_INTO;
	/** Debug - Zustand Routine überspringen */
	public static final int STEP_OVER = TcExecutionState.STEP_OVER;
	/** Debug - Zustand Routine verlassen */
	public static final int STEP_OUT = TcExecutionState.STEP_OUT;
	/** Debug - Zustand Routine ausführen bis zur nächsten WAIT - Anweisung */
	public static final int STEP_GOTO_WAIT = TcExecutionState.STEP_GOTO_WAIT;
	/** Debug - Zustand Routine ausführen bis zum nächsten Breakpoint */
	public static final int STEP_GOTO_BREAKPOINT = TcExecutionState.STEP_GOTO_BREAKPOINT;
	/** sleep time for the poll thread */
	private final int SLEEP_TIME = 500;
	private final int SLEEP_MIN_TIME = 50;
	private boolean commandExecuted;
	private KExecUnitRoot m_root;
	/** poll thread */
	private final int LOW_PRIORITY = Thread.NORM_PRIORITY - 2;
	private final int HIGH_PRIORITY = Thread.NORM_PRIORITY;
	protected PollThread pollThread;
	/** execution model listener */
	private final Vector m_listenerList = new Vector(10);
	/** Alle Ausführungseinheiten */
	private final Hashtable m_executionUnits = new Hashtable(501);

	/** Alle Ausführungszustände, verwendet in checkStates und checkExecUnits */
	private final Vector m_states = new Vector();
	/** Alle Ausführungseinheiten, verwendet in checkStates */
	private final Vector m_execUnits = new Vector();
	/** Enthält alle entfernten Ausführungseinheiten */
	private final Vector m_toRemove = new Vector();
	/** Enthält alle hinzugefügten Ausführungseinheiten */
	private final Vector m_toInsert = new Vector();
	/**
	 * Enthält alle noch zu überprüfenden Ausführungseinheiten, wird in
	 * checkExecUnits verwendet.
	 */
	private final Vector m_toCheck = new Vector();

	private String globalFilter;
	private boolean filterChanged;

	KTcDfl dfl;

	protected KExecAdministrator(KTcDfl dfl) {
		this.dfl = dfl;
	}

	protected void init() {
		m_root = new KExecUnitRoot("TeachTalk", dfl.client.execution.getRoot(), dfl.structure.getRoot(), new TcExecutionState());

		pollThread = new PollThread();
		pollThread.setPriority(LOW_PRIORITY);
		pollThread.start();

		dfl.client.addConnectionListener(new TcConnectionListener() {
			public void connectionStateChanged(boolean isConnected) {
				if (isConnected) {
				} else {
					pollThread.stopPoll();
					synchronized (KExecAdministrator.this.dfl.getLockObject()) {
						m_toRemove.setSize(0);
						m_toInsert.setSize(0);
						removeKExecUnits(m_root, m_toRemove);
						if (m_root.m_routines != null) {
							m_root.m_routines.setSize(0);
						}
						if (m_root.m_projects != null) {
							m_root.m_projects.setSize(0);
						}
						m_executionUnits.clear();
						m_states.setSize(0);
						m_execUnits.setSize(0);
						TcExecutionState state = m_root.m_state;
						state.executionState = TcExecutionState.STATE_INVALID;
						state.steppingState = TcExecutionState.STEP_OFF;
						fireExecUnitsRemovedAdded(m_toRemove, m_toInsert);
						m_toRemove.setSize(0);
					}
				}
			}
		});

	}

	protected void stop() {
		pollThread.stopPoll();
	}

	/**
	 * Lädt das angegebene Projekt.
	 * 
	 * @param project
	 *            Projekt
	 */
	public void loadProject(KStructProject project) {
		dfl.client.execution.loadProject(project.getTcStructuralNode());
		commandExecuted();
	}

	/**
	 * Entlädt das angegebene Projekt.
	 * 
	 * @param project
	 *            Projekt
	 */
	public void unloadProject(KExecUnitProject project) {
		dfl.client.execution.unloadProject(project.getTcExecutionUnit());
		commandExecuted();
	}

	/**
	 * Startet das angegebene Programm, d.h. die namenlose Routine des Programms
	 * wird in Ausführung gebracht.
	 * 
	 * @param program
	 *            Programm
	 * @return returns true when the program was successfully started
	 */
	public boolean startProgram(KStructProgram program) {
		TcExecutionUnit eu = dfl.client.execution.startProgram(program.getTcStructuralNode());
		if (eu != null) {
			// program successfully started
			synchronized (dfl.getLockObject()) {
				checkExecUnits();
				fireUpdateState();
			}
		}
		return eu != null;
	}

	/**
	 * Starts the unnamed routine of the program.
	 * 
	 * @param program
	 *            program to start
	 * @param interrupt
	 *            if true the unnamed routine will be stopped at the first
	 *            statement
	 * @param restart
	 *            if true the unnamed routine will be automatically restarted
	 * @return true if successfully started
	 */
	public boolean startProgram(KStructProgram program, boolean interrupt, boolean restart) {
		TcExecutionUnit eu = dfl.client.execution.startProgram(program.getTcStructuralNode(), interrupt, restart);
		if (eu != null) {
			// program successfully started
			synchronized (dfl.getLockObject()) {
				checkExecUnits();
				fireUpdateState();
			}
		}
		return eu != null;
	}

	/**
	 * Unterbricht den angegebenen Ablauf.
	 * 
	 * @param execUnit
	 *            Wurzel des Ablaufes
	 */
	public void interruptExecutionUnit(KExecUnitNode execUnit) {
		dfl.client.execution.interruptExeUnit(execUnit.getTcExecutionUnit());
		commandExecuted();
	}

	/**
	 * Setzt den angegebenen Ablauf fort.
	 * 
	 * @param execUnit
	 *            Wurzel des Ablaufes
	 */
	public void continueExecutionUnit(KExecUnitNode execUnit) {
		dfl.client.execution.continueExeUnit(execUnit.getTcExecutionUnit());
		commandExecuted();
	}

	/**
	 * Beendet den angegeben Ablauf.
	 * 
	 * @param execUnit
	 *            Wurzel des Ablaufes
	 */
	public void stopExecutionUnit(KExecUnitNode execUnit) {
		dfl.client.execution.stopExeUnit(execUnit.getTcExecutionUnit());
		commandExecuted();
	}

	/**
	 * Führt einen Schritt auf die angegebenen Routine aus. Die Routine muss im
	 * Debug - Modus sein.
	 * 
	 * @param execUnit
	 *            Routine
	 * @param stepKind
	 *            Schrittart (STEP, STEP_OVER, ...)
	 */
	public void step(KExecUnitRoutine execUnit, int stepKind) {
		dfl.client.execution.step(execUnit.getTcExecutionUnit(), stepKind);
		commandExecuted();
	}

	/**
	 * Führt einen Schritt auf die angegebenen Routinen aus. Die Routinen müssen
	 * im Debug - Modus sein.
	 * 
	 * @param execUnits
	 *            Routinen
	 * @param stepKind
	 *            Schrittart (STEP, STEP_OVER, ...)
	 */
	public void step(KExecUnitRoutine[] execUnits, int stepKind) {
		Vector eus = new Vector();
		for (int i = 0; i < execUnits.length; i++) {
			KExecUnitRoutine eur = execUnits[i];
			if (eur != null) {
				eus.addElement(eur.getTcExecutionUnit());
			}
		}
		if (0 < eus.size()) {
			dfl.client.execution.step(eus, stepKind);
			commandExecuted();
		}
	}

	/**
	 * Sets the execution mode.
	 * 
	 * @param execUnit
	 *            execution unit
	 * @param mode
	 *            {EXECUTION_MODE_FLOW, EXECUTION_MODE_ROUTINE,
	 *            EXECUTION_MODE_OFF}
	 */
	public void setExecutionMode(KExecUnitNode execUnit, int mode) {
		dfl.client.execution.setExecutionMode(execUnit.getTcExecutionUnit(), mode);
		commandExecuted();
	}

	/**
	 * Switchs the main flow stepping on or off
	 * 
	 * @param routine
	 *            root of the execution flow
	 * @param enable
	 *            true for on and false for off
	 */
	public void setMainFlowStepping(KExecUnitRoutine routine, boolean enable) {
		dfl.client.execution.setMainFlowStepping(routine.getTcExecutionUnit(), enable);
		commandExecuted();
	}

	/**
	 * Setzt den Ausführungszeiger der Routine auf die angegebene Zeilennummer.
	 * 
	 * @param execUnit
	 *            Routine
	 * @param line
	 *            Zeilennummer
	 */
	public void setInstructionPointer(KExecUnitRoutine execUnit, int line) {
		dfl.client.execution.setInstructionPointer(execUnit.getTcExecutionUnit(), line);
		commandExecuted();
	}

	/**
	 * Sets the filter for multi kinematic,
	 * 
	 * @param filter
	 *            kinematic directory
	 */
	public void setGlobalFilter(String filter) {
//		synchronized (dfl.getLockObject()) {
			if (filter != null) {
				globalFilter = filter.toLowerCase();
			} else {
				globalFilter = null;
			}
			filterChanged = true;
//		}
	}

	/**
	 * Liefert die Wurzel aller Ausführungseinheiten.
	 * 
	 * @return Wurzel
	 */
	public KExecUnitRoot getRoot() {
		return m_root;
	}

	/**
	 * Executes the given routine.
	 * 
	 * @param routine
	 *            routine which should be executed
	 * @param variable
	 *            variable instance or null if the routine is program routine
	 * @param parameter
	 *            parameter of the routine (KStructVarWrapper, Boolean, Byte,
	 *            Short, Integer, Long, Float or String)
	 * 
	 * @return returns the routine return value if it was successfully executed
	 *         otherwise null. If the routine does not have a return value
	 *         NoReturnValue will be retruned
	 */
	public Object executeRoutine(KStructRoutine routine, KStructVarWrapper variable, Object[] parameter) {
		return executeRoutine(routine, (TcAccessHandle)((variable != null) ? variable.getAccessHandle() : null), parameter);
	}
	
	public Object executeRoutineFromVariable(KStructRoutine routine, String variablePath, Object[] parameter) {
		TcAccessHandle handle = dfl.client.structure.getVarAccessHandle(variablePath);
	   	if (handle == null) {
	   		if (variablePath.startsWith("_system.")) {
	   			variablePath = variablePath.substring(8);
		   		handle = dfl.client.structure.getVarAccessHandle(variablePath);
	   		} else {
	   			variablePath = "_system." + variablePath;
		   		handle = dfl.client.structure.getVarAccessHandle(variablePath);
	   		}
	   	}
		return executeRoutine(routine,handle , parameter);
	}
	
	public Object executeRoutine(KStructRoutine routine, TcAccessHandle varAccessHandle, Object[] parameter) {
		Object[] p = new Object[parameter.length];
		for (int i = 0; i < parameter.length; i++) {
			if (parameter[i] instanceof KStructVarWrapper) {
				p[i] = ((KStructVarWrapper) parameter[i]).getAccessHandle();
			} else if (parameter[i] != null) {
				p[i] = parameter[i];
			} else{
				p[i] = dfl.client.structure.getVoidAccessHandle();
			}
		}
		// call teaching method
		Object done = dfl.client.execution.executeRoutine((TcStructuralRoutineNode) routine.getTcStructuralNode(), varAccessHandle, p);
		return done;
	}

	public Object executeRoutine(KStructRoutine scope, String routineName, Object[] parameter) {
		int major = dfl.client.getMajorVersion();
		if ((major > 3) || ((major == 3) &&  (dfl.client.getMinorVersion()>= 16))){
			Object[] p = new Object[parameter.length];
			for (int i = 0; i < parameter.length; i++) {
				if (parameter[i] instanceof KStructVarWrapper) {
					p[i] = ((KStructVarWrapper) parameter[i]).getAccessHandle();
				} else if (parameter[i] != null) {
					p[i] = parameter[i];
				} else{
					p[i] = dfl.client.structure.getVoidAccessHandle();
				}
			}
			TcStructuralRoutineNode routnode = scope.getTeachPendant();
			TcAccessHandle huenchen = null;
			if (routineName != null){
				huenchen = dfl.client.structure.getVarAccessHandle(routineName);
			}else{
				huenchen = dfl.client.structure.getVarAccessHandle(scope.getDeclarationPath());
			}
			return dfl.client.execution.executeRoutine(routnode, huenchen, p);
		}
		return null;
	}

	/**
	 * Fügt einen KExecAdministratorListener - Listener hinzu.
	 * 
	 * @param listener
	 *            Listener
	 */
	public void addListener(KExecAdministratorListener listener) {
		m_listenerList.addElement(listener);
	}

	/**
	 * Entfernt den KExecAdministratorListener - Listener
	 * 
	 * @param listener
	 *            Listener
	 */
	public void removeListener(KExecAdministratorListener listener) {
		m_listenerList.removeElement(listener);
	}

	/**
	 * Liefert die Ausführungseinheit für den angegeben Namen.
	 * 
	 * @param parent
	 *            Elternknoten
	 * @param name
	 *            Name der Ausführungseinheit
	 * 
	 * @return Ausführungseinheit
	 */
	public KExecUnitNode getKExecUnitNode(KExecUnitNode parent, String name) {
		KExecUnitNode res = null;
		if (parent instanceof KExecUnitScope) {
			res = checkName(((KExecUnitScope) parent).m_projects, name, true);
		}
		if ((res == null) && (parent instanceof KExecUnitProject)) {
			res = checkName(((KExecUnitProject) parent).m_routines, name, false);
		}
		if ((res == null) && (parent instanceof KExecUnitRoutine)) {
			res = checkName(((KExecUnitRoutine) parent).m_routines, name, false);
		}
		return res;
	}

	private KExecUnitNode checkName(Vector children, String name, boolean ignoreCase) {
		if (children == null) {
			return null;
		}
		for (int i = 0; i < children.size(); i++) {
			KExecUnitNode child = (KExecUnitNode) children.elementAt(i);
			if (ignoreCase) {
				if (child.getName().equalsIgnoreCase(name)) {
					return child;
				}
			} else {
				if (child.getName().equals(name)) {
					return child;
				}
			}
		}
		return null;
	}

	private void fireExecUnitsRemovedAdded(Vector toRemove, Vector toInsert) {
		int priority = Thread.currentThread().getPriority();
		Thread.currentThread().setPriority(HIGH_PRIORITY);
		for (int i = 0; i < m_listenerList.size(); i++) {
			try {
				KExecAdministratorListener l = (KExecAdministratorListener) m_listenerList.elementAt(i);
				l.execUnitsRemovedAdded(toRemove, toInsert);
			} catch (Exception e) {
				KDflLogger.error(KExecAdministrator.class, "fireExecUnitsRemovedAdded", e);
			}
		}
		Thread.currentThread().setPriority(priority);
	}

	private void fireUpdateState() {
		int priority = Thread.currentThread().getPriority();
		Thread.currentThread().setPriority(HIGH_PRIORITY);
		for (int i = 0; i < m_listenerList.size(); i++) {
			try {
				KExecAdministratorListener l = (KExecAdministratorListener) m_listenerList.elementAt(i);
				l.updateState();
			} catch (Exception e) {
				KDflLogger.error(KExecAdministrator.class, "fireUpdateState", e);
			}
		}
		Thread.currentThread().setPriority(priority);
	}

	/**
	 * Erzwingt das Auslesen der KExecUnitNodes.
	 */
	private void commandExecuted() {
		if (pollThread != null) {
			commandExecuted = true;
			pollThread.interrupt();
		}
	}

	private void checkExecUnits() {
		m_toRemove.setSize(0);
		m_toInsert.setSize(0);
		m_toCheck.setSize(0);
		m_states.setSize(0);
		m_execUnits.setSize(0);
		dfl.client.execution.getState(m_root.getTcExecutionUnit(), m_root.m_state);
		if (m_root.m_state.executionState == TcExecutionState.STATE_INVALID) {
			removeKExecUnits(m_root, m_toRemove);
			m_executionUnits.clear();
		} else if ((m_root.m_state.executionState != TcExecutionState.STATE_INVALID) && !m_executionUnits.containsKey(m_root.getTcExecutionUnit())) {
			m_execUnits.addElement(m_root.getTcExecutionUnit());
			dfl.client.execution.setExecUnitInfos(m_execUnits);
			KExecUnitNode scope = createNode(m_root, m_root.getTcExecutionUnit(), new TcExecutionState());
			if (scope != null) {
				m_executionUnits.put(m_root.getTcExecutionUnit(), scope);
				m_toInsert.addElement(scope);
			}
		}
		checkStates(m_root);
		// remove all invalid execUnits and add to check execUnits for new nodes
		checkExecUnitNodes(m_root, m_toRemove, m_toCheck);
		filterChanged = false;
		while (0 < m_toCheck.size()) {
			// there are execUnits to check
			m_execUnits.setSize(0);
			m_states.setSize(0);
			Enumeration e = dfl.client.execution.getExecutionUnits(m_toCheck, TcExecutionModel.FILTER_ALL, false);
			while (e.hasMoreElements()) {
				TcExecutionUnit executionUnit = (TcExecutionUnit) e.nextElement();
				if (!m_executionUnits.containsKey(executionUnit)) {
					m_execUnits.addElement(executionUnit);
					m_states.addElement(new TcExecutionState());
				}
			}
			m_toCheck.setSize(0);
			if (0 < m_execUnits.size()) {
				dfl.client.execution.setExecUnitInfos(m_execUnits);
				dfl.client.execution.getStates(m_execUnits, m_states);
				int j = 0;
				while (j < m_execUnits.size()) {
					TcExecutionUnit eu = (TcExecutionUnit) m_execUnits.elementAt(j);
					TcExecutionState s = (TcExecutionState) m_states.elementAt(j);
					// add
					if (s.executionState != TcExecutionState.STATE_INVALID) {
						KExecUnitNode eun = (KExecUnitNode) m_executionUnits.get(eu);
						if (eun == null) {
							KExecUnitNode parent = (KExecUnitNode) m_executionUnits.get(eu.getParent());
							eun = createNode(parent, eu, s);
							if (eun != null) {
								m_executionUnits.put(eu, eun);
								m_toInsert.addElement(eun);
								if (0 < eun.m_state.childCount) {
									m_toCheck.addElement(eu);
								}
							}
						}
					}
					j++;
				}
			}
		}
		int major = dfl.client.getMajorVersion();
		int minor = dfl.client.getMinorVersion();
		if (!((2 < major) || ((2 == major) && (84 <= minor)))) {
			// check projects for loading or unloading
			checkLoadingState(m_root);
		}

		if ((m_toRemove.size() > 0) || (m_toInsert.size() > 0)) {
			fireExecUnitsRemovedAdded(m_toRemove, m_toInsert);
		}
	}

	private static void checkLoadingState(KExecUnitProject parent) {
		boolean isLoading = false;
		boolean isUnloading = false;
		for (int i = 0; i < parent.getExecUnitRoutineCount(); i++) {
			KExecUnitRoutine r = parent.getExecUnitRoutine(i);
			if (r.getKind() == KExecUnitRoutine.NEW_ROUTINE) {
				if (r.m_state.executionState != KExecUnitNode.STATE_FINISHED) {
					isLoading = true;
					break;
				}
			} else if (r.getKind() == KExecUnitRoutine.DELETE_ROUTINE) {
				if (r.m_state.executionState != KExecUnitNode.STATE_FINISHED) {
					isUnloading = true;
					break;
				}
			}
		}
		if (isLoading) {
			parent.m_state.executionState = KExecUnitNode.STATE_PROJECT_LOADING;
		} else if (isUnloading) {
			parent.m_state.executionState = KExecUnitNode.STATE_PROJECT_UNLOADING;
		} else {
			parent.m_state.executionState = KExecUnitNode.STATE_RUNNING;
		}
		if (parent instanceof KExecUnitGlobal) {
			for (int i = 0; i < ((KExecUnitGlobal) parent).getExecUnitProjectCount(); i++) {
				checkLoadingState(((KExecUnitGlobal) parent).getExecUnitProject(i));
			}
		}
	}

	private KExecUnitNode createNode(KExecUnitNode parent, TcExecutionUnit eu, TcExecutionState state) {
		TcStructuralNode structuralNode = eu.getTcStructuralNode();
		KExecUnitNode eun = null;
		if ((structuralNode != null) && (parent != null)) {
			KStructNode n = null;
			switch (eu.getKind()) {
			case TcExecutionUnit.GLOBAL:
				if ((globalFilter != null) && !globalFilter.equals(structuralNode.getName().toLowerCase())) {
					// skip global project
					return null;
				}
			case TcExecutionUnit.SYSTEM:
			case TcExecutionUnit.PROJECT:
				n = dfl.structure.getKStructProject(structuralNode);
				if (n != null) {
					switch (eu.getKind()) {
					case TcExecutionUnit.SYSTEM:
						eun = new KExecUnitSystem(n.getKey(), eu, n, state, parent);
						break;
					case TcExecutionUnit.GLOBAL:
						eun = new KExecUnitGlobal(n.getKey(), eu, n, state, parent);
						break;
					default:
						eun = new KExecUnitProject(n.getKey(), eu, n, state, parent);
					}
					if (((KExecUnitScope) parent).m_projects == null) {
						((KExecUnitScope) parent).m_projects = new Vector(2);
					}
					((KExecUnitScope) parent).m_projects.addElement(eun);
				} else {
					// structural node isn't loaded yet
					parent.m_state.setChangeFlag();
				}
				break;
			case TcExecutionUnit.ROUTINE:
				n = dfl.structure.getKStructRoutine((TcStructuralRoutineNode) structuralNode);
				if (n != null) {
					eun = new KExecUnitRoutine(n.getKey(), eu, n, state, parent);
					if (parent instanceof KExecUnitProject) {
						if (((KExecUnitProject) parent).m_routines == null) {
							((KExecUnitProject) parent).m_routines = new Vector(20);
						}
						((KExecUnitProject) parent).m_routines.addElement(eun);
					} else if (parent instanceof KExecUnitRoutine) {
						if (((KExecUnitRoutine) parent).m_routines == null) {
							((KExecUnitRoutine) parent).m_routines = new Vector(20);
						}
						((KExecUnitRoutine) parent).m_routines.addElement(eun);
					}
				} else {
					// structural node isn't loaded yet
					parent.m_state.setChangeFlag();
				}
				break;
			}
		} else if ((eu.getKind() == TcExecutionUnit.ROUTINE) && (parent != null)) {
			int index = eu.getCallPath().lastIndexOf('.');
			String name = null;
			if (index > 0) {
				name = eu.getCallPath().substring(index + 1);
			} else {
				name = eu.getCallPath();
			}
			eun = new KExecUnitRoutine(name, eu, null, state, parent);
			if (parent instanceof KExecUnitProject) {
				if (((KExecUnitProject) parent).m_routines == null) {
					((KExecUnitProject) parent).m_routines = new Vector(20);
				}
				((KExecUnitProject) parent).m_routines.addElement(eun);
			} else if (parent instanceof KExecUnitRoutine) {
				if (((KExecUnitRoutine) parent).m_routines == null) {
					((KExecUnitRoutine) parent).m_routines = new Vector(20);
				}
				((KExecUnitRoutine) parent).m_routines.addElement(eun);
			}
		}
		return eun;
	}

	private void checkExecUnitNodes(KExecUnitNode parent, Vector toRemove, Vector toCheck) {
		if (parent instanceof KExecUnitScope) {
			checkExecUnitNodes(((KExecUnitScope) parent).m_projects, toRemove, toCheck);
		}
		if (parent instanceof KExecUnitProject) {
			checkExecUnitNodes(((KExecUnitProject) parent).m_routines, toRemove, toCheck);
		} else if (parent instanceof KExecUnitRoutine) {
			checkExecUnitNodes(((KExecUnitRoutine) parent).m_routines, toRemove, toCheck);
		}
	}

	private void checkExecUnitNodes(Vector children, Vector toRemove, Vector toCheck) {
		if (children != null) {
			int i = children.size() - 1;
			while (0 <= i) {
				KExecUnitNode eun = (KExecUnitNode) children.elementAt(i);
				if ((eun.m_state.executionState == TcExecutionState.STATE_INVALID) || (filterChanged && (eun instanceof KExecUnitGlobal))) {
					// remove
					children.removeElement(eun);
					toRemove.addElement(eun);
					m_executionUnits.remove(eun.getTcExecutionUnit());
					removeKExecUnits(eun, toRemove);
				} else {
					if (eun.m_state.hasChanged() && (0 < eun.m_state.childCount)) {
						toCheck.addElement(eun.getTcExecutionUnit());
					}
					eun.m_state.resetChangeFlag();
					checkExecUnitNodes(eun, toRemove, toCheck);
				}
				i--;
			}
		}
	}

	private void removeKExecUnits(KExecUnitNode eun, Vector toRemove) {
		// remove children
		if (eun instanceof KExecUnitRoutine) {
			removeKExecUnits(((KExecUnitRoutine) eun).m_routines, toRemove);
		}
		if (eun instanceof KExecUnitProject) {
			removeKExecUnits(((KExecUnitProject) eun).m_routines, toRemove);
		}
		if (eun instanceof KExecUnitScope) {
			removeKExecUnits(((KExecUnitScope) eun).m_projects, toRemove);
		}
	}

	private void removeKExecUnits(Vector children, Vector toRemove) {
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				KExecUnitNode eun = (KExecUnitNode) children.elementAt(i);
				m_executionUnits.remove(eun.getTcExecutionUnit());
				toRemove.addElement(eun);
				removeKExecUnits(eun, toRemove);
			}
			children.setSize(0);
		}
	}

	private void checkStates(KExecUnitNode root) {
		m_execUnits.setSize(0);
		m_states.setSize(0);
		addState(root, m_execUnits, m_states);
		if (0 < m_execUnits.size()) {
			dfl.client.execution.getStates(m_execUnits, m_states);
		}
	}

	private void addState(KExecUnitNode parent, Vector execUnits, Vector states) {
		if (parent instanceof KExecUnitScope) {
			addState(((KExecUnitScope) parent).m_projects, execUnits, states);
		}
		if (parent instanceof KExecUnitProject) {
			addState(((KExecUnitProject) parent).m_routines, execUnits, states);
		} else if (parent instanceof KExecUnitRoutine) {
			addState(((KExecUnitRoutine) parent).m_routines, execUnits, states);
		}
	}

	private void addState(Vector children, Vector execUnits, Vector states) {
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				KExecUnitNode eun = (KExecUnitNode) children.elementAt(i);
				execUnits.addElement(eun.getTcExecutionUnit());
				states.addElement(eun.m_state);
				addState(eun, execUnits, states);
			}
		}
	}

	private class PollThread extends Thread {
		/** poll flag, true if states and executions should be checked */
		private boolean m_bDoPoll = true;
		private long sleep;
		private long t0;
		private long difference;

		private PollThread() {
			super("KExecAdministrator");
		}

		public void run() {
			try {
				Thread.sleep(300);
			} catch (InterruptedException ie) {
			}
			while (m_bDoPoll) {
				try {
					t0 = System.currentTimeMillis();
					try {
						synchronized (dfl.getLockObject()) {
							if (filterChanged) {
								KExecUnitNode n = getKExecUnitNode(getRoot(), "_system");
								if (n != null) {
									// recheck system node
									n.m_state.setChangeFlag();
								}
							}
							checkExecUnits();
							fireUpdateState();
						}
					} catch (Exception e) {
						KDflLogger.error(this, "KExecAdministrator.PollThread:", e);
					}
					difference = System.currentTimeMillis() - t0;
					if (SLEEP_TIME < difference) {
						sleep = SLEEP_MIN_TIME;
					} else if ((SLEEP_TIME - difference) < SLEEP_MIN_TIME) {
						sleep = SLEEP_MIN_TIME;
					} else {
						sleep = SLEEP_TIME - difference;
					}
					if (commandExecuted) {
						sleep = SLEEP_MIN_TIME;
						commandExecuted = false;
					}
					Thread.sleep(sleep);
				} catch (InterruptedException ie) {
					// command was executed
					commandExecuted = false;
				}
			}
		}

		private void stopPoll() {
			m_bDoPoll = false;
			interrupt();
		}
	}
}
