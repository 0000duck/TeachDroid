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
package com.keba.kemro.teach.dfl.execution;

import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.network.*;

public class KExecUnitSystem extends KExecUnitScope {
   KExecUnitSystem (String name, TcExecutionUnit ortsExecutionUnit, KStructNode structNode,
         TcExecutionState state, KExecUnitNode parent) {
      super(name, ortsExecutionUnit, structNode, state, parent);
	}
}
