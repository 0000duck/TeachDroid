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
import com.keba.kemro.teach.dfl.structural.type.*;

public class KStructRoot extends KStructScope {
   
   protected KStructRoot (String key, KTcDfl dfl) {
      super(key, dfl);
   }
   
   public String getDirEntryPath () {
      return m_separator;
   }

   protected void init () {
      allConstants = new Hashtable(7);
      allTypes = new Hashtable(13);
      allRoutines = new Hashtable(29);
      programs = new KStructNodeVector(this, 3);
      units = new KStructNodeVector(this, 3);
      constants = new KStructNodeVector(this, 3);
      types = new KStructNodeVector(this, 13);
      variables = new KStructNodeVector(this, 3);
      routines = new KStructNodeVector(this, 29);
   }
   
   protected void loadChildren () {
      if (!isLoaded()) {
	      setLoaded(true);
	      KTypeFactory.loadSimpleTypes(this);
	      dfl.structure.routineFactory.loadRoutines(this);
	      dfl.structure.loadProjects(this, false);
      }
   }
   
}
