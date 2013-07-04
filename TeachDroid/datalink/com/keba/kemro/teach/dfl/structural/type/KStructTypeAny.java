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
package com.keba.kemro.teach.dfl.structural.type;

import com.keba.kemro.teach.dfl.*;

/**
 * any type
 */
public class KStructTypeAny extends KStructType {
	KStructTypeAny(String key, int visibility, boolean allowsChildren, KTcDfl dfl) {
		super(key.length() == 0 ? "ANY": key, visibility, allowsChildren, dfl);
	}
}
