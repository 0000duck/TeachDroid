/* -------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Projekt   : Kemro.teachview.r2
 *    Auftragsnr: 5500821
 *    Erstautor : sinn
 *    Datum     : 13.10.2004
 *------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl.structural;

import java.util.*;
import com.keba.kemro.teach.dfl.*;

public class KStructScope extends KStructProject {
   public KStructNodeVector projects = new KStructNodeVector(this);

   protected KStructScope (String key, KTcDfl dfl) {
      super(key, dfl);
   }


   /**
    * Initialisiert die Zuordnungstabellen eines Projekt-Knoten und wird aus
    * der Basisklasse überschrieben.
    */
   protected void init () {
      allConstants = new Hashtable(267);
      allTypes = new Hashtable(601);
      allRoutines = new Hashtable(1500);
      programs = new KStructNodeVector(this, 150);
      units = new KStructNodeVector(this, 150);
      constants = new KStructNodeVector(this, 100);
      types = new KStructNodeVector(this, 250);
      variables = new KStructNodeVector(this, 250);
      routines = new KStructNodeVector(this, 250);

   }
   /**
    * Löscht die untergeordneten Knoten
    */
   protected void clear () {
      projects.removeChildren();
      super.clear();
   }

   /**
    * Lädt alle untergeordneten Knoten (Programme, Konstanten, Typen, Variablen).
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
	      dfl.structure.loadProjects(this, false);
	      dfl.structure.endLoad(getKey());
      }
   }
}
