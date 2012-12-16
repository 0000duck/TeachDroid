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
 * Short Integer mit 8 Bit
 */
public class KStructTypeSInt extends KStructTypeInt {

   KStructTypeSInt (String key, int visibility, KTcDfl dfl) {
      super(key.length() == 0 ? KEditKW.KW_SINT: key, visibility, dfl);
   }

   /**
    * @see com.keba.kemro.teach.dfl.structural.type.KStructTypeSimple#getDefaultLowerRange()
    */
   public Number getDefaultLowerRange () {
      return LOWER_BYTE;
   }

   /**
    * @see com.keba.kemro.teach.dfl.structural.type.KStructTypeSimple#getDefaultUpperRange()
    */
   public Number getDefaultUpperRange () {
      return UPPER_BYTE;
   }
}
