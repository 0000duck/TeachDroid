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
package com.keba.kemro.teach.dfl.edit;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.dir.KDirEntry;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.util.KDflLogger;
import com.keba.kemro.teach.network.*;

/**
 * Mit diesem Editor ist es möglich Änderungen in Dateien durchzuführen. Es
 * können Variablen, Typen, Constanten angelegt, sowie Codezeilen eingefügt
 * werden.
 */
public class KEditAdministrator {
	private final static int MAX_CACHE_SIZE = 8;
	private final KEditor[] cache = new KEditor[MAX_CACHE_SIZE];
	private int cacheCounter;

	private KTcDfl dfl;

	protected KEditAdministrator(KTcDfl dfl) {
		this.dfl = dfl;
	}

	protected void init() {

		dfl.client.addConnectionListener(new TcConnectionListener() {
			public void connectionStateChanged(boolean isConnected) {
				if (!isConnected) {
					// clear model cache
					clearModelCache();
				}
			}
		});
	}

	/**
	 * Retruns a editor for the given program. The editor should be freed after
	 * using by calling dismissKEditAdministrator.
	 * 
	 * @param program
	 *            program for which the editor is requested
	 * 
	 * @return editor
	 */
	public KEditor getKEditor(KStructProgram program) {
		TcStructuralNode n = program.getTcStructuralNode();
		if (n == null) {
			return null;
		}
		TcStructuralNode dirEntryNode = n.getDeclarationNode();
		if (dirEntryNode == null) {
			return null;
		}
		TcDirEntry file = dfl.client.structure.getDirEntry(dirEntryNode);
		if (file == null) {
			return null;
		}
		int i = 0;
		while ((i < cacheCounter) && !cache[i].file.equals(file)) {
			i++;
		}
		if (i < cacheCounter) {
			// editor found
			if (!cache[i].modifiedDate.equals(file.getModifiedDate())) {
				cache[i].reopenEditorModel();
				// cache[i].modifiedDate = file.getModifiedDate();
				cache[i].fireEditorModelChanged();
			}
			cache[i].clients++;
			return cache[i];
		}
		if (cacheCounter >= MAX_CACHE_SIZE) {
			i = 0;
			while ((i < cacheCounter) && (0 < cache[i].clients)) {
				i++;
			}
			if (i < cacheCounter) {
				// no clients for this editor => remove editor
				cache[i].clear();
				if (i + 1 < cacheCounter) {
					System.arraycopy(cache, i + 1, cache, i, cacheCounter - i - 1);
				}
				cacheCounter--;
				cache[cacheCounter] = null;
			}
		}
		if (cacheCounter < MAX_CACHE_SIZE) {
			TcEditorModel model = dfl.client.structure.getEditorModel(n);
			if (model != null) {
				cache[cacheCounter] = new KEditor(model, file, dfl);
				cache[cacheCounter].clients++;
				cacheCounter++;
				return cache[cacheCounter - 1];
			}
		}
		return null;
	}
	
	
	/**
	 * Freigeben des Editors
	 * 
	 * @param editor
	 *            Freizugebender Editor
	 */
	public void dismissKEditor(KEditor editor) {
		// closeAllKEditAdministrator();
		int i = 0;
		while ((i < cacheCounter) && !cache[i].equals(editor)) {
			i++;
		}
		if (i < cacheCounter) {
			cache[i].clients--;
			if (cache[i].clients == 0) {
				cache[i].clear();
				System.arraycopy(cache, i + 1, cache, i, cacheCounter - i - 1);
				cacheCounter--;
				cache[cacheCounter] = null;
			}

		} else {
			// error
			KDflLogger.error(KEditAdministrator.class, "KEditAdministrator - dismissKEditAdministrator - editor isn't in cache");
		}
	}

	/**
	 * Reloads the editor content. This method have to be called after a
	 * operation which changes the editor content.
	 * 
	 * @param program
	 *            Knoten der editiert wurde (KStructProgram oder
	 *            KStructTypeUnit)
	 */
	public void reloadKEditor(KStructProgram program) {
		if (program == null) {
			return;
		}
		TcStructuralNode n = program.getTcStructuralNode();
		if (n == null) {
			return;
		}
		TcStructuralNode dirEntryNode = n.getDeclarationNode();
		if (dirEntryNode == null) {
			return;
		}
		TcDirEntry file = dfl.client.structure.getDirEntry(dirEntryNode);
		if (file == null) {
			return;
		}
		int i = 0;
		while ((i < cacheCounter) && !cache[i].file.equals(file)) {
			i++;
		}
		if (i < cacheCounter) {
			// editor found
			cache[i].reopenEditorModel();
			cache[i].fireEditorModelChanged();
		}
	}

	private void clearModelCache() {
		int i = 0;
		while (i < cacheCounter) {
			cache[i] = null;
			i++;
		}
		cacheCounter = 0;
	}

}
