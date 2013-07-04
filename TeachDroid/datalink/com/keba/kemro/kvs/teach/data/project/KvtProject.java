/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Project : KEMRO.teachview.4
 *------------------------------------------------------------------------*/
package com.keba.kemro.kvs.teach.data.project;

import java.util.Date;
import java.util.Vector;

import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.dir.KDirEntry;
import com.keba.kemro.teach.dfl.execution.KExecUnitNode;
import com.keba.kemro.teach.dfl.execution.KExecUnitProject;
import com.keba.kemro.teach.dfl.execution.KExecUnitRoutine;
import com.keba.kemro.teach.dfl.structural.KStructNode;
import com.keba.kemro.teach.dfl.structural.KStructNodeVector;
import com.keba.kemro.teach.dfl.structural.KStructProgram;
import com.keba.kemro.teach.dfl.structural.KStructProject;
import com.keba.kemro.teach.dfl.structural.routine.KStructRoutine;

/**
 * Representation eines Projektes
 */
public class KvtProject {
	public final static int NOT_BUILDED = 0;
	public final static int BUILDED_WITH_ERROR = 1;
	public final static int BUILDED_WITHOUT_ERROR = 2;
	public final static int NOT_ALREADY_LOADED = 3;
	public final static int SUCCESSFULLY_LOADED = 4;
	public final static int NOT_ALREADY_UNLOADED = 5;

	KDirEntry dirEntry;
	KStructProject structProject;
	KExecUnitProject execUnitProject;
	private int state;

	private Vector programs;
	private KTcDfl dfl;

	/**
	 * Legt ein neues KProjekt an
	 * 
	 *@param dirEntry
	 *            Der KDirEntry für das Projekt
	 */
	KvtProject(KDirEntry dirEntry, KStructProject structProject, KExecUnitProject execUnitProject, KTcDfl dfl) {
		this.dirEntry = dirEntry;
		this.structProject = structProject;
		this.execUnitProject = execUnitProject;
		this.dfl = dfl;
		checkProjectState(false);
	}

	/**
	 * get number of programs in this project
	 * 
	 * @return number of programs
	 */
	public int getProgramCount() {
		synchronized (dfl.getLockObject()) {
			if (programs == null) {
				loadPrograms();
			}
			return programs.size();
		}
	}

	/**
	 * get project name. This name has to be unique. Always use case insensitive
	 * operation on this name !!!
	 * 
	 * @return project name (without extension).
	 */
	public String getName() {
		return this.getDirEntry().getName();
	}

	/**
	 * get program at index
	 * 
	 * @param index
	 *            index of program
	 * @return KvtProgram node
	 */
	public KvtProgram getProgram(int index) {
		synchronized (dfl.getLockObject()) {
			if (programs == null) {
				loadPrograms();
			}
			return ((0 <= index) && (index < programs.size())) ? (KvtProgram) programs.elementAt(index) : null;
		}
	}

	/**
	 * Returns the program with the given name.
	 * 
	 * @param name
	 *            program name
	 * @return program
	 */
	public KvtProgram getProgram(String name) {
		synchronized (dfl.getLockObject()) {
			if (programs == null) {
				loadPrograms();
			}
			for (int i = 0; i < programs.size(); i++) {
				KvtProgram prog = (KvtProgram) programs.elementAt(i);
				if (prog.toString().equalsIgnoreCase(name)) {
					return prog;
				}
			}
		}
		return null;
	}

	private void loadPrograms() {
		Vector entries = dfl.directory.getAllPrograms(dirEntry);
		programs = new Vector();
		if (entries != null) {
			for (int i = 0; i < entries.size(); i++) {
				KDirEntry entry = (KDirEntry) entries.elementAt(i);
				KStructProgram sp = null;
				KExecUnitRoutine routine = null;
				String name = entry.getName();
				if (structProject != null) {
					sp = checkProgram(structProject.programs, name);
					if ((sp != null) && (execUnitProject != null)) {
						routine = checkRoutine(sp, execUnitProject);
					}
				}
				KvtProgram prg = new KvtProgram(entry, this, sp, routine);
				programs.addElement(prg);
			}
		}
	}

	protected boolean isLoaded() {
		synchronized (dfl.getLockObject()) {
			return (programs != null);
		}
	}

	/**
	 * @return
	 */
	public String getProjectStateString() {
		switch (state) {
		case NOT_BUILDED:
			return "not built";
		case NOT_ALREADY_LOADED:
			return "loading";
		case BUILDED_WITHOUT_ERROR:
			return "built";
		case BUILDED_WITH_ERROR:
			return "build error";
		case SUCCESSFULLY_LOADED:
			return "loaded";
		case NOT_ALREADY_UNLOADED:
			return "unloading";
		default:
			return "unknown";
		}
	}

	private static KStructProgram checkProgram(KStructNodeVector programs, String key) {
		for (int i = 0; i < programs.getChildCount(); i++) {
			KStructNode en = programs.getChild(i);
			if (en.getKey().equalsIgnoreCase(key)) {
				return (KStructProgram) en;
			}
		}
		return null;
	}

	private static KExecUnitRoutine checkRoutine(KStructProgram program, KExecUnitProject project) {
		for (int i = 0; i < project.getExecUnitRoutineCount(); i++) {
			KExecUnitRoutine er = project.getExecUnitRoutine(i);
			KStructRoutine sr = (KStructRoutine) er.getKStructNode();
			if ((sr != null) && program.equals(sr.getParent()) && KvtProgram.UNNAMED_ROUTINE_NAME.equalsIgnoreCase(sr.getKey())) {
				return er;
			}
		}
		return null;
	}

	/**
	 * Überprüft den Projektstatus und benachrichtigt die Listener falls sich
	 * etwas geändert hat und doFire true ist.
	 * 
	 *@param doFire
	 *            Wenn true werden die Listener bei Änderungen benachrichtigt.
	 */
	protected void checkProjectState(boolean doFire) {
		int prjStateOld;
		int prjStateNew;

		prjStateOld = getProjectState();
		if (getExecUnitProject() != null) {
			KExecUnitProject eup = getExecUnitProject();
			if (eup.getExecutionState() == KExecUnitNode.STATE_PROJECT_LOADING) {
				prjStateNew = NOT_ALREADY_LOADED;
			} else if (eup.getExecutionState() == KExecUnitNode.STATE_PROJECT_UNLOADING) {
				prjStateNew = NOT_ALREADY_UNLOADED;
			} else {
				prjStateNew = SUCCESSFULLY_LOADED;
			}
		}
		// if exists projectExeUnitProject-Node
		else if (getStructProject() != null) {
			if (getStructProject().hasCompileError()) {
				prjStateNew = BUILDED_WITH_ERROR;
			} else {
				prjStateNew = BUILDED_WITHOUT_ERROR;
			}
		} else {
			prjStateNew = NOT_BUILDED;
		}
		if (prjStateOld != prjStateNew) {
			setProjectState(prjStateNew);
			if (doFire) {
				KvtProjectAdministrator.fireProjectStateChanged(this);
			}
		}
	}

	/**
	 * Liefert den KDirEntry für das Projekt
	 * 
	 *@return Den KDirEntry des Projekts
	 */
	public KDirEntry getDirEntry() {
		return dirEntry;
	}

	/**
	 * Liefert den KStructProject des Projekts
	 * 
	 *@return KStructProject
	 */
	public KStructProject getStructProject() {
		return structProject;
	}

	/**
	 * Setzt den KStructProject des Projekts
	 * 
	 *@param newPrj
	 *            KStructProject
	 */
	protected void setStructProject(KStructProject newPrj) {
		structProject = newPrj;
		if (structProject == null) {
			execUnitProject = null;
		}

		this.checkProjectState(true);
	}

	/**
	 * Setzt die Ausführungseinheit des Projekts
	 * 
	 *@param execUnit
	 *            Ausführungseinheit des Projekts
	 */
	protected void setExecUnitProject(KExecUnitProject execUnit) {
		execUnitProject = execUnit;
		this.checkProjectState(true);
	}

	/**
	 * Liefert die Ausführungseinheit des Projekts
	 * 
	 *@return Ausführungseinheit des Projekts
	 */
	public KExecUnitProject getExecUnitProject() {
		return execUnitProject;
	}

	/**
	 * Liefert true wenn das Projekt das Globale Projekt ist
	 * 
	 *@return True wenn das Projekt das Globale Projekt ist.
	 */
	public boolean isGlobalProject() {
		if (dirEntry == null) {
			return false;
		} else if (dirEntry != null) {
			return dirEntry.isGlobalProject();
		} else {
			return false;
		}
	}

	/**
	 * @return true if this is the system project
	 */
	public boolean isSystemProject() {
		if (dirEntry == null) {
			return false;
		} else if (dirEntry != null) {
			return dirEntry.isSystemProject();
		} else {
			return false;
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (dirEntry != null){
		   return this.getDirEntry().getName();
		}
		return "???";
	}

	/**
	 * Liefert den Projektstatus
	 * 
	 *@return Projektstatus
	 */
	public int getProjectState() {
		return state;
	}

	private void setProjectState(int newState) {
		state = NOT_BUILDED;
		if ((newState >= NOT_BUILDED) && (newState <= NOT_ALREADY_UNLOADED)) {
			state = newState;
		}
	}

	/**
	 * Liefert das Erstellungsdatum
	 * 
	 *@return Erstellungsdatum
	 */
	public Date getProjectCreationDate() {
		return dirEntry.getCreationDate();
	}

	/**
	 * Liefert das Änderungsdatum
	 * 
	 *@return Änderungsdatum
	 */
	public Date getProjectModificationDate() {
		return dirEntry.getModifiedDate();
	}

}
