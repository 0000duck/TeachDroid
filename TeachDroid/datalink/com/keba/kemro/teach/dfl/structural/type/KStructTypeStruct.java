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
package com.keba.kemro.teach.dfl.structural.type;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.edit.*;
import com.keba.kemro.teach.dfl.structural.*;

/**
 * Repräsentiert einen Strukturtypen.
 */
public class KStructTypeStruct extends KStructTypeAny {
   /** Komponenten */
   public KStructNodeVector components;

   KStructTypeStruct (String key, int visibility, boolean allowsChildren, KTcDfl dfl) {
      super(key.length() == 0 ? KEditKW.KW_STRUCT: key, visibility, allowsChildren, dfl);
      if (allowsChildren) {
         components = new KStructNodeVector(this);
      }
   }

   /**
    * Lädt alle Komponenten des Typs.
    */
   protected void loadChildren () {
      if (!isLoaded() && this.getAllowsChildren()) {
	      setLoaded(true);
	      dfl.structure.variableFactory.loadComponents(this);
      }
  }
}
