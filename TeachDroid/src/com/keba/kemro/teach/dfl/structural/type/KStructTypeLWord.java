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
import com.keba.kemro.teach.dfl.edit.*;

public class KStructTypeLWord extends KStructTypeLInt {
	KStructTypeLWord(String key, int visibility, KTcDfl dfl) {
		super(key.length() == 0 ? KEditKW.KW_LWORD: key, visibility, dfl);
	}
}
