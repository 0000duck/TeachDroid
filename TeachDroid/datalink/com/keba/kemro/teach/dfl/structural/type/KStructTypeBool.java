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
 * Strukturknoten für den Wahrheitstyp.
 */
public class KStructTypeBool extends KStructTypeSimple {

   KStructTypeBool (String key, int visibility, KTcDfl dfl) {
      super(key.length() == 0 ? KEditKW.KW_BOOL: key, visibility, dfl);
   }
}
