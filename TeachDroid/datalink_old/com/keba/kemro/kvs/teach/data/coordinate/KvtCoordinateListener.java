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
package com.keba.kemro.kvs.teach.data.coordinate;

/**
 * Listener für die Koordinatensysteme
 *
 * @author ede
 */
public interface KvtCoordinateListener {
   /**
    * Diese Methode wird aufgerufen wenn sich die Koordinatensysteme geändert haben.
    */
   public void coordinateSystemsChanged ();
}
