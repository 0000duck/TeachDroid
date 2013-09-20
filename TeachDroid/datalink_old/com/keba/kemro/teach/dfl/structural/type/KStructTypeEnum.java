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
package com.keba.kemro.teach.dfl.structural.type;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.edit.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.constant.KStructConstEnum;


/**
 * Strukturknoten für den Aufzählungstyp.
 */
public class KStructTypeEnum extends KStructTypeAny {
   /** Vektor für die Aufzählungskonstanten */
   public KStructNodeVector constants;

   KStructTypeEnum (String key, int visibility, boolean allowsChildren, KTcDfl dfl) {
      super(key.length() == 0 ? KEditKW.KW_ENUMERATION: key, visibility, allowsChildren, dfl);
      if (allowsChildren) {
         constants = new KStructNodeVector(this);
      }
   }

   /**
    * @see com.keba.kemro.teach.dfl.structural.KStructNode#loadChildren()
    */
    protected void loadChildren () {
      if (!isLoaded() && getAllowsChildren()) {
	      setLoaded(true);
	      dfl.structure.constFactory.loadConstants(this);
      }
   }
    
  /**
   * Searches for enum constant with the given value
   */
   public KStructConstEnum GetEnumConst(Integer enumValue) {
        KStructNodeVector  enumConstVector = constants;
        for (int i = 0; i < enumConstVector.getChildCount(); i++) {
           KStructConstEnum enumConst = (KStructConstEnum) enumConstVector.getChild(i);
           int initValue = ((Integer)enumConst.getInitValue()).intValue();
           if (initValue == enumValue.intValue()) {
              return enumConst;
           }
        } 
        return null;
    }    
   
   
   /**
    * Searches for enum constant with the given value
    */
    public KStructConstEnum GetEnumConst(int enumValue) {
         KStructNodeVector  enumConstVector = constants;
         for (int i = 0; i < enumConstVector.getChildCount(); i++) {
            KStructConstEnum enumConst = (KStructConstEnum) enumConstVector.getChild(i);
            int initValue = ((Integer)enumConst.getInitValue()).intValue();
            if (initValue == enumValue) {
               return enumConst;
            }
         } 
         return null;
     } 
}
