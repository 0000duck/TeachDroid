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
 * TcExecutionUnit repr�sentiert ein Ausf�hrungseinheit im TeachControl
 *
 * @see TcExecutionModel
 */
public interface TcExecutionUnit {
   /** invalid */
   public static int INVALID = -1;
   /** Ausf�hrungseinheit Global */
   public static int SYSTEM = 8;
   /** Ausf�hrungseinheit Global */
   public static int GLOBAL = 0;
   /** Ausf�hrungseinheit Projekt */
   public static int PROJECT = 1;
   /** Ausf�hrungseinheit Routine */
   public static int ROUTINE = 3;

   /**
    * Liefert die �bergeordnente Ausf�hrungseinheit zur�ck. 
    *
    * @return Ausf�hrungseinheit
    */
   public TcExecutionUnit getParent ();
   
   /**
    * Liefert das Ausf�hrungshandle. 
    *
    * @return Ausf�hrungseinheit
    */
   public int getHandle ();

   /**
    * Liefert die Art der Ausf�hrungseinheit zur�ck.
    *
    * @return Art der Ausf�hrungseinheit
    */
   public int getKind ();

   /**
    * Liefert die Ausf�hrungspriorit�t zur�ck.
    *
    * @return Ausf�hrungspriorit�t
    */
   public int getPriority ();

   /**
    * Liefert den Aufrufpfad der Ausf�hrungseinheit (nur g�ltig f�r Routinen) zur�ck.
    *
    * @return Aufrufpfad
    */
   public String getCallPath ();

   /**
    * Liefert den Wert true zur�ck, wenn der Ablauf durch den Start eines Programmes
    * (TcExecutionModel.startProgram) hervorgegangen ist.
    *
    * @see TcExecutionModel
    */
   public boolean isMainFlow ();

   /**
    * Liefert den Strukturbaumknoten (TcStructuralNode) zur�ck, welches die
    * Ausf�hrungseinheit repr�sentiert.
    *
    * @see TcStructuralNode
    */
   public TcStructuralNode getTcStructuralNode ();


}
