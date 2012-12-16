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
package com.keba.kemro.teach.dfl.compilerError;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.network.*;

import java.util.*;

/**
 * Holt die Kompilerfehlermeldungen und stellt sie dem System in Form einer Liste zur Verfügung.
 * Alle Listener werden beim Auftreten von Compilerfehlern benachrichtigt.
 */
public class KCompilerErrorAdministrator {
	private final KCompilerErrorList compilerErrors = new KCompilerErrorList();
	private final Hashtable lists = new Hashtable();
	private KTcDfl dfl;
	
	protected KCompilerErrorAdministrator (KTcDfl dfl) {
		this.dfl = dfl;
	}
	protected void init () {
	}
	/**
	 * Checks for error messages. If thera are error messages then KCompilerErrorListener will be called.
	 *
	 * @return true if there are error messages
	 */
	public boolean checkIncCompilerError () {
			compilerErrors.removeAllCompilerErrors();
			TcErrorMessage error = dfl.client.structure.getErrorMessage();
			while (error != null) {
				compilerErrors.addCompilerError(new KCompilerError(error));
				error = dfl.client.structure.getErrorMessage();
			}
			return (compilerErrors.getCompilerErrorCount() > 0);
	}
	/**
	 * Dies Methode löst ein Auslesen der Fehlermeldungen aus. Wird nach jedem Build aufgerufen.
	 * Falls Fehlermeldungen anstehen werden diese in eine Liste zusammengefasst und an
	 * alle Listener weitergereicht.
	 *
	 * @param project zugehöriges Projekt
	 *  
	 * @return True wenn Fehler aufgetreten sind.
	 */
	public boolean checkCompilerError (KStructProject project) {
		String key = project.getDirEntryPath();
		KCompilerErrorList l = (KCompilerErrorList) lists.get(key);
		if (l != null) {
			l.removeAllCompilerErrors();
		} else {
			l = new KCompilerErrorList();
			lists.put(key, l);
		}
		TcErrorMessage[] errors = dfl.client.structure.getErrorMessages(project.getTcStructuralNode());
		if (errors != null) {
			for (int i = 0; i < errors.length; i++) {
				l.addCompilerError(new KCompilerError(errors[i]));
			}
		}
		return 0 < l.getCompilerErrorCount();
	}

	/**
	 * Liefert die Compilerfehlermeldung des letzten Build aufrufes.
	 * 
	 * @return Compilerfehlermeldungen
	 */
	public KCompilerErrorList getCompilerErrorList (KStructProject project) {
		if (project == null) {
			return compilerErrors;
		} 
		String key = project.getDirEntryPath();
		return (KCompilerErrorList) lists.get(key);
	}
	
	public void removeCompilerErrorList (KStructProject project) {
		String key = project.getDirEntryPath();
		lists.remove(key);
	}
}
