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
 * Diese Klasse definiert einen Unterbereichstypen. Varieblen dieses Typs können
 * nur Werte annehmen die in diesem Bereich liegen.
 */
public class KStructTypeSubrange extends KStructTypeSimple {
   /** Flag für die Überprüfung von Range Konstanten */
   protected final static int BIT_MASK_CONSTANT_CHECKED = BIT_MASK_LAST_KSTRUCT_TYPE_NODE * 2;
   private Integer lowerBound = ZERO;
   private Integer upperBound = ZERO;
   private KStructConst lowerKStructNode = null;
   private KStructConst upperKStructNode = null;

   KStructTypeSubrange (String key, int visibility, KTcDfl dfl) {
      super(key.length() == 0 ? KEditKW.KW_SUBRANGE: key, visibility, dfl);
   }

   /**
    * Flag für die Überprüfung der Ranges auf Konstanten
    */
   private void setConstantsChecked () {
      setBit(BIT_MASK_CONSTANT_CHECKED, true);
   }

   /**
    * Flag für die Überprüfung der Ranges auf Konstanten
    *
    * @return True wenn auf Konstanten überprüft wured
    */
   private boolean isConstantsChecked () {
      return isBitSet(BIT_MASK_CONSTANT_CHECKED);
   }

   /**
    * Liefert die untere Grenze als Integer, egal ob über Konstante definiert oder nicht
    * 
    * @return die untere Grenze
    */
   public Integer getLowerBound () {
      checkBoundConstants();
      return lowerBound;
   }

   /**
   * Liefert die obere Grenze als Integer, egal ob über Konstante definiert oder nicht
   * 
   * @return die obere Grenze
   */
   public Integer getUpperBound () {
      checkBoundConstants();
      return upperBound;
   }

   /**
    * Description of the Method
    */
   private void checkBoundConstants () {
      if (isAliasType() || isConstantsChecked()) {
         return;
      }
      setConstantsChecked();
      if (ortsStructuralNode == null) {
         return;
      }

      // Just in case of processing ETypeAdmin.SIMPLE_SUBRANGE
      TcStructuralConstNode lowerTcNode =
         ((TcStructuralTypeNode) ortsStructuralNode).getLowerBoundConst();
      TcStructuralConstNode upperTcNode =
         ((TcStructuralTypeNode) ortsStructuralNode).getUpperBoundConst();
      if (lowerTcNode != null) {
         lowerKStructNode = dfl.structure.getKStructConst(lowerTcNode);
      }
      if (upperTcNode != null) {
         upperKStructNode = dfl.structure.getKStructConst(upperTcNode);
      }
      if (lowerKStructNode != null) {
         lowerBound = (Integer) lowerKStructNode.getInitValue();
      } else {
         lowerBound = new Integer(((TcStructuralTypeNode) ortsStructuralNode).getLowerBound());
      }
      if (upperKStructNode != null) {
         upperBound = (Integer) upperKStructNode.getInitValue();
      } else {
         upperBound = new Integer(((TcStructuralTypeNode) ortsStructuralNode).getUpperBound());
      }
   }

   /**
    * @see com.keba.kemro.teach.dfl.structural.type.KStructTypeSimple#getDefaultLowerRange()
    */
   public Number getDefaultLowerRange () {
      return getLowerBound();
   }

   /**
    * @see com.keba.kemro.teach.dfl.structural.type.KStructTypeSimple#getDefaultUpperRange()
    */
   public Number getDefaultUpperRange () {
      return getUpperBound();
   }
}
