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
 * TcStructuralNode repräsentiert einen Strukturbaumknoten des TeachControls.
 *
 * @see TcStructuralModel
 * @see TcStructuralRoutineNode
 * @see TcStructuralTypeNode
 * @see TcStructuralVarNode
 */
public interface TcStructuralNode {
   /** DOCUMENT ME! */
   public static final byte ROOT = 0;
   /** system Sichtbarkeitsbereich */
   public static final byte SYSTEM = 15;
   /** globaler Sichtbarkeitsbereich */
   public static final byte GLOBAL = 1;
   /** Sichtbarkeitsbereich Projekt */
   public static final byte PROJECT = 2;
   /** Sichtbarkeitsbereich Programm */
   public static final byte PROGRAM = 3;
   /** Typ */
   public static final byte TYPE = 4;
   /** Sichtbarkeitsbereich Routine */
   public static final byte ROUTINE = 5;
   /** Variable */
   public static final byte VAR = 6;
   /** Konstante */
   public static final byte CONST = 7;
   /** unbekannt */
   public static int UNKOWN = -1;


	public boolean isAbstract();
   public boolean isUserNode ();
   public boolean hasAttributes ();

   /**
    * Liefert den Name, z.B. Typname, Variablenname zurück.
    *
    * @return Name
    */
   public String getName ();

   /**
    * Liefert die Art (PROJECT, PROGRAM, ...) des Strukturbaumknotens zurück.
    *
    * @return Art
    */
   public byte getKind ();

   /**
    * Liefert den Strukturbaumknoten der Deklarationsdatei zurück.
    *
    * @return Strukturbaumknoten
    */
   public TcStructuralNode getDeclarationNode ();

   /**
    * Liefert den übergeordneten Knoten im Strukturbaum zurück.
    *
    * @return DOCUMENT ME!
    */
   public TcStructuralNode getParent () ;

}
