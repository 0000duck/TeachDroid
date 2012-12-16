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
import com.keba.kemro.teach.network.*;


/**
 * Flieﬂkommakonstante
 */
public class KStructConstReal extends KStructConst {
   private static final Float defaultValue = new Float(0);

   KStructConstReal (String key, KStructType type, int visibility, KTcDfl dfl) {
      super(key, type, visibility, dfl);
      setLoaded(true);
      setAllowsChildren(false);
   }

   /**
    * @see com.keba.kemro.teach.dfl.structural.constant.KStructConst#getInitValue()
    */
   public Object getInitValue () {
      if (m_initValue == null) {
         if (ortsStructuralNode == null) {
            m_initValue = defaultValue;
         } else {
            TcValue value =
               ((TcStructuralConstNode) this.getTcStructuralNode()).getValue();
            m_initValue = new Float(value.floatValue);
         }
      }
      return m_initValue;
   }
}
