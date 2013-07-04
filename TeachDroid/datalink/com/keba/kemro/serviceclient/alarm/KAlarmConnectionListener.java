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
package com.keba.kemro.serviceclient.alarm;

/**
 * Interface zur Benachrichtigung von Verbindungereignissen
 *
 */
public interface KAlarmConnectionListener {
   /**
    * Verbindung wurde aufgebaut.
    */
   public void connected ();

   /**
    * Verbindung wurde abgebaut.
    */
   public void disconnected ();
}
