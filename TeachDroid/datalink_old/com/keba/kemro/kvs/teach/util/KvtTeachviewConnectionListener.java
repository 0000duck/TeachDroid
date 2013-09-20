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
package com.keba.kemro.kvs.teach.util;

/**
 * Interface für die Benachrichtigung der Verbindungszustandsänderungen.
 */
public abstract interface KvtTeachviewConnectionListener {
   /**
    * Wird Aufgerufen wenn die Verbindung (wieder) hergestellt wurde
    */
   public void teachviewConnected ();

   /**
    * Wird bei einem Verbindungsabbruch aufgerufen.
    */
   public void teachviewDisconnected ();
}
