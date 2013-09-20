/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Project : Kemro.teachview.r2
 *------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl.value;

import com.keba.kemro.teach.dfl.*;


/**
 * This abstract class represents ths base class of a variable map target. The map target can be a internal
 * teachtalk variable, a internal tachtalk program or unit routine or a external variable. 
 */
public abstract class KMapTarget {
   protected String fullPath;
   protected KTcDfl dfl;

   protected KMapTarget (KTcDfl dfl) {
   	// do nothing
   	this.dfl = dfl;
   }
}
