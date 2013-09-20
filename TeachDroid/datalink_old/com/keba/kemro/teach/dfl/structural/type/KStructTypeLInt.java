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
package com.keba.kemro.teach.dfl.structural.type;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.edit.*;

/**
 * Strukturknoten für einen langer Ganzzahltyp (LINT 64 Bit).
 */
public class KStructTypeLInt extends KStructTypeSimple {

   KStructTypeLInt (String key, int visibility, KTcDfl dfl) {
      super(key.length() == 0 ? KEditKW.KW_LINT: key, visibility, dfl);
   }

   /**
    * @see com.keba.kemro.teach.dfl.structural.type.KStructTypeSimple#getDefaultLowerRange()
    */
   public Number getDefaultLowerRange () {
      return LOWER_LONG;
   }

   /**
    * @see com.keba.kemro.teach.dfl.structural.type.KStructTypeSimple#getDefaultUpperRange()
    */
   public Number getDefaultUpperRange () {
      return UPPER_LONG;
   }
}
