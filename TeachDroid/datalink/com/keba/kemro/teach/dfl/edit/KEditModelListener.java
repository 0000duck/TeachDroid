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
package com.keba.kemro.teach.dfl.edit;

/**
 * Schnittstelle zur Benachrichtigen bei Editoränderungen
 */
public interface KEditModelListener {
   /**
    * Wir aufgerufen wenn sich der Editor geändert hat.
    */
   public void changed ();
}
