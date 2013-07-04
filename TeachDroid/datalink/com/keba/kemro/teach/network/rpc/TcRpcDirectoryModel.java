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
package com.keba.kemro.teach.network.rpc;

import com.keba.jrpc.rpc.*;
import com.keba.kemro.teach.network.*;
import com.keba.kemro.teach.network.rpc.protocol.*;

import java.io.*;
import java.util.*;


/**
 * TcDirectoryModel ermöglicht das Auslesen und Ändern von TeachControl - Verzeichnissen und -
 * Dateien. Verzeichniseinträge können Projektverzeichnisse, Programmdateien und  
 * Bausteindateien sein.
 *
 * @see TcConnectionListener
 * @see TcConnectionManager
 * @see TcStructuralModel
 * @see TcExecutionModel
 */
public class TcRpcDirectoryModel implements TcDirectoryModel {
   private final RpcTcAddDirEntryIn rpcTcAddDirEntryIn = new RpcTcAddDirEntryIn();
   private final RpcTcAddDirEntryOut rpcTcAddDirEntryOut = new RpcTcAddDirEntryOut();
   private final RpcTcCopyDirEntryIn rpcTcCopyDirEntryIn = new RpcTcCopyDirEntryIn();
   private final RpcTcCopyDirEntryOut rpcTcCopyDirEntryOut = new RpcTcCopyDirEntryOut();
   private final RpcTcRenameDirEntryIn rpcTcRenameDirEntryIn = new RpcTcRenameDirEntryIn();
   private final RpcTcRenameDirEntryOut rpcTcRenameDirEntryOut = new RpcTcRenameDirEntryOut();
   private final RpcTcDeleteDirEntryIn rpcTcDeleteDirEntryIn = new RpcTcDeleteDirEntryIn();
   private final RpcTcDeleteDirEntryOut rpcTcDeleteDirEntryOut = new RpcTcDeleteDirEntryOut();
   private final RpcTcGetFirstDirEntryChunkIn rpcTcGetFirstDirEntryChunkIn =
      new RpcTcGetFirstDirEntryChunkIn();
   private final RpcTcGetFirstDirEntryChunkOut rpcTcGetFirstDirEntryChunkOut =
      new RpcTcGetFirstDirEntryChunkOut();
   private final RpcTcGetNextDirEntryChunkIn rpcTcGetNextDirEntryChunkIn =
      new RpcTcGetNextDirEntryChunkIn();
   private final RpcTcGetNextDirEntryChunkOut rpcTcGetNextDirEntryChunkOut =
      new RpcTcGetNextDirEntryChunkOut();
   private final RpcTcGetDirEntryInfoListIn rpcTcGetDirEntryInfoListIn =
      new RpcTcGetDirEntryInfoListIn();
   private final RpcTcGetDirEntryInfoListOut rpcTcGetDirEntryInfoListOut =
      new RpcTcGetDirEntryInfoListOut();
   private final RpcTcGetDirEntryInfoOut rpcTcGetDirEntryInfoOut =
      new RpcTcGetDirEntryInfoOut();

   TcRpcClient client;
   String libPath;
   
   TcRpcDirectoryModel (TcRpcClient client) {
   	this.client = client;
   }
   /**
    * Liefert die Einträge des TeachControl - Verzeichnisses (Global oder Projekt),  welche der
    * angegebenen Art kind entsprechen.
    *
    * @param parent gibt das Verzeichnis an, in welchem iteriert werden soll.
    * @param kind gibt die Art (Projekt, Programm, Baustein oder alle) der Einträge an.
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
   public TcDirEntry getRoot () {
      return new TcRpcDirEntry("", client);
   }

   public String getLibPath () {
   	if (libPath == null) {
      	libPath = "";
      	TcRpcDirEntry system = new TcRpcDirEntry("_system.tt", client);
      	synchronized (rpcTcGetDirEntryInfoOut) {
      		rpcTcGetDirEntryInfoOut.info.name = "_system";
      		rpcTcGetDirEntryInfoOut.info.kind.value = TcDirEntry.PROJECT;
      		rpcTcGetDirEntryInfoOut.info.size = 0;
      		rpcTcGetDirEntryInfoOut.info.attr = 0;
      		rpcTcGetDirEntryInfoOut.info.createTime = 0;
      		rpcTcGetDirEntryInfoOut.info.modifyTime = 0;
      		rpcTcGetDirEntryInfoOut.info.accessTime = 0;
      		rpcTcGetDirEntryInfoOut.info.isGlobal = false;
      		system.setInfo(rpcTcGetDirEntryInfoOut.info);
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
    * Erstellt im Verzeichnis parent einen neuen Eintrag mit dem Namen name und der Art kind.
    *
    * @param directory Zielverzeichnis
    * @param name Name Verzeichniseintragname
    * @param kind Art des Eintrages (Porjekt, Programm oder Baustein)
    *
    * @return neuer Verzeichniseintrag
    *
    * @see TcDirEntry
    */
   public TcDirEntry add (String directory, String name, int kind) {
      try {
         synchronized (rpcTcAddDirEntryIn) {
            rpcTcAddDirEntryIn.dirPath = TcRpcDirEntry.convertDirEntryPathToHandle(directory);
            rpcTcAddDirEntryIn.dirEntryName = name;
            rpcTcAddDirEntryIn.kind.value = convertKind(kind);
            client.client.RpcTcAddDirEntry_1(rpcTcAddDirEntryIn, rpcTcAddDirEntryOut);
            if (rpcTcAddDirEntryOut.retVal) {
               TcDirEntry dirEntry = new TcRpcDirEntry(rpcTcAddDirEntryOut.dirEntryPath, client);
               return dirEntry;
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcDirectoryModel - add: ");
      }
      return null;
   }

   /**
    * Kopiert den Verzeichniseintrag source in das Zielverzeichnis destParent und gibt ihm den
    * Namen destName. Bei Vezeichnissen werden auch alle Dateien der Unterverzeichnisse kopiert. 
    *
    * @param source zu kopierender Verzeichniseintrag
    * @param destDirectory Zielverzeichnis
    * @param destName neuer Name des kopierten Eintrages
    *
    * @return neuer Verzeichniseintrag
    */
   public TcDirEntry copy (TcDirEntry source, String destDirectory, String destName) {
      try {
         synchronized (rpcTcCopyDirEntryIn) {
            rpcTcCopyDirEntryIn.srcDirEntryPath = ((TcRpcDirEntry) source).getHandle();
            rpcTcCopyDirEntryIn.destDirPath = TcRpcDirEntry.convertDirEntryPathToHandle(destDirectory);
            rpcTcCopyDirEntryIn.destdirEntryName = destName;
            rpcTcCopyDirEntryIn.kind.value = source.getKind();
            client.client.RpcTcCopyDirEntry_1(rpcTcCopyDirEntryIn, rpcTcCopyDirEntryOut);
            if (rpcTcCopyDirEntryOut.retVal) {
               TcDirEntry dirEntry = new TcRpcDirEntry(rpcTcCopyDirEntryOut.dirEntryPath, client);
               return dirEntry;
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcDirectoryModel - copy: ");
      }
      return null;
   }

   /**
    * Benennt den Verzeichniseintrag dirEntry auf den Namen newName um.
    *
    * @param dirEntry zu umbenennender Verzeichniseintrag
    * @param newName neuer Name des Verzeichniseintrages
    *
    * @return neuer Verzeichniseintrag
    */
   public TcDirEntry rename (TcDirEntry dirEntry, String newName) {
      try {
         synchronized (rpcTcRenameDirEntryIn) {
            rpcTcRenameDirEntryIn.dirEntryPath = ((TcRpcDirEntry) dirEntry).getHandle();
            rpcTcRenameDirEntryIn.newDirEntryName = newName;
            rpcTcRenameDirEntryIn.kind.value = dirEntry.getKind();
            client.client.RpcTcRenameDirEntry_1(rpcTcRenameDirEntryIn, rpcTcRenameDirEntryOut);
            if (rpcTcRenameDirEntryOut.retVal) {
               TcDirEntry de = new TcRpcDirEntry(rpcTcRenameDirEntryOut.dirEntryPath, client);
               return de;
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcDirectoryModel - rename: ");
      }
      return null;
   }

   /**
    * Löscht den Verzeichniseintrag dirEntry.
    *
    * @param dirEntry der zu löschende Verzeichniseintrag
    *
    * @return true wenn der Verzeichniseintrag erfolgreich gelöscht werden konnte
    */
   public boolean delete (TcDirEntry dirEntry) {
      try {
         synchronized (rpcTcDeleteDirEntryIn) {
            rpcTcDeleteDirEntryIn.dirEntryPath = ((TcRpcDirEntry) dirEntry).getHandle();
            client.client.RpcTcDeleteDirEntry_1(rpcTcDeleteDirEntryIn, rpcTcDeleteDirEntryOut);
            return rpcTcDeleteDirEntryOut.retVal;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcDirectoryModel - delete: ");
      }
      return false;
   }
   
   private int convertKind (int kind) {
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
      private final TcRpcDirEntry[] elems = new TcRpcDirEntry[rpcChunkLen.value];
      private int index;
      private boolean isFirst = true;
      private boolean isValid = false;
      private boolean checkSystem = false;
      private boolean systemFound = false;

      private DirEntryChunkEnumeration (TcDirEntry parent, int kind) {
         this.parent = parent;
         this.kind = kind;
         checkSystem = (((TcRpcDirEntry) parent).getHandle().length() == 0) 
         && ((kind == TcDirEntry.PROJECT) || (kind == TcDirEntry.FILTER_ALL_TC_ENTRIES) || (kind == TcDirEntry.FILTER_ALL));
      }

      /**
       * @see java.util.Enumeration#hasMoreElements()
       */
      public boolean hasMoreElements () {
         if (!isValid) {
            if (index >= nrOfHnd) {
               if (isFirst) {
                  isFirst = false;
                  getFirstChunk();
               } else if (nrOfHnd == rpcChunkLen.value) {
                  getNextChunk();
               }
            }
            isValid = index < nrOfHnd;
            if (!isValid && checkSystem && !systemFound) {
            	// fake system entry when it isn't in the application directory
            	checkSystem = false;
            	isValid = addSystemEntry();
            }
         }
         return isValid;
      }

      /**
       * @see java.util.Enumeration#nextElement()
       */
      public Object nextElement () {
         if (hasMoreElements()) {
            isValid = false;
            TcDirEntry elem = elems[index];
            index++;
            return elem;
         }
         return null;
      }

      private boolean addSystemEntry () {
      	TcRpcDirEntry system = new TcRpcDirEntry("_system.tt", client);
      	synchronized (rpcTcGetDirEntryInfoOut) {
      		rpcTcGetDirEntryInfoOut.info.name = "_system";
      		rpcTcGetDirEntryInfoOut.info.kind.value = TcDirEntry.PROJECT;
      		rpcTcGetDirEntryInfoOut.info.size = 0;
      		rpcTcGetDirEntryInfoOut.info.attr = 0;
      		rpcTcGetDirEntryInfoOut.info.createTime = 0;
      		rpcTcGetDirEntryInfoOut.info.modifyTime = 0;
      		rpcTcGetDirEntryInfoOut.info.accessTime = 0;
      		rpcTcGetDirEntryInfoOut.info.isGlobal = false;
      		system.setInfo(rpcTcGetDirEntryInfoOut.info);
      	}
      	Enumeration e = getEntries(system, TcDirEntry.FILTER_ALL_TC_ENTRIES);
      	while (e.hasMoreElements()) {
      		TcDirEntry entry = (TcDirEntry) e.nextElement();
      		String path = entry.getDirEntryPath();
      		int i = path.indexOf("_SYSTEM.TT");
      		if (0 < i) {
      			system.setDirEntryPath(path.substring(0, i + 10));
      			if (index < rpcChunkLen.value) {
      				elems[index] = system;
      				return true;
      			}
      		}
      	}
      	return false;
      }

      private boolean loadInfo (TCI client, String[] dirEntryPaths, int nrOfHandles)
                           throws RPCException, IOException {
         synchronized (rpcTcGetDirEntryInfoListIn) {
            rpcTcGetDirEntryInfoListIn.dirEntryPaths_count = nrOfHandles;
            System.arraycopy(
                             dirEntryPaths, 0, rpcTcGetDirEntryInfoListIn.dirEntryPaths, 0,
                             nrOfHandles);
            client.RpcTcGetDirEntryInfoList_1(
                                              rpcTcGetDirEntryInfoListIn,
                                              rpcTcGetDirEntryInfoListOut);
            if (rpcTcGetDirEntryInfoListOut.retVal) {
               for (int i = 0; i < nrOfHandles; i++) {
                  elems[i].setInfo(rpcTcGetDirEntryInfoListOut.infos[i]);
                  if (checkSystem && !systemFound) {
                  	systemFound = elems[i].isSystem();
                  }
               }
               return true;
            }
            return false;
         }
      }

      private void getFirstChunk () {
         try {
            synchronized (rpcTcGetFirstDirEntryChunkIn) {
               rpcTcGetFirstDirEntryChunkIn.dirPath = ((TcRpcDirEntry) parent).getHandle();
               rpcTcGetFirstDirEntryChunkIn.kind.value = kind;
               client.client.RpcTcGetFirstDirEntryChunk_1(
                                                   rpcTcGetFirstDirEntryChunkIn,
                                                   rpcTcGetFirstDirEntryChunkOut);
               if (rpcTcGetFirstDirEntryChunkOut.retVal) {
                  index = 0;
                  nrOfHnd = rpcTcGetFirstDirEntryChunkOut.dirEntryPaths_count;
                  iterHandle = rpcTcGetFirstDirEntryChunkOut.iterHnd;
                  if (0 < nrOfHnd) {
                     for (int i = 0; i < nrOfHnd; i++) {
                        elems[i] =
                           new TcRpcDirEntry(rpcTcGetFirstDirEntryChunkOut.dirEntryPaths[i], client);
                     }
                     if (loadInfo(client.client, rpcTcGetFirstDirEntryChunkOut.dirEntryPaths, nrOfHnd)) {
                        return;
                     }
                  }
               }
            }
         } catch (Exception e) {
            System.out.println("Disconnect in TcDirectoryModel - DirEntryChunkEnumeration - getFirstChunk: ");
         }
         nrOfHnd = 0;
         for (int i = 0; i < elems.length; i++) {
            elems[i] = null;
         }
      }

      private void getNextChunk () {
         try {
            synchronized (rpcTcGetNextDirEntryChunkIn) {
               rpcTcGetNextDirEntryChunkIn.iterHnd = iterHandle;
               rpcTcGetNextDirEntryChunkIn.dirPath = ((TcRpcDirEntry) parent).getHandle();
               rpcTcGetNextDirEntryChunkIn.kind.value = kind;
               client.client.RpcTcGetNextDirEntryChunk_1(
                                                  rpcTcGetNextDirEntryChunkIn,
                                                  rpcTcGetNextDirEntryChunkOut);
               if (rpcTcGetNextDirEntryChunkOut.retVal) {
                  index = 0;
                  nrOfHnd = rpcTcGetNextDirEntryChunkOut.dirEntryPaths_count;
                  iterHandle = rpcTcGetNextDirEntryChunkOut.iterHnd;
                  if (0 < nrOfHnd) {
                     for (int i = 0; i < nrOfHnd; i++) {
                        elems[i] = new TcRpcDirEntry(rpcTcGetNextDirEntryChunkOut.dirEntryPaths[i], client);
                     }
                     if (loadInfo(client.client, rpcTcGetNextDirEntryChunkOut.dirEntryPaths, nrOfHnd)) {
                        return;
                     }
                  }
               }
            }
         } catch (Exception e) {
            System.out.println("Disconnect in TcDirectoryModel - DirEntryChunkEnumeration - getNextChunk: ");
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
      
      
      private ProgramAndUserProgramDirEntryChunkEnumeration (TcDirEntry parent) {
         programs = new DirEntryChunkEnumeration(parent, TcDirEntry.PROGRAM);
         userPrograms = new DirEntryChunkEnumeration(parent, TcDirEntry.USER_PROGRAM); 
      }
      public boolean hasMoreElements () {
         return programs.hasMoreElements() ? true: userPrograms.hasMoreElements();
      }
      
      public Object nextElement () {
         Object elem = programs.nextElement();
         if (elem != null) {
            return elem;
         }
         return userPrograms.nextElement();
      }
      
   }
}
