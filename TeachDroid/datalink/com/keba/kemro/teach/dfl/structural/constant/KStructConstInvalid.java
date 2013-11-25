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
package com.keba.kemro.teach.dfl.structural.constant;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.type.*;

/**
 * Repräsentiert eine ungültige Konstantendeklaration.
 */
public class KStructConstInvalid extends KStructConst {
	KStructConstInvalid(String key, KStructType type, int visibility, KTcDfl dfl) {
		super(key, type, visibility, dfl);
		setLoaded(true);
	}

	public Object getInitValue() {
		return null;
	}
}
