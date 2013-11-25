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
package com.keba.kemro.teach.network;

import java.util.*;


/**
 * TcDirEntry repräsentiert einen Verzeichniseintrag im TeachControl. Der Eintrag
 * besitzt einen Namen, eine Art (Projekt, Programm oder Baustein), ein Erstellungs-, 
 * Zugriffs- und Veränderungsdatum.
 *
 * @see TcRpcDirectoryModel
 */
public abstract class TcDirEntry {
   /** Project entry, ".tt" directory */
   public static final int PROJECT = 0;
   /** Unit entry, ".tts" file */
   public static final int UNIT = 1;
   /** Program entry, ".ttp" file*/
   public static final int PROGRAM = 2;
   /** Data file contains the save values, ".ttd" file */
   public static final int DATA = 3;
   /** Unknown directory entry */
   public static final int UNKOWN = 5;
   /** End User program, ".tip" file */
   public static final int USER_PROGRAM = 6;
   /** End User variable declaration file, ".tid" file */
   public static final int USER_DATA = 7;
   /** Archive entry, ".tta */
   public static final int ARCHIVE = 11;
   
   /** Iteration filter for all known entries, ".tt", ".ttp", ".tts", ".ttd", ".tip" and ".tid" */
   public static final int FILTER_ALL_TC_ENTRIES = 4;
   /** Iteration filter for all end user entries, ".tip", ".tid" */
   public static final int FILTER_ALL_USER = 8;
   /** Iteration filter for program and data entries, ".ttp" and ".ttd" */
   public static final int FILTER_ALL_PROGRAM = 9;
   /** all directory entries */
   public static final int FILTER_ALL = 10;
   
   /** Iteration filter for all programs and end user programs, ".ttp" and ".tip" */
   public static final int FILTER_PROGRAM_AND_USER_PROGRAM = 100;

   
   private static int upCaseLength = 256;
   private static final char diff = 'a' - 'A';
   private static char[] upCase = new char[upCaseLength];

   protected String handle;
   private String path;
   protected String name;
   protected int kind;
   protected long createdTime;
   protected long modifiedTime;
   protected long accessTime;
   private Date createdDate;
   private Date modifiedDate;
   private Date accessDate;
   protected int size;
   protected boolean isGlobal;
   protected boolean isSystem;
   protected boolean loaded;

   /**
    * Konstruktor des Verzeichniseintrags.
    *
    * @param handle gibt den relativen Pfad von der Wurzel des TeachControl -
    *        Verzeichnisses bis zum Eintrag an.
    */
   protected TcDirEntry (String handle) {
	   if (handle.startsWith("/")){
		   handle = handle.substring(1);
	   }
       this.handle = handle;
   }

   /**
    * Liefert den TeachControl - Handle zurück.
    *
    * @return TeachControl - Handle
    */
   protected String getHandle () {
      return handle;
   }
   
   protected static String convertHandleToDirEntryPath (String handle) {
      synchronized (upCase) {
         int len = handle.length() + 1;
         char ch;
         if (len > upCaseLength) {
            upCaseLength = len;
            upCase = new char[upCaseLength];
         }
         handle.getChars(0, len - 1, upCase, 1);
         for (int i = 1; i < len; i++) {
            ch = upCase[i];
            if ((ch >= 'a') && (ch <= 'z')) {
               upCase[i] = (char) (ch - diff);
            } else if (ch == '/') {
               upCase[i] = java.io.File.separatorChar;
            }
         }
         if (upCase[1] == java.io.File.separatorChar) {
            return new String(upCase, 1, len - 1);
         }
         upCase[0] = java.io.File.separatorChar;
         return new String(upCase, 0, len);
      }
   }

   protected static String convertDirEntryPathToHandle (String dirEntryPath) {
      return dirEntryPath.replace(java.io.File.separatorChar, '/');
   }

   /**
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals (Object o) {
     if (this == o) {
    	 return true;
     }
     if (!(o instanceof TcDirEntry)) {
    	 return false;
     }
  	 return handle.equalsIgnoreCase(((TcDirEntry) o).handle);
   }

   public int hashCode () {
   	return handle.hashCode();
   }

   /**
    * Liefert den relativen Pfad des Verzeichniseintrages zurück.
    *
    * @return relativen Pfad
    */
   public String getDirEntryPath () {
      if (path == null) {
         path = TcDirEntry.convertHandleToDirEntryPath(handle);
      }
      return path;
   }

   protected void setDirEntryPath (String path) {
   	this.path = path;
   }
   /**
    * Liefert den Namen des Verzeichniseintrages ohne Teachtalk - Dateierweiterung zurück.
    *
    * @return Namen des Verzeichniseintrages
    */
   public String getName () {
      if (loadInfo()) {
         return name;
      }
      return null;
   }

   /**
    * Liefert die Art (Projekt, Programm oder Baustein) des Verzeichniseintrages zurück.
    *
    * @return Art des Verzeichniseintrages
    */
   public int getKind () {
      if (loadInfo()) {
         return kind;
      }
      return UNKOWN;
   }

   /**
    * Liefert das Erstellungsdatum zurück.
    *
    * @return Ddas Erstellungsdatum
    */
   public Date getCreatedDate () {
      if (loadInfo()) {
         if (createdDate == null) {
            createdDate = new Date(createdTime);
         }
         return createdDate;
      }
      return null;
   }

   /**
    * Liefert das Änderungsdatum zurück.
    *
    * @return das Änderungsdatum
    */
   public Date getModifiedDate () {
      if (loadInfo()) {
         if (modifiedDate == null) {
            modifiedDate = new Date(modifiedTime);
         }
         return modifiedDate;
      }
      return null;
   }

   /**
    * Liefert das Zugriffsdatum zurück.
    *
    * @return das Zugriffsdatum
    */
   public Date getAccessDate () {
      if (loadInfo()) {
         if (accessDate == null) {
            accessDate = new Date(accessTime);
         }
         return accessDate;
      }
      return null;
   }

   /**
    * Liefert die Dateigröße zurück.
    *
    * @return die Dateigröße
    */
   public int getSize () {
      if (loadInfo()) {
         return size;
      }
      return 0;
   }

   /**
    * Gibt an ob das Verzeichnis das global Verzeichnis ist.
    *
    * @return true wenn das Verzeichnis das global Verzeichnis ist
    */
   public boolean isGlobal () {
      if (loadInfo()) {
         return isGlobal;
      }
      return false;
   }
   
   /**
    * Gibt an ob das Verzeichnis das system Verzeichnis ist.
    *
    * @return true wenn das Verzeichnis das global Verzeichnis ist
    */
   public boolean isSystem () {
      if (loadInfo()) {
         return isSystem;
      }
      return false;
   }

   /**
    * Erzwingt das Nachladen des Knoteninhaltes beim nächsten Zugriff.
    */
   public void forceReload () {
		loaded = false;
	   createdDate = null;
	   modifiedDate = null;
	   accessDate = null;
	}
   
   protected abstract boolean loadInfo();
}
