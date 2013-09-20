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
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.network.*;


/**
 * Die Klasse beschreibt die Basisklasse für die speziellen Typstrukturen und 
 * stellt die nötigen Funktionalitäten zur Typbearbeitung zur Verfügung.
 */
public class KStructType extends KStructNode {
   /** Konstante für die Bearbeitung der Bitmaske: Basistyp ist überprüft */
   protected final static int BIT_MASK_BASE_TYPE_CHECKED = BIT_MASK_LAST * 2;
   /** Konstante für die Bearbeitung der Bitmaske: Typ ist ein Aliastyp */
   protected final static int BIT_MASK_IS_ALIAS = BIT_MASK_LAST * 2 * 2;
   /** Konstante für die Bearbeitung der Bitmaske: letztes Bit */
   protected final static int BIT_MASK_LAST_KSTRUCT_TYPE_NODE = BIT_MASK_IS_ALIAS;
   /** Konstante für Zeichenkette UNDEFINED */
   protected final static String DATA_TYPE_NAME_UNDEFINED = "UNDEFINED";
   /** Referenz auf den Basistyp */
   protected KStructType baseType = null;

   KStructType (String key, int visibility, boolean allowsChildren, KTcDfl dfl) {
      super(key, dfl);
      setAllowsChildren(allowsChildren);
      setLoaded(!allowsChildren);
      this.setVisibility(visibility);
   }

   protected void setTcStructuralNode (TcStructuralNode n) {
   	super.setTcStructuralNode(n);
   }

   /**
    * Setzt den übergeordneten Knoten
    *
    * @param newParent übergeordneten Knoten (Vaterknoten)
    */
   protected void setParent (KStructNode newParent) {
      super.setParent(newParent);
      if (
          (parent instanceof KStructTypeArray) && !(this instanceof KStructTypeRoutine) &&
          !(this instanceof KStructTypeArray)) {
         setLoaded(true);
         setAllowsChildren(false);
      }
   }

   /**
    * Liefert wahr, wenn der Typ ein Alias-Typ (umbenannter Typ) ist.
    *
    * @return Wahrheitswert
    */
   public boolean isAliasType () {
      checkReference();
      return isBitSet(BIT_MASK_IS_ALIAS);
   }

   /**
    * Setzt den Typ als Alias-Typ
    *
    * @param isAlias wahr, wenn der Typ ein Alias-Typ ist.
    */
   protected void setIsAliasType (boolean isAlias) {
      setBit(BIT_MASK_IS_ALIAS, isAlias);
   }

   /**
    * Liefert den Basistyp zurück, wenn keiner vorhanden ist wird null zurück
    * gegeben.
    *
    * @return Basistyp
    */
   public KStructType getKStructType () {
      checkReference();
      if (isAliasType()) {
      	return baseType;
      }
      return null;
   }

   /**
    * Setzt das BASE_TYPE_CHECKED-Bit in der Bitmaske.
    */
   protected void setBaseChecked () {
      setBit(BIT_MASK_BASE_TYPE_CHECKED, true);
   }

   /**
    * Liefert das BASE_TYPE_CHECKED-Bit aus der Bitmaske.
    *
    * @return BASE_TYPE_CHECKED-Bit
    */
   protected boolean isBaseChecked () {
      return isBitSet(BIT_MASK_BASE_TYPE_CHECKED);
   }

   /**
    * der Basistyp wird aktualisiert.
    */
   protected void checkReference () {
      if (!isBaseChecked()) {
      	setBaseChecked();
         TcStructuralTypeNode baseTypeNode = ((TcStructuralTypeNode) ortsStructuralNode).getBaseType();
         setIsAliasType((baseTypeNode != null) && (baseTypeNode.getName()!=null) && (baseTypeNode.getName().length() > 0));
         if ((baseTypeNode != null) && isAliasType()) {
         	baseType = dfl.structure.getKStructType(baseTypeNode);
         	if (baseType == null) {
         		if (baseTypeNode.getBaseType() == null) {
                  if (this instanceof KStructTypeArray) {
                     baseType =
                     	dfl.structure.typeFactory.createKStructType(baseTypeNode, dfl);
                  } else if (this instanceof KStructTypeMapTo) {
                     baseType = dfl.structure.typeFactory.createKStructType(baseTypeNode, dfl);
                  }         			
         		} else {
                  baseType =
                  	dfl.structure.typeFactory.createKStructType(baseTypeNode, dfl);        			
         		}
         	}
         }
      }
   }
}
