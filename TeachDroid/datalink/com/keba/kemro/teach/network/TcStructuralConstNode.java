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
 * TcStructuralConstNode repräsentiert eine Konstante im TeachControl.
 *
 */
public interface TcStructuralConstNode extends TcStructuralNode {

   /**
    * Liefert den Wert der Konstanten zurück.
    *
    * @return Wert
    */
   public TcValue getValue ();

   /**
    * Liefert den Typ der Konstanten zurück.
    *
    * @return Typ
    */
   public TcStructuralTypeNode getType ();

}
