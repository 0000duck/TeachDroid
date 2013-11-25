/*-------------------------------------------------------------------------
*                   (c) 1999 by KEBA Ges.m.b.H & Co
*                            Linz/AUSTRIA
*                         All rights reserved
*--------------------------------------------------------------------------
*    Projekt   : KEMRO.teachview.4
*    Auftragsnr: 5500395
*    Erstautor : tur
*    Datum     : 01.04.2003
*--------------------------------------------------------------------------
*      Revision:
*        Author:
*          Date:
*------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl.structural;

import java.io.*;
import java.util.*;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.compilerError.*;
import com.keba.kemro.teach.network.*;


/**
 * Strukturknoten für ein Projekt.
 */
public class KStructProject extends KStructNode {
   /** Bitmakse für Compiler-Fehler */
   protected final static int BIT_MASK_HAS_COMPILE_ERROR = KStructNode.BIT_MASK_LAST * 2;
   /** Strukturknotenvektor für Programme */
   public KStructNodeVector programs;
   /** Strukturknotenvektor */
   public KStructNodeVector units;
   /** Vector for all global constants */
   public KStructNodeVector constants;
   /** Vector for all global types */
   public KStructNodeVector types;
   /** Vector for all global variables */
   public KStructNodeVector variables;
   /** Vector for all global routines */
   public KStructNodeVector routines;

   
   /** Zuordnungstabelle für Programme und Bausteine */
   public Hashtable programsAndUnits = new Hashtable(201);
   /** Zuordnungstabelle für alle Konstanten */
   public Hashtable allConstants;
   /** Zuordnungstabelle für alle Typen */
   public Hashtable allTypes;
   /** Zuordnungstabelle für alle Routinen */
   public Hashtable allRoutines;
   
   private final Hashtable dirEntryPaths = new Hashtable(201);

   /**
    * Erzeugt einen Projekt-Strukturknoten
    *
    * @param key Name des Projektes
    */
   protected KStructProject (String key, KTcDfl dfl) {
      super(key, dfl);
      setAllowsChildren(true);
      init();
   }

   /**
    * Initialisiert die Zuordnungstabellen eines Projekt-Knoten.
    */
   protected void init () {
      allConstants = new Hashtable(41);
      allTypes = new Hashtable(201);
      allRoutines = new Hashtable(601);
      programs = new KStructNodeVector(this, 75);
      units = new KStructNodeVector(this, 150);
      constants = new KStructNodeVector(this, 100);
      types = new KStructNodeVector(this, 250);
      variables = new KStructNodeVector(this, 250);
      routines = new KStructNodeVector(this, 250);
   }

   /**
    * Setzt dem Knoten den Zustand, dass er Compiler-Fehler aufweist.
    *
    * @param b Compiler-Fehler Zustand
    */
   protected void setHasCompileError (boolean b) {
      setBit(BIT_MASK_HAS_COMPILE_ERROR, b);
   }
   
   
   /**
    * Speichert alle Save Variablen dieses Projektes
    * @return true wenn erfolgreich
    */
   public boolean writeBackSaveValues(){
      if (ortsStructuralNode != null){
         return dfl.client.structure.writeBackSaveValues(ortsStructuralNode);
      }
      return false;
   }

   /**
    * Liefert den Dateizugriffspfad des Projekt-Strukturknoten.
    *
    * @return Zeichenkette als Pfad
    */
   public String getDirEntryPath () {
      if (storedDirEntryPath != null) {
         return storedDirEntryPath;
      }
   	TcDirEntry entry = dfl.client.structure.getDirEntry(getTcStructuralNode());
   	if (entry != null) {
   		storedDirEntryPath = entry.getDirEntryPath();
   	} else {
   		storedDirEntryPath = File.separator + getKey().toUpperCase() + KTcDfl.PROJECT_DIR_EXTENSION;
   	}
      return storedDirEntryPath;
   }

   String getDirEntryPath (TcStructuralNode dirEntryNode) {
      String path = (String) dirEntryPaths.get(dirEntryNode);
      if (path == null) {
      	TcDirEntry entry = dfl.client.structure.getDirEntry(dirEntryNode);
      	if (entry != null) {
         	path = entry.getDirEntryPath();      		
      	} else {
      		path = getDirEntryPath() + File.separator + dirEntryNode.getName().toUpperCase() + KTcDfl.PROJECT_DIR_EXTENSION;
      	}
      	dirEntryPaths.put(dirEntryNode, path);
      }
      return path;
   }
   /**
    * Prüft auf Compiler-Fehler
    *
    * @return Wahrheitswert
    */
   public boolean hasCompileError () {
      return isBitSet(BIT_MASK_HAS_COMPILE_ERROR);
   }

   /**
    * Returns true is the source files of the project have changed since last project build.
    * @return true if the source files of the project have changed
    */
   public boolean projectSourcesChanged () {
   	return dfl.client.structure.projectSourcesChanged(getTcStructuralNode());
   }
   /**
    * Lädt die untergeordneten Knoten des Projektes
    *
    */
   protected void loadChildren () {
      if (!isLoaded()) {
	      setLoaded(true);
			dfl.structure.startLoad();
			dfl.structure.loadPrograms(this);
	      if (!hasFatalCompilerError()) {
	      	dfl.structure.typeFactory.loadTypes(this);
		      dfl.structure.constFactory.loadConstants(this);
		      dfl.structure.variableFactory.loadVariables(this);
		      dfl.structure.routineFactory.loadRoutines(this);
	      }
	      dfl.structure.endLoad(getKey());
      }
   }

   protected boolean hasFatalCompilerError () {
   	if (hasCompileError()) {
   		KCompilerErrorList list = dfl.error.getCompilerErrorList(this);
   		// CR 16561: ErrorNr == 99 is a CRC - check sum error therefor the project should
   		// be loaded anyway
   		return (1 < list.getCompilerErrorCount()) || list.getCompilerError(0).getErrorNr() != 99;
   	}
   	return false;
   }
   /**
    * Löscht die untergeordneten Knoten (Programme, Bausteine, Konstanten,
    * Typen, Routinen)
    */
   protected void clear () {
      programs.removeChildren();
      units.removeChildren();
      constants.removeChildren();
      types.removeChildren();
      variables.removeChildren();
      routines.removeChildren();
      programsAndUnits.clear();
      allConstants.clear();
      allTypes.clear();
      allRoutines.clear();
      dirEntryPaths.clear();
   }
}
