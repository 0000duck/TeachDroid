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
import com.keba.kemro.teach.network.*;



/**
 * Strukturknoten für einen MapTo-Typ
 */
public class KStructTypeMapTo extends KStructTypeAny {

	KStructTypeMapTo (KTcDfl dfl) {
      super(KEditKW.KW_MAPTO, KStructNode.PRIVATE, false, dfl);
   }

   /**
    * @see com.keba.kemro.teach.dfl.structural.type.KStructType#getKStructType()
    */
   public KStructType getKStructType () {
      checkReference();
      return baseType;
   }

   public boolean isAliasType () {
      return false;
   }

   protected void checkReference () {
      if (!isBaseChecked()) {
      	setBaseChecked();
         TcStructuralTypeNode baseTypeNode = ((TcStructuralTypeNode) ortsStructuralNode).getBaseType();
         if (baseTypeNode != null) {
         	baseType = dfl.structure.getKStructType(baseTypeNode);
         	if (baseType == null) {
         		baseType = dfl.structure.typeFactory.createKStructType(baseTypeNode, dfl);
         	}
         }
      }
   }
}
