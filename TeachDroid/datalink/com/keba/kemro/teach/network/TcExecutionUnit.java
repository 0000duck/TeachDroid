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
 * TcExecutionUnit repräsentiert ein Ausführungseinheit im TeachControl
 *
 * @see TcExecutionModel
 */
public interface TcExecutionUnit {
   /** invalid */
   public static int INVALID = -1;
   /** Ausführungseinheit Global */
   public static int SYSTEM = 8;
   /** Ausführungseinheit Global */
   public static int GLOBAL = 0;
   /** Ausführungseinheit Projekt */
   public static int PROJECT = 1;
   /** Ausführungseinheit Routine */
   public static int ROUTINE = 3;

   /**
    * Liefert die übergeordnente Ausführungseinheit zurück. 
    *
    * @return Ausführungseinheit
    */
   public TcExecutionUnit getParent ();
   
   /**
    * Liefert das Ausführungshandle. 
    *
    * @return Ausführungseinheit
    */
   public int getHandle ();

   /**
    * Liefert die Art der Ausführungseinheit zurück.
    *
    * @return Art der Ausführungseinheit
    */
   public int getKind ();

   /**
    * Liefert die Ausführungspriorität zurück.
    *
    * @return Ausführungspriorität
    */
   public int getPriority ();

   /**
    * Liefert den Aufrufpfad der Ausführungseinheit (nur gültig für Routinen) zurück.
    *
    * @return Aufrufpfad
    */
   public String getCallPath ();

   /**
    * Liefert den Wert true zurück, wenn der Ablauf durch den Start eines Programmes
    * (TcExecutionModel.startProgram) hervorgegangen ist.
    *
    * @see TcExecutionModel
    */
   public boolean isMainFlow ();

   /**
    * Liefert den Strukturbaumknoten (TcStructuralNode) zurück, welches die
    * Ausführungseinheit repräsentiert.
    *
    * @see TcStructuralNode
    */
   public TcStructuralNode getTcStructuralNode ();


}
