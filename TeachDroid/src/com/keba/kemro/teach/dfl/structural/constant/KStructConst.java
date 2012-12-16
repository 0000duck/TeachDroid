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
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.type.*;
import com.keba.kemro.teach.network.*;


/**
 * Ist die Basisklasse aller Konstanten im Strukturbaum.
 */
public abstract class KStructConst extends KStructNode {
   /** Konstantenwert */
   protected Object m_initValue = null;
   protected KStructType m_type;


   protected KStructConst (String key, KStructType type, int visibility, KTcDfl dfl) {
      super(key, dfl);
      setVisibility(visibility);
      m_type = type;
   }

   protected void setTcStructuralNode (TcStructuralNode n) {
   	super.setTcStructuralNode(n);
   }

   /**
    * Liefert den Typ der Konstante.
    *
    * @return Typ
    */
   public KStructType getKStructType () {
      return m_type;
   }

   /**
    * Liefert den Konstantenwert.
    *
    * @return Konstantenwert
    */
   public abstract Object getInitValue ();

}
