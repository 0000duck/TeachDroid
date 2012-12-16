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
import com.keba.kemro.teach.dfl.structural.constant.*;
import com.keba.kemro.teach.network.*;


/**
 * Strukturknoten für einen Feldtyp.
 */
public class KStructTypeArray extends KStructTypeAny {
   /** Gibt an, ob die Konstanten für die Feldgrenzen geladen sind. */
   protected final static int BIT_MASK_CONSTANTS_LOADED = BIT_MASK_LAST_KSTRUCT_TYPE_NODE * 2 * 2;
   /** Gibt an, wie die Feldgröße definiert ist. */
   protected final static int BIT_MASK_IS_SIZE_DEFINED =
      BIT_MASK_LAST_KSTRUCT_TYPE_NODE * 2 * 2 * 2;
   /** Legt die Felduntergrenze fest. */
   public Integer lowerBound = ZERO;
   /** Legt die Feldobergrenze fest. */
   public Integer upperBound = ZERO;
   private KStructConstLInt lowerKStructNode = null;
   private KStructConstLInt upperKStructNode = null;

   KStructTypeArray (String key, int visibility, KTcDfl dfl) {
      super(key.length() == 0 ? KEditKW.KW_ARRAY: key, visibility, false, dfl);
   }

   /**
    * Liefert den Strukturtyp der Feldelemente.
    *
    * @return Feldelementtyp
    */
   public KStructType getArrayElementKStructType () {
      checkReference();
      if (isAliasType()) {
         return null;
      }
      return baseType;
   }

   /**
    * Liefert wahr, wenn die untere Feldgrenze durch einen Konstante festgelegt
    * ist.
    *
    * @return Wahrheitswert
    */
   public boolean isLowerBoundConstant () {
      checkBoundsConstants();
      return (lowerKStructNode != null);
   }

   /**
    * Liefert wahr, wenn die obere Feldgrenze durch eine Konstante festgelegt
    * ist.
    *
    * @return Wahrheitswert
    */
   public boolean isUpperBoundConstant () {
      checkBoundsConstants();
      return (upperKStructNode != null);
   }

   /**
    * Liefert die Felduntergrenze.
    *
    * @return Felduntergrenze
    */
   public Integer getLowerBound () {
      checkBoundsConstants();
      return lowerBound;
   }

   /**
    * Liefert die Feldobergrenze
    *
    * @return Feldobergrenze
    */
   public Integer getUpperBound () {
      checkBoundsConstants();
      return upperBound;
   }

   /**
    * Liefert wahr, wenn das Feld durch fixe Größe definiert ist und falsch, wenn
    * das Feld durch ein Interval von...bis definiert ist.
    *
    * @return Wahrheitswert
    */
   public boolean isDefinedBySize () {
      return isBitSet(BIT_MASK_IS_SIZE_DEFINED);
   }

   /**
    * Setzt die Konstanten für die Feldgrenzen als geladen.
    */
   protected void setConstantsLoaded () {
      setBit(BIT_MASK_CONSTANTS_LOADED, true);
   }

   /**
    * Setzt die Bitmaske IS_SIZE_DEFINED. Das Feld ist bestimmt durch eine Konstante
    * und nicht durch ein Intervall.
    *
    * @param isDefinedbySize wahr, wenn Feld durch Konstante bestimmt ist.
    */
   protected void setIsDefinedBySize (boolean isDefinedbySize) {
      setBit(BIT_MASK_IS_SIZE_DEFINED, isDefinedbySize);
   }

   protected void checkReference () {
      if (!isBaseChecked()) {
      	super.checkReference();
         if (!isAliasType()) {
         	TcStructuralTypeNode baseTypeNode = ((TcStructuralTypeNode) ortsStructuralNode).getArrayElementType();
         	baseType = dfl.structure.getKStructType(baseTypeNode);
         	if (baseType == null) {
               baseType =
               	dfl.structure.typeFactory.createKStructType(baseTypeNode, dfl);
         	}
         }
      }
   }

   /**
    * Liefert wahr, wenn die Konstanten für die Feldgrenzen geladen sind.
    *
    * @return Wahrheitswert
    */
   private boolean constantsAreLoaded () {
      return isBitSet(BIT_MASK_CONSTANTS_LOADED);
   }

   /**
    * Description of the Method
    */
   private void checkBoundsConstants () {
      if (isAliasType()) {
         return;
      }
      if (constantsAreLoaded()) {
         return;
      }
      setConstantsLoaded();
      if (getTcStructuralNode() == null) {
         return;
      }
      TcStructuralConstNode lowerTcNode =
         ((TcStructuralTypeNode) getTcStructuralNode()).getLowerBoundConst();
      TcStructuralConstNode upperTcNode;
      if (
          (((TcStructuralTypeNode) getTcStructuralNode()).getLowerBound() == 0) &&
          (lowerTcNode == null)) {
         setIsDefinedBySize(true);
         upperTcNode = ((TcStructuralTypeNode) getTcStructuralNode()).getArraySizeConst();
         if (upperTcNode == null) {
            upperTcNode = ((TcStructuralTypeNode) getTcStructuralNode()).getUpperBoundConst();
            if (upperTcNode != null) {
               setIsDefinedBySize(false);
            }
         }
      } else {
         upperTcNode = ((TcStructuralTypeNode) getTcStructuralNode()).getUpperBoundConst();
         setIsDefinedBySize(false);
      }
      if (lowerTcNode != null) {
         lowerKStructNode = (KStructConstLInt) dfl.structure.getKStructConst(lowerTcNode);
      }
      if (upperTcNode != null) {
         upperKStructNode = (KStructConstLInt) dfl.structure.getKStructConst(upperTcNode);
      }
      if (lowerKStructNode != null) {
         lowerBound = new Integer(((Number) lowerKStructNode.getInitValue()).intValue());
      } else {
         lowerBound = new Integer(((TcStructuralTypeNode) getTcStructuralNode()).getLowerBound());
      }
      if (upperKStructNode != null) {
         upperBound = new Integer(((Number) upperKStructNode.getInitValue()).intValue());
         if (isDefinedBySize()) { // just on [n]
            upperBound = new Integer(upperBound.intValue() - 1);
         }
      } else {
         int ub = ((TcStructuralTypeNode) getTcStructuralNode()).getUpperBound();
         upperBound = new Integer(ub);
      }
   }
}
