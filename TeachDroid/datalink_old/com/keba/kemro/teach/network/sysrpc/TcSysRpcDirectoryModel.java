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
package com.keba.kemro.teach.network.sysrpc;

import java.io.IOException;
import java.util.Enumeration;

import com.keba.jrpc.rpc.RPCException;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcAddDirEntryIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcAddDirEntryOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcCopyDirEntryIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcCopyDirEntryOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcDeleteDirEntryIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcDeleteDirEntryOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetDirEntryInfoListIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetDirEntryInfoListOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetDirEntryInfoOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetFirstDirEntryChunkIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetFirstDirEntryChunkOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetNextDirEntryChunkIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetNextDirEntryChunkOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcRenameDirEntryIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcRenameDirEntryOut;
import com.keba.kemro.plc.network.sysrpc.TCI.TCI;
import com.keba.kemro.teach.network.TcConnectionListener;
import com.keba.kemro.teach.network.TcConnectionManager;
import com.keba.kemro.teach.network.TcDirEntry;
import com.keba.kemro.teach.network.TcDirectoryModel;
import com.keba.kemro.teach.network.TcExecutionModel;
import com.keba.kemro.teach.network.TcStructuralModel;

/**
 * TcDirectoryModel ermöglicht das Auslesen und Ändern von TeachControl -
 * Verzeichnissen und - Dateien. Verzeichniseinträge können
 * Projektverzeichnisse, Programmdateien und Bausteindateien sein.
 * 
 * @see TcConnectionListener
 * @see TcConnectionManager
 * @see TcStructuralModel
 * @see TcExecutionModel
 */
public class TcSysRpcDirectoryModel implements TcDirectoryModel {
	private final SysRpcTcAddDirEntryIn SysRpcTcAddDirEntryIn = new SysRpcTcAddDirEntryIn();
	private final SysRpcTcAddDirEntryOut SysRpcTcAddDirEntryOut = new SysRpcTcAddDirEntryOut();
	private final SysRpcTcCopyDirEntryIn SysRpcTcCopyDirEntryIn = new SysRpcTcCopyDirEntryIn();
	private final SysRpcTcCopyDirEntryOut SysRpcTcCopyDirEntryOut = new SysRpcTcCopyDirEntryOut();
	private final SysRpcTcRenameDirEntryIn SysRpcTcRenameDirEntryIn = new SysRpcTcRenameDirEntryIn();
	private final SysRpcTcRenameDirEntryOut SysRpcTcRenameDirEntryOut = new SysRpcTcRenameDirEntryOut();
	private final SysRpcTcDeleteDirEntryIn SysRpcTcDeleteDirEntryIn = new SysRpcTcDeleteDirEntryIn();
	private final SysRpcTcDeleteDirEntryOut SysRpcTcDeleteDirEntryOut = new SysRpcTcDeleteDirEntryOut();
	private final SysRpcTcGetFirstDirEntryChunkIn SysRpcTcGetFirstDirEntryChunkIn = new SysRpcTcGetFirstDirEntryChunkIn();
	private final SysRpcTcGetFirstDirEntryChunkOut SysRpcTcGetFirstDirEntryChunkOut = new SysRpcTcGetFirstDirEntryChunkOut();
	private final SysRpcTcGetNextDirEntryChunkIn SysRpcTcGetNextDirEntryChunkIn = new SysRpcTcGetNextDirEntryChunkIn();
	private final SysRpcTcGetNextDirEntryChunkOut SysRpcTcGetNextDirEntryChunkOut = new SysRpcTcGetNextDirEntryChunkOut();
	private final SysRpcTcGetDirEntryInfoListIn SysRpcTcGetDirEntryInfoListIn = new SysRpcTcGetDirEntryInfoListIn();
	private final SysRpcTcGetDirEntryInfoListOut SysRpcTcGetDirEntryInfoListOut = new SysRpcTcGetDirEntryInfoListOut();
	private final SysRpcTcGetDirEntryInfoOut SysRpcTcGetDirEntryInfoOut = new SysRpcTcGetDirEntryInfoOut();

	TcSysRpcClient client;
	String libPath;
	private boolean disconnected;

	TcSysRpcDirectoryModel(TcSysRpcClient client) {
		this.client = client;
	}

	/**
	 * Liefert die Einträge des TeachControl - Verzeichnisses (Global oder
	 * Projekt), welche der angegebenen Art kind entsprechen.
	 * 
	 * @param parent
	 *            gibt das Verzeichnis an, in welchem iteriert werden soll.
	 * @param kind
	 *            gibt die Art (Projekt, Programm, Baustein oder alle) der
	 *            Einträge an.
	 * 
	 * @return Verzeichnisenumeration
	 * 
	 * @see TcDirEntry
	 */
	public Enumeration getEntries(TcDirEntry parent, int kind) {
		if (client.getUserMode()) {
			if (kind == TcDirEntry.FILTER_PROGRAM_AND_USER_PROGRAM) {
				return new DirEntryChunkEnumeration(parent, TcDirEntry.USER_PROGRAM);
			}
			return new DirEntryChunkEnumeration(parent, kind);
		} else if (kind == TcDirEntry.FILTER_PROGRAM_AND_USER_PROGRAM) {
			return new ProgramAndUserProgramDirEntryChunkEnumeration(parent);
		}
		return new DirEntryChunkEnumeration(parent, kind);
	}

	/**
	 * Liefert das Wurzelverzeichnis.
	 * 
	 * @return Wurzelverzeichnis
	 */
	public TcDirEntry getRoot() {
		return new TcSysRpcDirEntry("", client);
	}

	
	Object SysRpcTcGetDirEntryInfoIn = new Object();
	public String getLibPath() {
		if (libPath == null) {
			libPath = "";
			TcSysRpcDirEntry system = new TcSysRpcDirEntry("_system.tt", client);
			synchronized (SysRpcTcGetDirEntryInfoIn) {
				SysRpcTcGetDirEntryInfoOut.info.name = "_system";
				SysRpcTcGetDirEntryInfoOut.info.kind.value = TcDirEntry.PROJECT;
				SysRpcTcGetDirEntryInfoOut.info.size = 0;
				SysRpcTcGetDirEntryInfoOut.info.attr = 0;
				SysRpcTcGetDirEntryInfoOut.info.createTime = 0;
				SysRpcTcGetDirEntryInfoOut.info.modifyTime = 0;
				SysRpcTcGetDirEntryInfoOut.info.accessTime = 0;
				SysRpcTcGetDirEntryInfoOut.info.isGlobal = false;
				system.setInfo(SysRpcTcGetDirEntryInfoOut.info);
			}
			Enumeration e = getEntries(system, TcDirEntry.FILTER_ALL_TC_ENTRIES);
			while (e.hasMoreElements()) {
				TcDirEntry entry = (TcDirEntry) e.nextElement();
				String path = entry.getDirEntryPath();
				if (path.startsWith("..", 1)) {
					int i = path.indexOf("_SYSTEM.TT");
					if (i == -1) {
						// old directory structure
						i = path.indexOf("FWHEADER.TT");
						if ((i > 0) && ( path.indexOf("FWHEADER.TTA") > -1)){
							i = -1;
						}
						if (0 < i) {
							libPath = path.substring(0, i + 11);
							break;
						}
            			i = path.indexOf("TEACHCONTROL");
						if (1 < i) {
							libPath = path.substring(0, i + 12);
							break;
						}
					}
				}
			}
		}
		return libPath;
	}

	/**
	 * Erstellt im Verzeichnis parent einen neuen Eintrag mit dem Namen name und
	 * der Art kind.
	 * 
	 * @param directory
	 *            Zielverzeichnis
	 * @param name
	 *            Name Verzeichniseintragname
	 * @param kind
	 *            Art des Eintrages (Porjekt, Programm oder Baustein)
	 * 
	 * @return neuer Verzeichniseintrag
	 * 
	 * @see TcDirEntry
	 */
	public TcDirEntry add(String directory, String name, int kind) {
		try {
			synchronized (SysRpcTcAddDirEntryIn) {
				SysRpcTcAddDirEntryIn.dirPath = TcSysRpcDirEntry.convertDirEntryPathToHandle(directory);
				SysRpcTcAddDirEntryIn.dirEntryName = name;
				SysRpcTcAddDirEntryIn.kind.value = convertKind(kind);
				client.client.SysRpcTcAddDirEntry_1(SysRpcTcAddDirEntryIn, SysRpcTcAddDirEntryOut);
				if (SysRpcTcAddDirEntryOut.retVal) {
					TcDirEntry dirEntry = new TcSysRpcDirEntry(SysRpcTcAddDirEntryOut.dirEntryPath.toString(), client);
					return dirEntry;
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcDirectoryModel - add: ");
		}
		return null;
	}
	

	private void disconnected(String text){
		if (!disconnected){
			disconnected = true;
			disconnected(text);
		}
	}

	/**
	 * Kopiert den Verzeichniseintrag source in das Zielverzeichnis destParent
	 * und gibt ihm den Namen destName. Bei Vezeichnissen werden auch alle
	 * Dateien der Unterverzeichnisse kopiert.
	 * 
	 * @param source
	 *            zu kopierender Verzeichniseintrag
	 * @param destDirectory
	 *            Zielverzeichnis
	 * @param destName
	 *            neuer Name des kopierten Eintrages
	 * 
	 * @return neuer Verzeichniseintrag
	 */
	public TcDirEntry copy(TcDirEntry source, String destDirectory, String destName) {
		try {
			synchronized (SysRpcTcCopyDirEntryIn) {
				SysRpcTcCopyDirEntryIn.srcDirEntryPath = ((TcSysRpcDirEntry) source).getHandle();
				SysRpcTcCopyDirEntryIn.destDirPath = TcSysRpcDirEntry.convertDirEntryPathToHandle(destDirectory);
				SysRpcTcCopyDirEntryIn.destdirEntryName = destName;
				SysRpcTcCopyDirEntryIn.kind.value = source.getKind();
				client.client.SysRpcTcCopyDirEntry_1(SysRpcTcCopyDirEntryIn, SysRpcTcCopyDirEntryOut);
				if (SysRpcTcCopyDirEntryOut.retVal) {
					TcDirEntry dirEntry = new TcSysRpcDirEntry(SysRpcTcCopyDirEntryOut.dirEntryPath.toString(), client);
					return dirEntry;
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcDirectoryModel - copy: ");
		}
		return null;
	}

	/**
	 * Benennt den Verzeichniseintrag dirEntry auf den Namen newName um.
	 * 
	 * @param dirEntry
	 *            zu umbenennender Verzeichniseintrag
	 * @param newName
	 *            neuer Name des Verzeichniseintrages
	 * 
	 * @return neuer Verzeichniseintrag
	 */
	public TcDirEntry rename(TcDirEntry dirEntry, String newName) {
		try {
			synchronized (SysRpcTcRenameDirEntryIn) {
				SysRpcTcRenameDirEntryIn.dirEntryPath = ((TcSysRpcDirEntry) dirEntry).getHandle();
				SysRpcTcRenameDirEntryIn.newDirEntryName = newName;
				SysRpcTcRenameDirEntryIn.kind.value = dirEntry.getKind();
				client.client.SysRpcTcRenameDirEntry_1(SysRpcTcRenameDirEntryIn, SysRpcTcRenameDirEntryOut);
				if (SysRpcTcRenameDirEntryOut.retVal) {
					TcDirEntry de = new TcSysRpcDirEntry(SysRpcTcRenameDirEntryOut.dirEntryPath.toString(), client);
					return de;
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcDirectoryModel - rename: ");
		}
		return null;
	}

	/**
	 * Löscht den Verzeichniseintrag dirEntry.
	 * 
	 * @param dirEntry
	 *            der zu löschende Verzeichniseintrag
	 * 
	 * @return true wenn der Verzeichniseintrag erfolgreich gelöscht werden
	 *         konnte
	 */
	public boolean delete(TcDirEntry dirEntry) {
		try {
			synchronized (SysRpcTcDeleteDirEntryIn) {
				SysRpcTcDeleteDirEntryIn.dirEntryPath = ((TcSysRpcDirEntry) dirEntry).getHandle();
				client.client.SysRpcTcDeleteDirEntry_1(SysRpcTcDeleteDirEntryIn, SysRpcTcDeleteDirEntryOut);
				return SysRpcTcDeleteDirEntryOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcDirectoryModel - delete: ");
		}
		return false;
	}

	private int convertKind(int kind) {
		if (client.getUserMode()) {
			if (kind == TcDirEntry.PROGRAM) {
				return TcDirEntry.USER_PROGRAM;
			}
			return kind;
		}
		return kind;
	}

	private class DirEntryChunkEnumeration implements Enumeration {
		TcDirEntry parent;
		int kind;
		private int nrOfHnd;
		private int iterHandle;
		private final TcSysRpcDirEntry[] elems = new TcSysRpcDirEntry[TCI.rpcChunkLen];
		private int index;
		private boolean isFirst = true;
		private boolean isValid = false;
		private boolean checkSystem = false;
		private boolean systemFound = false;

		private DirEntryChunkEnumeration(TcDirEntry parent, int kind) {
			this.parent = parent;
			this.kind = kind;
			checkSystem = (((TcSysRpcDirEntry) parent).getHandle().length() == 0)
					&& ((kind == TcDirEntry.PROJECT) || (kind == TcDirEntry.FILTER_ALL_TC_ENTRIES) || (kind == TcDirEntry.FILTER_ALL));
		}

		/**
		 * @see java.util.Enumeration#hasMoreElements()
		 */
		public boolean hasMoreElements() {
			if (!isValid) {
				if (index >= nrOfHnd) {
					if (isFirst) {
						isFirst = false;
						getFirstChunk();
					} else if (nrOfHnd == TCI.rpcChunkLen) {
						getNextChunk();
					}
				}
				isValid = index < nrOfHnd;
				if (!isValid && checkSystem && !systemFound) {
					// fake system entry when it isn't in the application
					// directory
					checkSystem = false;
					isValid = addSystemEntry();
				}
			}
			return isValid;
		}

		/**
		 * @see java.util.Enumeration#nextElement()
		 */
		public Object nextElement() {
			if (hasMoreElements()) {
				isValid = false;
				TcDirEntry elem = elems[index];
				index++;
				return elem;
			}
			return null;
		}

		private boolean addSystemEntry() {
			TcSysRpcDirEntry system = new TcSysRpcDirEntry("_system.tt", client);
			synchronized (SysRpcTcGetDirEntryInfoOut) {
				SysRpcTcGetDirEntryInfoOut.info.name = "_system";
				SysRpcTcGetDirEntryInfoOut.info.kind.value = TcDirEntry.PROJECT;
				SysRpcTcGetDirEntryInfoOut.info.size = 0;
				SysRpcTcGetDirEntryInfoOut.info.attr = 0;
				SysRpcTcGetDirEntryInfoOut.info.createTime = 0;
				SysRpcTcGetDirEntryInfoOut.info.modifyTime = 0;
				SysRpcTcGetDirEntryInfoOut.info.accessTime = 0;
				SysRpcTcGetDirEntryInfoOut.info.isGlobal = false;
				system.setInfo(SysRpcTcGetDirEntryInfoOut.info);
			}
			Enumeration e = getEntries(system, TcDirEntry.FILTER_ALL_TC_ENTRIES);
			while (e.hasMoreElements()) {
				TcDirEntry entry = (TcDirEntry) e.nextElement();
				String path = entry.getDirEntryPath();
				int i = path.indexOf("_SYSTEM.TT");
				if (0 < i) {
					system.setDirEntryPath(path.substring(0, i + 10));
					if (index < TCI.rpcChunkLen) {
						elems[index] = system;
						return true;
					}
				}
			}
			return false;
		}

		private boolean loadInfo(TcSysRpcClient client, String[] dirEntryPaths, int nrOfHandles) throws RPCException, IOException {
			synchronized (SysRpcTcGetDirEntryInfoListIn) {
				SysRpcTcGetDirEntryInfoListIn.dirEntryPaths_count = nrOfHandles;
				if (SysRpcTcGetDirEntryInfoListIn.dirEntryPaths == null) {
					SysRpcTcGetDirEntryInfoListIn.dirEntryPaths = new String[TCI.rpcChunkLen];
				}
				System.arraycopy(dirEntryPaths, 0, SysRpcTcGetDirEntryInfoListIn.dirEntryPaths, 0, nrOfHandles);
				client.client.SysRpcTcGetDirEntryInfoList_1(SysRpcTcGetDirEntryInfoListIn, SysRpcTcGetDirEntryInfoListOut);
				if (SysRpcTcGetDirEntryInfoListOut.retVal) {
					for (int i = 0; i < nrOfHandles; i++) {
						elems[i].setInfo(SysRpcTcGetDirEntryInfoListOut.infos[i]);
						if (checkSystem && !systemFound) {
							systemFound = elems[i].isSystem();
						}
					}
					return true;
				}
				return false;
			}
		}

		private void getFirstChunk() {
			try {
				synchronized (SysRpcTcGetFirstDirEntryChunkIn) {
					SysRpcTcGetFirstDirEntryChunkIn.dirPath = ((TcSysRpcDirEntry) parent).getHandle();
					SysRpcTcGetFirstDirEntryChunkIn.kind.value = kind;
					client.client.SysRpcTcGetFirstDirEntryChunk_1(SysRpcTcGetFirstDirEntryChunkIn, SysRpcTcGetFirstDirEntryChunkOut);
					if (SysRpcTcGetFirstDirEntryChunkOut.retVal) {
						index = 0;
						nrOfHnd = SysRpcTcGetFirstDirEntryChunkOut.dirEntryPaths_count;
						iterHandle = SysRpcTcGetFirstDirEntryChunkOut.iterHnd;
						if (0 < nrOfHnd) {
							for (int i = 0; i < nrOfHnd; i++) {
								elems[i] = new TcSysRpcDirEntry(SysRpcTcGetFirstDirEntryChunkOut.dirEntryPaths[i].toString(), client);
							}
							if (loadInfo(client, SysRpcTcGetFirstDirEntryChunkOut.dirEntryPaths, nrOfHnd)) {
								return;
							}
						}
					}
				}
			} catch (Exception e) {
				disconnected("Disconnect in TcDirectoryModel - DirEntryChunkEnumeration - getFirstChunk: ");
			}
			nrOfHnd = 0;
			for (int i = 0; i < elems.length; i++) {
				elems[i] = null;
			}
		}

		private void getNextChunk() {
			try {
				synchronized (SysRpcTcGetNextDirEntryChunkIn) {
					SysRpcTcGetNextDirEntryChunkIn.iterHnd = iterHandle;
					SysRpcTcGetNextDirEntryChunkIn.dirPath = ((TcSysRpcDirEntry) parent).getHandle();
					SysRpcTcGetNextDirEntryChunkIn.kind.value = kind;
					client.client.SysRpcTcGetNextDirEntryChunk_1(SysRpcTcGetNextDirEntryChunkIn, SysRpcTcGetNextDirEntryChunkOut);
					if (SysRpcTcGetNextDirEntryChunkOut.retVal) {
						index = 0;
						nrOfHnd = SysRpcTcGetNextDirEntryChunkOut.dirEntryPaths_count;
						iterHandle = SysRpcTcGetNextDirEntryChunkOut.iterHnd;
						if (0 < nrOfHnd) {
							for (int i = 0; i < nrOfHnd; i++) {
								elems[i] = new TcSysRpcDirEntry(SysRpcTcGetNextDirEntryChunkOut.dirEntryPaths[i].toString(), client);
							}
							if (loadInfo(client, SysRpcTcGetNextDirEntryChunkOut.dirEntryPaths, nrOfHnd)) {
								return;
							}
						}
					}
				}
			} catch (Exception e) {
				disconnected("Disconnect in TcDirectoryModel - DirEntryChunkEnumeration - getNextChunk: ");
			}
			nrOfHnd = 0;
			for (int i = 0; i < elems.length; i++) {
				elems[i] = null;
			}
		}
	}

	private class ProgramAndUserProgramDirEntryChunkEnumeration implements Enumeration {
		DirEntryChunkEnumeration programs;
		DirEntryChunkEnumeration userPrograms;

		private ProgramAndUserProgramDirEntryChunkEnumeration(TcDirEntry parent) {
			programs = new DirEntryChunkEnumeration(parent, TcDirEntry.PROGRAM);
			userPrograms = new DirEntryChunkEnumeration(parent, TcDirEntry.USER_PROGRAM);
		}

		public boolean hasMoreElements() {
			return programs.hasMoreElements() ? true : userPrograms.hasMoreElements();
		}

		public Object nextElement() {
			Object elem = programs.nextElement();
			if (elem != null) {
				return elem;
			}
			return userPrograms.nextElement();
		}

	}
}
