/*-------------------------------------------------------------------------
*                   (c) 1999 by KEBA Ges.m.b.H & Co
*                            Linz/AUSTRIA
*                         All rights reserved
*--------------------------------------------------------------------------
*    Projekt   : Kemro.teachview.r2
*    Auftragsnr: 5500806
*    Erstautor : sinn
*    Datum     : 02.03.2005
*------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl.structural.var;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.type.*;

/**
 * 32 bit variable
 */
public class KStructVarDWord extends KStructVarDInt {
	KStructVarDWord(String key, int visibility, byte kind,
			boolean isMapTo, boolean isComponent, KStructType type, KTcDfl dfl) {
		super(key, visibility, kind, isMapTo, isComponent, type, dfl);
	}
}
