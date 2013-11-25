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
 * TcStructuralTypeNode repr�sentiert einen Typ im TeachControl.
 *
 * @see TcStructuralModel
 * @see TcStructuralNode
 * @see TcStructuralRoutineNode
 * @see TcStructuralVarNode
 */
public interface TcStructuralTypeNode extends TcStructuralNode {
	/** ung�ltiger Typ */
	public static final byte UNKOWN_TYPE = 0;
	/** BOOL Typ */
	public static final byte BOOL_TYPE = 1;
	/** 8-Bit Integer Typ */
	public static final byte SINT_TYPE = 2;
	/** 16-Bit Integer Typ */
	public static final byte INT_TYPE = 3;
	/** 32-Bit Integer Typ */
	public static final byte DINT_TYPE = 4;
	/** 64-Bit Integer Typ */
	public static final byte LINT_TYPE = 5;
	/** REAL Typ */
	public static final byte REAL_TYPE = 6;
	/** STRING - Typ */
	public static final byte STRING_TYPE = 7;
	/** SUBRANGE - Typ */
	public static final byte SUBRANGE_TYPE = 8;
	/** ENUM - Typ */
	public static final byte ENUM_TYPE = 9;
	/** ARRAY - Typ */
	public static final byte ARRAY_TYPE = 10;
	/** STRUCT - Typ */
	public static final byte STRUCT_TYPE = 11;
	/** UNIT - Typ */
	public static final byte UNIT_TYPE = 12;
	/** MAPTO - Typ */
	public static final byte MAPTO_TYPE = 13;
	/** ROUTINE - Typ */
	public static final byte ROUTINE_TYPE = 14;
	/** beliebiger Typ */
	public static final byte ANY_TYPE = 15;

	public static final byte BYTE_TYPE = 20;
	public static final byte WORD_TYPE = 21;
	public static final byte DWORD_TYPE = 22;
	public static final byte LWORD_TYPE = 23;
	public static final byte LREAL_TYPE=24;

   /**
    * Liefert die Typart zur�ck.
    *
    * @return Typart
    */
   public byte getTypeKind ();

   /**
    * Liefert die Unteregrenze eines Array oder Subrange - Typs zur�ck
    *
    * @return Untergrenze
    */
   public int getLowerBound ();

   /**
    * Liefert die Oberegrenze eines Array oder Subrange - Typs zur�ck.
    *
    * @return Oberegrenze
    */
   public int getUpperBound ();

   /**
    * Liefert die untere Grenzkonstante eines Array oder Subrange - Typs zur�ck.
    *
    * @return untere Grenzkonstante
    */
   public TcStructuralConstNode getLowerBoundConst ();

   /**
    * Liefert die obere Grenzkonstante eines Array oder Subrange - Typs zur�ck.
    *
    * @return obere Grenzkonstante
    */
   public TcStructuralConstNode getUpperBoundConst ();

   /**
    * Liefert die Gr��enkonstante eines Array - Typs zur�ck.
    *
    * @return Gr��enkonstante
    */
   public TcStructuralConstNode getArraySizeConst ();

   /**
    * Liefert den Bausteintyp, von dem dieser Bausteintyp abgeleitet wurde, zur�ck.
    *
    * @return Basisbaustein
    */
   public TcStructuralTypeNode getBaseUnit ();

   /**
    * Liefert den Basistyp (z.B. Array) zur�ck.
    *
    * @return Basistyp
    */
   public TcStructuralTypeNode getBaseType ();
   
   /**
    * Liefert den Bausteintyp, von dem dieser Bausteintyp abgeleitet wurde, zur�ck.
    *
    * @return Basisbaustein
    */
   public TcStructuralNode getVariantNode();

   /**
    * Liefert den R�ckgabetyp des Routinen - Types zur�ck.
    *
    * @return R�ckgabetyp
    */
   public TcStructuralTypeNode getReturnType ();

   /**
    * Liefert den Typ des Array-Elementes zur�ck.
    *
    * @return Array-Elementtyp
    */
   public TcStructuralTypeNode getArrayElementType ();

}
