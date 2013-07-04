/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Projekt   : KEMRO.teachview.4
 *    Auftragsnr: 5500395
 *    Erstautor : ede
 *    Datum     : 01.04.2003
 *--------------------------------------------------------------------------
 *      Revision:
 *        Author:
 *          Date:
 *------------------------------------------------------------------------*/
package com.keba.kemro.kvs.teach.data.project;

import java.util.Date;

import com.keba.kemro.teach.dfl.dir.KDirEntry;
import com.keba.kemro.teach.dfl.execution.KExecUnitNode;
import com.keba.kemro.teach.dfl.execution.KExecUnitRoutine;
import com.keba.kemro.teach.dfl.structural.KStructProgram;

/**
 * Repräsentation eines Programmes
 */
public class KvtProgram {
	public static final String	UNNAMED_ROUTINE_NAME	= "*";
	/** The program isn't builded (not in struct- and execution-tree). */
	public static final int		NOT_USED				= 0;
	/**
	 * The program is opened (only in struct-tree; project not in execution-
	 * tree).
	 */
	public static final int		OPEN					= 1;
	/** The program is loaded (prog in struct-tree; project in execution-tree). */
	public static final int		LOADED					= 2;
	/** The program is interrupted. */
	public static final int		STOPPED					= 3;
	/** The program is waiting. */
	public static final int		WAITING					= 4;
	/** The program is stepping. */
	public static final int		STEPPING				= 5;
	/** The program is running. */
	public static final int		RUNNING					= 6;
	/** The program is finished. */
	public final static int		FINISHED				= 7;

	private KDirEntry			m_programDirEntry;
	private KStructProgram		m_programStruct;
	private KExecUnitRoutine	m_unnamedRoutine;
	private int					m_state;						// state of the
																// program:
	private KvtProject			m_parent;

	/**
	 * creates new KvtProgram object
	 * 
	 * @param dirEntry
	 *            directory entry describing program
	 * @param project
	 *            project for this program
	 * @param program
	 *            program to be represented by this object
	 * @param routine
	 *            unnamed routine
	 */
	KvtProgram(KDirEntry dirEntry, KvtProject project, KStructProgram program, KExecUnitRoutine routine) {
		m_programDirEntry = dirEntry;
		m_programStruct = program;
		m_unnamedRoutine = routine;
		m_parent = project;
		checkProgramState(false);
	}

	/**
	 * Liefert das Erzeugungsdatum
	 * 
	 * @return Das Erzeugungsdatum
	 */
	public Date getProgramCreationDate() {
		return m_programDirEntry.getCreationDate();
	}

	/**
	 * @return
	 */
	public String getProgramStateString() {
		switch (m_state) {
		case 1:
			return "open";
		case 2:
			return "loaded";
		case 3:
			return "stopped";
		case 4:
			return "waiting";
		case 5:
			return "stepping";
		case 6:
			return "finished";
		default:
			return "unknown";
		}
	}

	/**
	 * Liefert das Änderungsdatum
	 * 
	 * @return Das Änderungsdatum
	 */
	public Date getProgramModificationDate() {
		return m_programDirEntry.getModifiedDate();
	}

	/**
	 * Liefert die Größe des Programms
	 * 
	 * @return Größe des Programms
	 */
	public int getProgramSize() {
		return m_programDirEntry.getSize();
	}

	/**
	 * Liefert den Status des Programmes
	 * 
	 * @return Status des Programmes
	 */
	public int getProgramState() {
		return m_state;
	}

	/**
	 * Liefert das Projekt in dem sich das Program befindet.
	 * 
	 * @return Das Projekt in dem sich das Program befindet.
	 */
	public KvtProject getParent() {
		return m_parent;
	}

	/**
	 * get program name. This name has to be unique inside of one project.
	 * Always use case insensitive operation on this name !!!
	 * 
	 * @return program name (without extension).
	 */
	public String getName() {
		return getDirEntry().getName();
	}

	/**
	 * Setzt einen neuen Programmzustand
	 * 
	 * @param newState
	 *            Neuer Programmzustand
	 */
	protected void setProgramState(int newState) {
		m_state = NOT_USED;
		if ((newState >= NOT_USED) && (newState <= FINISHED)) {
			m_state = newState;
		}
	}

	/**
	 * Überprüft den aktuellen Programmstatus. Wenn doFire und sich der Status
	 * geändert hat wird ein Prgramstate Changed ausgelöst.
	 * 
	 * @param doFire
	 *            Wenn true werden Listener benachrichtigt.
	 */
	protected void checkProgramState(boolean doFire) {
		int prgStateOld;
		int prgStateNew;

		prgStateOld = getProgramState();
		if (this.m_programStruct == null) {
			prgStateNew = NOT_USED;
		} else {
			int prgState;

			prgStateNew = OPEN;
			KvtProject prj = getParent();
			if (prj != null) {
				if (prj.getProjectState() == KvtProject.SUCCESSFULLY_LOADED) {
					prgStateNew = LOADED;
				}
				if (m_unnamedRoutine != null) {
					prgState = m_unnamedRoutine.getExecutionState();
					switch (prgState) {
					case KExecUnitNode.STATE_WAITING:
						prgStateNew = WAITING;
						break;
					case KExecUnitNode.STATE_RUNNING:
						prgStateNew = RUNNING;
						break;
					case KExecUnitNode.STATE_STEPPING:
						prgStateNew = STEPPING;
						break;
					case KExecUnitNode.STATE_FINISHED:
						prgStateNew = FINISHED;
						break;
					case KExecUnitNode.STATE_INTERRUPTED:
						prgStateNew = STOPPED;
						break;
					case KExecUnitNode.STATE_INVALID:
						prgStateNew = OPEN;
						if (prj.getProjectState() == KvtProject.SUCCESSFULLY_LOADED) {
							prgStateNew = LOADED;
						}
						break;
					}
				}
			} else {
				prgStateNew = NOT_USED;
			}
		}
		if (prgStateOld != prgStateNew) {
			setProgramState(prgStateNew);
			if (doFire) {
				KvtProjectAdministrator.fireProgramStateChanged(this);
			}
		}
	}

	/**
	 * Liefert den KDirEntry des Programms
	 * 
	 * @return Den KDirEntry
	 */
	public KDirEntry getDirEntry() {
		return m_programDirEntry;
	}

	/**
	 * Liefert den KStructProgram des Programms
	 * 
	 * @return Den KStructProgram des Programms
	 */
	public KStructProgram getStructProgram() {
		return m_programStruct;
	}

	/**
	 * Setzt den KStructProgram.
	 * 
	 * @param newPrg
	 *            Der KStructProgram Knoten
	 */
	protected void setStructProgram(KStructProgram newPrg) {
		m_programStruct = newPrg;
		if (m_programStruct == null) {
			m_unnamedRoutine = null;
		}
		this.checkProgramState(true);
	}

	/**
	 * Setzt die Ausführungseinheit für die unbenannte Routine
	 * 
	 * @param execUnnamedRoutine
	 *            Ausführungseinheit für die unbenannte Routine
	 */
	protected void setUnnamedExecUnitRoutine(KExecUnitRoutine execUnnamedRoutine) {
		m_unnamedRoutine = execUnnamedRoutine;
		this.checkProgramState(true);
	}

	/**
	 * Liefert die Ausführungseinheit für die unbenannte Routine
	 * 
	 * @return Ausführungseinheit für die unbenannte Routine
	 */
	public KExecUnitRoutine getUnnamedExecUnitRoutine() {
		return m_unnamedRoutine;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getDirEntry().getName();
	}

}
