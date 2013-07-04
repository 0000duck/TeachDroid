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
 * Routinetyps
 */
public class KStructTypeRoutine extends KStructTypeAny {
   /** Liste mit den Parametern */
   public KStructNodeVector parameters;
   private KStructType returnType = null;


   KStructTypeRoutine (String key, int visibility, boolean allowsChildren, KTcDfl dfl) {
      super(key.length() == 0 ? KEditKW.KW_ROUTINE: key, visibility, allowsChildren, dfl);
      if (allowsChildren) {
      	parameters = new KStructNodeVector(this);
      }
   }

   /**
    * Liefert den Return Typ der Routine
    *
    * @return Return Typ der Routine
    */
   public KStructType getReturnType () {
      if ((returnType == null) && (ortsStructuralNode != null)) {
      	Object o = getTcStructuralNode();
      	if (o instanceof TcStructuralTypeNode) {
      		TcStructuralTypeNode n = (TcStructuralTypeNode)o;
      		returnType = dfl.structure.getKStructType(n.getReturnType());
      	}
      }
      return returnType;
   }

   /**
    * @see com.keba.kemro.teach.dfl.structural.KStructNode#loadChildren()
    */
   protected void loadChildren () {
      if (!isLoaded() && this.getAllowsChildren() && (baseType == null)) {
         setLoaded(true);
         dfl.structure.variableFactory.loadVariables(this);
      }
   }
}
