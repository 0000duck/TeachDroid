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
package com.keba.kemro.kvs.teach.model;

import java.util.Enumeration;
import java.util.Vector;

import com.keba.kemro.kvs.teach.data.program.KvtStatementType;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.codepoint.KCodePointAdministrator;
import com.keba.kemro.teach.dfl.codepoint.KCodePointAdministrator.CodePoint;
import com.keba.kemro.teach.dfl.codepoint.KCodePointListener;
import com.keba.kemro.teach.dfl.edit.KEditKW;
import com.keba.kemro.teach.dfl.edit.KEditModelListener;
import com.keba.kemro.teach.dfl.edit.KEditor;
import com.keba.kemro.teach.dfl.structural.KStructProgram;
import com.keba.kemro.teach.dfl.structural.routine.KStructRoutine;
import com.keba.kemro.teach.network.TcEditorTextRange;

/**
 * Diese Klasse implementiert das Routinenmodell für das Anzeigeelement
 * KRoutineView. Sie liefert den Routinentext, den Hauptlaufzeiger, die
 * Breakpoints, Umrechnungsfunktion für Anzeigezeilennummer zu
 * Dateizeilennummer, CALL-Statement-Parameter und das KSymbol für eine
 * bestimmte Zeile.
 */
public class KvtRoutineModel implements KEditModelListener, KCodePointListener {
	private static int CORRECTION_OFFSET = 1;
	private final Vector listeners = new Vector(1);
	private final Vector breakpoints = new Vector(3);
	protected KStructRoutine routine;
	protected KEditor editAdmin;
	private int mainFlowPointer = -1;
	protected int begLine;
	protected int endLine;
	public static final String	EOF_KEY				= ">>>EOF<<<";		// Config.getStringProperty("EOF_text",
																		// ">>>EOF<<<");
	public static final String EDIT_START = "// START_EDIT";
	public static final String EDIT_END = "// END_EDIT";
	private String kairoVersion = null;

	public String getKAIROVersion() {
		return kairoVersion;
	}

	/**
	 * Konstruktor
	 */
	public KvtRoutineModel() {
		// do nothing
	}

	/**
	 * Fügt einen KRoutineModelListener hinzu.
	 * 
	 * @param l
	 *            KRoutineModelListener
	 */
	public void addRoutineModelListener(KvtRoutineModelListener l) {
		listeners.addElement(l);
	}

	/**
	 * Entfernt den KRoutineModelListener.
	 * 
	 * @param l
	 *            KRoutineModelListener
	 */
	public void removeRoutineModelListener(KvtRoutineModelListener l) {
		listeners.removeElement(l);
	}

	/**
	 * Gibt die Änderung der KStructRoutine bekannt.
	 */
	protected void fireRoutineChanged() {
		for (int i = listeners.size() - 1; 0 <= i; i--) {
			KvtRoutineModelListener l = (KvtRoutineModelListener) listeners.elementAt(i);
			l.routineChanged();
		}
	}

	/**
	 * Gibt die Änderung des Routineninhaltes bekannt.
	 */
	protected void fireRoutineModelChanged() {
		for (int i = listeners.size() - 1; 0 <= i; i--) {
			KvtRoutineModelListener l = (KvtRoutineModelListener) listeners.elementAt(i);
			l.modelChanged();
		}
	}

	/**
	 * Gibt die Änderung des Hauptlaufzeigers bekannt.
	 */
	protected void fireMainFlowPointerChanged() {
		for (int i = listeners.size() - 1; 0 <= i; i--) {
			KvtRoutineModelListener l = (KvtRoutineModelListener) listeners.elementAt(i);
			l.mainFlowPointerChanged();
		}
	}

	/**
	 * Gibt das Hinzufügen eines Breakpoints bekannt.
	 * 
	 * @param lineOfView
	 *            Anzeigezeilennummer
	 */
	protected void fireBreakpointAdded(int lineOfView) {
		for (int i = listeners.size() - 1; 0 <= i; i--) {
			KvtRoutineModelListener l = (KvtRoutineModelListener) listeners.elementAt(i);
			l.breakpointAdded(lineOfView);
		}
	}

	/**
	 * Gibt das Entfernen eines Breakpoints bekannt.
	 * 
	 * @param lineOfView
	 *            Anzeigezeilennummer
	 */
	protected void fireBreakpointRemoved(int lineOfView) {
		for (int i = listeners.size() - 1; 0 <= i; i--) {
			KvtRoutineModelListener l = (KvtRoutineModelListener) listeners.elementAt(i);
			l.breakpointRemoved(lineOfView);
		}
	}

	/**
	 * Gibt die Änderung des Breakpointzustandes bekannt.
	 * 
	 * @param lineOfView
	 *            Anzeigezeilennummer
	 */
	protected void fireBreakpointChanged(int lineOfView) {
		for (int i = listeners.size() - 1; 0 <= i; i--) {
			KvtRoutineModelListener l = (KvtRoutineModelListener) listeners.elementAt(i);
			l.breakpointChanged(lineOfView);
		}
	}

	/**
	 * Liefert die Zeilenanzahl der Routine zurück.
	 * 
	 * @return Zeilenanzahl
	 */
	public int getLineCount() {
		return (editAdmin != null) ? (endLine - begLine + 1) : 0;
	}

	/**
	 * Liefert den Text für die angegebene Zeile.
	 * 
	 * @param lineOfView
	 *            Anzeigezeilennummer
	 * 
	 * @return Anzeigezeilennummer
	 */
	public String getLine(int lineOfView) {
		return ((editAdmin != null) && (lineOfView < (endLine - begLine))) ? editAdmin.getLine(begLine + lineOfView) : EOF_KEY;
	}

	/**
	 * Konvertiert die angegebene Ausführungszeilennummer in die
	 * Anzeigezeilennummer.
	 * 
	 * @param lineOfExection
	 *            Ausführungszeilennummer
	 * 
	 * @return Anzeigezeilennummer
	 */
	public int getLineOfView(int lineOfExection) {
		return lineOfExection - CORRECTION_OFFSET - begLine;
	}

	/**
	 * Konvertiert die angegebene Anzeigezeilennummer in die
	 * Ausführungszeilennummer.
	 * 
	 * @param lineOfView
	 *            Anzeigezeilennummer
	 * 
	 * @return Ausführungszeilennummer
	 */
	public int getLineOfExecution(int lineOfView) {
		return begLine + lineOfView + CORRECTION_OFFSET;
	}

	/**
	 * Konvertiert die angegebene Anzeigezeilennummer in die Dateizeilennummer.
	 * 
	 * @param lineOfView
	 *            Anzeigezeilennummer
	 * 
	 * @return Dateizeilennummer
	 */
	public int getLineOfFile(int lineOfView) {
		return begLine + lineOfView;
	}

	/**
	 * Liefert die Anzeigezeilennummer des ersten Statements in der Routine.
	 * 
	 * @return Anzeigezeilennummer des ersten Statements
	 */
	public int getLineOfFirstStatement() {
		if (editAdmin != null) {
			int i = begLine;
			while (i < endLine) {
				String line = getLine(i).trim();
				if (line.startsWith(KEditKW.KW_END_VAR)) {
					begLine = i + 1;
				}
				i++;
			}
			i = begLine;
			while (KvtStatementType.getType(getLine(i), routine) == KvtStatementType.TYPE_COMMENT) {
				i++;
			}
		}
		return -1;
	}

	protected void releaseResources() {
		if (editAdmin != null) {
			editAdmin.removeKEditModelListener(this);
			KTcDfl d = KvtSystemCommunicator.getTcDfl();
			if (d != null) {
				d.editor.dismissKEditor(editAdmin);
			}
		}
		kairoVersion = null;
		routine = null;
		editAdmin = null;
		breakpoints.setSize(0);
		KTcDfl d = KvtSystemCommunicator.getTcDfl();
		if (d != null) {
			d.codepoint.removeCodePointListener(this);
		}
	}

	protected void setStatementRange(int line, int count) {
		long time = System.currentTimeMillis();
		int beg = line;
		int end = line + count;

		// if ((KvtSystem.getKAIROVersionString() != null) &&
		// editAdmin.getLine(beg).trim().startsWith(KvtSystem.getKAIROVersionString()))
		// {
		// try {
		// kairoVersion =
		// editAdmin.getLine(beg).trim().substring(KvtSystem.getKAIROVersionString().length()).trim();
		// } catch (Exception e) {
		// kairoVersion = null;
		// }
		// beg++;
		// }
		// if (KvtSystem.isEndUserMode() && (!KvtSystem.isIMMHandling)){
		begLine = beg;
		endLine = end;
		// return;
		// }
		
		
		if (editAdmin.getLine(beg).trim().startsWith(KEditKW.KW_ROUTINE)) {
			beg++;
			end--;
		}
		int i = beg;
		while (i < end) {
			if (editAdmin.getLine(i).trim().startsWith(KEditKW.KW_END_VAR)) {
				beg = i + 1;
			}
			i++;
		}
		// if (KvtSystem.isIMMHandling && KvtSystem.getUserLevel() < 15) {
		// i = beg;
		// while (i < end) {
		// String l = editAdmin.getLine(i).trim();
		// if (l.equalsIgnoreCase(EDIT_START)) {
		// beg = i + 1;
		// } else if (l.equalsIgnoreCase(EDIT_END)) {
		// end = i;
		// }
		// i++;
		// }
		// }
		begLine = beg;
		endLine = end;
	
	}

	public boolean isValidEditorLine(int line) {
		// if (KvtSystem.isIMMHandling) {
		// return (line + begLine) <= endLine;
		// }
		return true;
	}

	/**
	 * Returns the routine
	 * 
	 * @return routine
	 */
	public KStructRoutine getKStructRoutine() {
		return routine;
	}

	public void reloadRoutine() {
		KStructRoutine r = routine;
		routine = null;
		setKStructRoutine(r);
	}

	/**
	 * Setzt eine neue Routine in das Routinenmodell.
	 * 
	 * @param routine
	 *            Routine
	 */
	public void setKStructRoutine(KStructRoutine routine) {
		mainFlowPointer = -1;
		if ((routine != null) && !routine.equals(this.routine)) {
			releaseResources();
			KTcDfl d = KvtSystemCommunicator.getTcDfl();
			if (d != null) {
				editAdmin = d.editor.getKEditor((KStructProgram) routine.getParent());
				if (editAdmin != null) {
					TcEditorTextRange range = editAdmin.getTextRange(routine);
					if (range != null) {
						setStatementRange(range.line, range.count);
						this.routine = routine;
						editAdmin.addKEditModelListener(this);
						// get all breakpoints
						Enumeration e = d.codepoint.getCodePoints(routine);
						while (e.hasMoreElements()) {
							CodePoint codePoint = (CodePoint) e.nextElement();
							int i = 0;
							while (i < breakpoints.size()) {
								KCodePointAdministrator.CodePoint cp = (KCodePointAdministrator.CodePoint) breakpoints.elementAt(i);
								if (codePoint.getLineNr() <= cp.getLineNr()) {
									break;
								}
								i++;
							}
							breakpoints.insertElementAt(codePoint, i);
						}
						d.codepoint.addCodePointListener(this);
					} else {
						d.editor.dismissKEditor(editAdmin);
						editAdmin = null;
					}
				}
			}
		} else if (routine == null) {
			releaseResources();
		}
		fireRoutineChanged();
		fireMainFlowPointerChanged();
	}

	/**
	 * Liefert die Anzeigezeilennummer des Hauptlaufzeigers zurück.
	 * 
	 * @return Anzeigezeilennummer des Hauptlaufzeigers
	 */
	public int getMainFlowPointer() {
		return mainFlowPointer;
	}

	/**
	 * Setzt den Hauptlaufzeiger.
	 * 
	 * @param lineOfView
	 *            Anzeigezeilennummer
	 */
	public void setMainFlowPointer(int lineOfView) {
		if (mainFlowPointer != lineOfView) {
			mainFlowPointer = lineOfView;
			fireMainFlowPointerChanged();
	    }
	}

	/**
	 * Aktiviert die Breakpoints, d.h. alle Breakpoints im TeachControl werden
	 * gelöscht und die gespeicherten Breakpoints des Teachviews werden im
	 * TeachControl gesetzt und aktiviert.
	 * 
	 * @param b
	 *            true für das Aktivieren der Breakpoints
	 */
	public void setBreakpointsActivated(boolean b) {
		KTcDfl d = KvtSystemCommunicator.getTcDfl();
		if (d != null) {
			d.codepoint.setActivated(b);
		}
	}

	/**
	 * Fügt einen neuen Breakpoint hinzu.
	 * 
	 * @param lineOfView
	 *            Anzeigezeilennummer
	 */
	public void addBreakpoint(int lineOfView) {
		if (routine != null) {
			KTcDfl d = KvtSystemCommunicator.getTcDfl();
			if (d != null) {
				d.codepoint.addCodePoint(routine, lineOfView + begLine + CORRECTION_OFFSET, KCodePointAdministrator.BREAKPOINT);
			}
		}
	}

	/**
	 * Entfernt den Breakpoint.
	 * 
	 * @param lineOfView
	 *            Anzeigezeilennummer
	 */
	public void removeBreakpoint(int lineOfView) {
		if (routine != null) {
			CodePoint cp = getBreakpointAtLine(lineOfView);
			if (cp != null) {
				KTcDfl d = KvtSystemCommunicator.getTcDfl();
				if (d != null) {
					d.codepoint.removeCodePoint(cp);
				}
			}
		}
	}

	/**
	 * Enfernt alle Breakpoints der Routine.
	 */
	public void removeAllBreakpoints() {
		for (int i = breakpoints.size() - 1; 0 <= i; i--) {
			CodePoint cp = (CodePoint) breakpoints.elementAt(i);
			KTcDfl d = KvtSystemCommunicator.getTcDfl();
			if (d != null) {
				d.codepoint.removeCodePoint(cp);
			}
		}
	}

	/**
	 * Liefert die Anzahl der Breakpoints der Routine zurück.
	 * 
	 * @return Anzahl der Breakpoints
	 */
	public int getBreakpointCount() {
		return breakpoints.size();
	}

	/**
	 * Liefert den i-ten Breakpoint
	 * 
	 * @param index
	 *            Index
	 * 
	 * @return Breakpoint
	 */
	public CodePoint getBreakpoint(int index) {
		return (CodePoint) breakpoints.elementAt(index);
	}

	/**
	 * Liefert den Breakpoint für die angegeben Anzeigezeilennummer zurück.
	 * 
	 * @param lineOfView
	 *            Anzeigezeilennummer
	 * 
	 * @return Breakpoint
	 */
	public CodePoint getBreakpointAtLine(int lineOfView) {
		if (routine != null) {
			for (int i = 0; i < breakpoints.size(); i++) {
				CodePoint cp = (CodePoint) breakpoints.elementAt(i);
				if (cp.getLineNr() == (lineOfView + begLine + +CORRECTION_OFFSET)) {
					return cp;
				}
			}
		}
		return null;
	}

	/**
	 * @see com.keba.kemro.teach.dfl.edit.KEditModelListener#changed()
	 */
	public void changed() {
		TcEditorTextRange range = editAdmin.getTextRange(routine);
		if (range != null) {
			setStatementRange(range.line, range.count);
		} else {
			releaseResources();
		}
		fireRoutineModelChanged();
	}

	/**
	 * @see com.keba.kemro.teach.dfl.codepoint.KCodePointListener#added(com.keba.kemro.teach.dfl.codepoint.KCodePointAdministrator.CodePoint)
	 */
	public void added(KCodePointAdministrator.CodePoint codePoint) {
		if (routine != null) {
			if (routine.equals(codePoint.getStructRoutine())) {
				int i = 0;
				while (i < breakpoints.size()) {
					KCodePointAdministrator.CodePoint cp = (KCodePointAdministrator.CodePoint) breakpoints.elementAt(i);
					if (codePoint.getLineNr() <= cp.getLineNr()) {
						break;
					}
					i++;
				}
				breakpoints.insertElementAt(codePoint, i);
				fireBreakpointAdded(codePoint.getLineNr() - begLine - CORRECTION_OFFSET);
			}
		}
	}

	/**
	 * @see com.keba.kemro.teach.dfl.codepoint.KCodePointListener#removed(com.keba.kemro.teach.dfl.codepoint.KCodePointAdministrator.CodePoint)
	 */
	public void removed(KCodePointAdministrator.CodePoint codePoint) {
		if (routine != null) {
			if (routine.equals(codePoint.getStructRoutine())) {
				breakpoints.removeElement(codePoint);
				fireBreakpointChanged(codePoint.getLineNr() - begLine - CORRECTION_OFFSET);
			}
		}
	}

	/**
	 * @see com.keba.kemro.teach.dfl.codepoint.KCodePointListener#changed(com.keba.kemro.teach.dfl.codepoint.KCodePointAdministrator.CodePoint)
	 */
	public void changed(KCodePointAdministrator.CodePoint codePoint) {
		if (routine != null) {
			if (routine.equals(codePoint.getStructRoutine())) {
				fireBreakpointChanged(codePoint.getLineNr() - begLine - CORRECTION_OFFSET);
			}
		}
	}

	/**
	 * @see com.keba.kemro.teach.dfl.codepoint.KCodePointListener#allChanged()
	 */
	public void allChanged() {
		for (int i = breakpoints.size() - 1; 0 <= i; i--) {
			CodePoint cp = (CodePoint) breakpoints.elementAt(i);
			fireBreakpointChanged(cp.getLineNr() - begLine - CORRECTION_OFFSET);
		}
	}

	/**
	 * @see com.keba.kemro.teach.dfl.codepoint.KCodePointListener#watchpointVariableAdded(com.keba.kemro.teach.dfl.codepoint.KCodePointAdministrator.CodePoint,
	 *      com.keba.kemro.teach.dfl.codepoint.KCodePointAdministrator.WatchpointVariableNode)
	 */
	public void watchpointVariableAdded(KCodePointAdministrator.CodePoint codePoint, KCodePointAdministrator.WatchpointVariableNode variable) {
		// interface method not needed
	}

	/**
	 * @see com.keba.kemro.teach.dfl.codepoint.KCodePointListener#watchpointVariableRemoved(com.keba.kemro.teach.dfl.codepoint.KCodePointAdministrator.CodePoint,
	 *      com.keba.kemro.teach.dfl.codepoint.KCodePointAdministrator.WatchpointVariableNode)
	 */
	public void watchpointVariableRemoved(KCodePointAdministrator.CodePoint codePoint, KCodePointAdministrator.WatchpointVariableNode variable) {
		// interface method not needed
	}

	/**
	 * @see com.keba.kemro.teach.dfl.codepoint.KCodePointListener#watchpointVariableChanged(com.keba.kemro.teach.dfl.codepoint.KCodePointAdministrator.CodePoint,
	 *      com.keba.kemro.teach.dfl.codepoint.KCodePointAdministrator.WatchpointVariableNode)
	 */
	public void watchpointVariableChanged(KCodePointAdministrator.CodePoint codePoint, KCodePointAdministrator.WatchpointVariableNode variable) {
		// interface method not needed
	}
}
