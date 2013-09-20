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
 * This class represents a external map target. 
 */
public class KMapTargetExternal extends KMapTarget {

	/**
    * Constructor for a external variable map target.
    *     
    * @param name name of external variable
    */
   KMapTargetExternal (String name, KTcDfl dfl) {
   	super(dfl);
		this.fullPath = name;
   }
   
   /**
    * Returns the name of the external variable.
    * @return name of the external variable
    */
   public String getName () {
      return fullPath;
   }

}
