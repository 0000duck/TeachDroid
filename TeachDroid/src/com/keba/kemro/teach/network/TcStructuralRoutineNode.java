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
 * TcStructuralRoutineNode repr�sentiert eine Routine im TeachControl.
 *
 * @see TcStructuralModel
 * @see TcStructuralNode
 * @see TcStructuralTypeNode
 * @see TcStructuralVarNode
 */
public interface TcStructuralRoutineNode extends TcStructuralNode {
   /** unbenannte Routine */
   public static final byte UNNAMED_ROUTINE = 0;
   /** benannte Routine */
   public static final byte NAMED_ROUTINE = 1;
   /** Ereignisroutine */
   public static final byte AT_ROUTINE = 2;


   /**
    * Liefert die Routinenart (UNNAMED_ROUTINE, NAMED_ROUTINE oder AT_ROUTINE) zur�ck.
    *
    * @return Routinenart
    */
   public byte getRoutineKind ();

   /**
    * Liefert den R�ckgabetyp der Routine zur�ck.
    *
    * @return Typ
    */
   public TcStructuralTypeNode getReturnType ();

   /**
    * Liefert die Ereignisvariable einer Ereignisroutine zur�ck.
    *
    * @return Ereignisvariable
    */
   public TcStructuralVarNode getEventVariable ();

   /**
    * Liefert die Anzahl der Statements zur�ck.
    *
    * @return Anzahl der Statements
    */
   public int getNrOfStatements ();

   /**
    * Gibt die Sichtbarkeit an.
    *
    * @return true f�r eine Public - Routine
    */
   public boolean isPublic ();
}
