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
package com.keba.kemro.teach.dfl.dir;

import com.keba.kemro.teach.network.*;
import java.util.*;


/**
 * Wrapperobjekt für TcDirEntry
 */
public class KDirEntry {
   /** Project entry, ".tt" directory */
   public static final int PROJECT = TcDirEntry.PROJECT;
   /** Unit entry, ".tts" file */
   public static final int UNIT = TcDirEntry.UNIT;
   /** Program entry, ".ttp" file*/
   public static final int PROGRAM = TcDirEntry.PROGRAM;
   /** Data file contains the save values, ".ttd" file */
   public static final int DATA = TcDirEntry.DATA;
   /** End User program, ".tip" file */
   public static final int USER_PROGRAM = TcDirEntry.USER_PROGRAM;
   /** End User variable declaration file, ".tid" file */
   public static final int USER_VAR = TcDirEntry.USER_DATA;
   /** Unknown directory entry */
   public static final int UNKOWN = TcDirEntry.UNKOWN;
	
	
   private TcDirEntry dirEntry;

   /**
    * Legt einen neue KDirEntry an
    *
    * @param tcDirEntry Der korrespondierende TcDirEntry
    */
   public KDirEntry (TcDirEntry tcDirEntry) {
      dirEntry = tcDirEntry;
   }

   /**
    * Liefert den Namen des KDirEntry
    *
    * @return Der Name des DirEntry
    */
   public String getName () {
      return dirEntry.getName();
   }

   /**
    * Liefert den Typ des Entries
    *
    * @return Liefert den Typ des Entries
    */
   public int getKind () {
      return dirEntry.getKind();
   }

   /**
    * Liefert die Größe der Datei auf der Festplatte
    *
    * @return Größe der Datei
    */
   public int getSize () {
      return dirEntry.getSize();
   }

   /**
    * Liefert den Pfad des Verzeichniseintrages
    *
    * @return Der Pfad des Verzeichniseintrages
    */
   public String getDirEntryPath () {
      return dirEntry.getDirEntryPath();
   }

   /**
    * Liefert den Erstellungszeitpunkt
    *
    * @return Zeitpunkt des Erstellens
    */
   public Date getCreationDate () {
      return dirEntry.getCreatedDate();
   }

   /**
    * Liefert den Zeipunkt der letzten gespeicherten Änderung
    *
    * @return Zeipunkt der letzten Änderung
    */
   public Date getModifiedDate () {
      return dirEntry.getModifiedDate();
   }

   /**
    * Liefert true wenn is sich um den KDirEntry des Global Projektes handelt
    *
    * @return True wenn Globales Projekt
    */
   public boolean isGlobalProject () {
      return dirEntry.isGlobal();
   }
   /**
    * Liefert true wenn is sich um den KDirEntry des Global Projektes handelt
    *
    * @return True wenn Globales Projekt
    */
   public boolean isSystemProject () {
      return dirEntry.isSystem();
   }

   /**
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals (Object o) {
     if (this == o) {
    	 return true;
     }
     if (!(o instanceof KDirEntry)) {
    	 return false;
     }
  	 return dirEntry.equals(((KDirEntry)o ).getTcDirEntry());
   }

   public int hashCode () {
   	return dirEntry.hashCode();
   }

   /**
    * Liefert den gepeicherten TcDirEntry 
    *
    * @return den TcDirEntry
    */
   public TcDirEntry getTcDirEntry () {
      return dirEntry;
   }
}
