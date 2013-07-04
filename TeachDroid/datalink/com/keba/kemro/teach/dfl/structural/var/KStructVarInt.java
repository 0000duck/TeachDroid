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
package com.keba.kemro.teach.dfl.structural.var;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.type.*;


/**
 * Strukturknoten für eine DINT-Variable
 */
public class KStructVarInt extends KStructVarDInt {

   KStructVarInt (String key, int visibility, byte kind,
                            boolean isMapTo, boolean isComponent, KStructType type, KTcDfl dfl) {
      super(key, visibility, kind, isMapTo, isComponent, type, dfl);
   }
}
