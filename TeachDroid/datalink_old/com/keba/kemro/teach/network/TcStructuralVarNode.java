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
package com.keba.kemro.teach.network;

/**
 * TcStructuralVarNode repräsentiert eine Variable im TeachControl.
 *
 * @see TcStructuralModel
 * @see TcStructuralNode
 * @see TcStructuralRoutineNode
 * @see TcStructuralTypeNode
 */
public interface TcStructuralVarNode extends TcStructuralNode {
   /** Formal - Parameter - Variable */
   public static final byte PARAM_KIND = 0;
   /** Variable innerhalb eines Sichtbarkeitsbereiches. */
   public static final byte VAR_KIND = 1;
   /** Formal - Constant - Parameter - Variable */
   public static final byte CONST_PARAM_KIND = 2;
   /** Formal - Value - Parameter - Variable */
   public static final byte VALUE_PARAM_KIND = 3;
   /** Constant Variable */
   public static final byte CONST_VAR_KIND = 4;

   /**
    * Liefert die Variablenart (VAR, PARAM, CONST_PARAM oder VALUE_PARAM) zurück.
    *
    * @return Variablenart
    */
   public byte getVarKind ();

   /**
    * Liefert den Typ der Variable zurück.
    *
    * @return Variablentyp
    */
   public TcStructuralTypeNode getType ();

   /**
    * Gibt die Sichtbarkeit an.
    *
    * @return true für eine Pulic - Variable
    */
   public boolean isPublic () ;


   /**
    * Liefert true für eine Save-Variable zurück.
    *
    * @return true für eine Save-Variable
    */
   public boolean isSave ();

   
   /**
    * Returns true if the variable is a optional parameter.
    * @return true for a optional parameter
    */
   public boolean isOptional ();
   
   
   /**
    * Returns true if the variable is a optional parameter.
    * @return true for a optional parameter
    */
   public boolean isReadOnly ();

   /**
    * Returns true if the variable is referenced.
    * @return true if the variable is referenced
    */
   public boolean isReferenced ();
   
   /**
    * Returns true if the variable is defined as export.
    * @return true if the variable is defined as export.
    */
   public boolean isExportVariable ();
}
