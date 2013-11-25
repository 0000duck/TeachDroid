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

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.routine.*;


/**
 * Strukturknoten für ein Programm.
 */
public class KStructProgram extends KStructNode {
   /** Vector for all public and private constants */
   public KStructNodeVector constants = new KStructNodeVector(this);
   /** Vector for all public and private types */
   public KStructNodeVector types = new KStructNodeVector(this);
   /** Vector for all public and private variables */
   public KStructNodeVector variables = new KStructNodeVector(this);
   /** Vector for all public and private routines */
   public KStructNodeVector routines = new KStructNodeVector(this);

   /**
    * Erzeugt einen Programm-Strukturknoten
    *
    * @param key Name des Programms
    */
   protected KStructProgram (String key, KTcDfl dfl) {
      super(key, dfl);
      setAllowsChildren(true);
   }

   /**
    * Liefert den Dateizugriffspfad des Programmstruktur-Knoten.
    *
    * @return Zeichenkette als Pfad
    */
   public String getDirEntryPath () {
      if (storedDirEntryPath != null) {
         return storedDirEntryPath;
      }
      storedDirEntryPath = ((KStructProject) getParent()).getDirEntryPath(this.getTcStructuralNode());
      return storedDirEntryPath;
   }
   
   
   /**
    * Speichert alle Save Variablen dieses Programmes.
    * @return true wenn erfolgreich
    */
   public boolean writeBackSaveValues(){
      if (ortsStructuralNode != null){
         return dfl.client.structure.writeBackSaveValues(ortsStructuralNode);
      }
      return false;
   }
   

   /**
    * Lädt die untergeordneten Knoten (Typen, Routinen, Konstanten, Variablen).
    */
   protected void loadChildren () {
      if (!isLoaded()) {
	      setLoaded(true);
	      dfl.structure.typeFactory.loadTypes(this);
	      dfl.structure.constFactory.loadConstants(this);
	      dfl.structure.variableFactory.loadVariables(this);
	      dfl.structure.routineFactory.loadRoutines(this);
      }
   }

   /**
    * Liefert die unbenannte Routine des Programmknoten
    *
    * @return unbenannte Routine
    */
   public KStructRoutine getUnNamedRoutine () {
      int count = routines.getChildCount();
      int i = 0;
      boolean found = false;
      KStructRoutine unNamedRoutine = null;
      while (!found && (i < count)) {
         unNamedRoutine = (KStructRoutine) routines.getChild(i);
         found = unNamedRoutine.isUnnamedRoutine();
         i++;
      }
      return found ? unNamedRoutine : null;
   }
}
