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
package com.keba.kemro.teach.dfl.dir;

/**
 * Dieses Interface dient dazu um von Projektstrukturänderungen benachrichtigt
 * zu werden.
 */
public interface KDirectoryAdministratorListener {
   /**
    * Wird aufgerufen wenn sich die Struktur der Dateien geändert hat.
    */
   public void directoryProjectsChanged ();
}
