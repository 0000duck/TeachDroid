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
 * Schnittstelle f�r die Mitteilung der Zustands�nderung des TcConnectionManager.
 *
 * @see TcConnectionManager
 */
public interface TcConnectionListener {
   /**
    * Gibt die Status�nderung der Verbindung zum TeachControl an.
    *
    * @param isConnected true wenn eine Verbindung zum Server besteht, sonst false.
    */
   public void connectionStateChanged (boolean isConnected);
}
