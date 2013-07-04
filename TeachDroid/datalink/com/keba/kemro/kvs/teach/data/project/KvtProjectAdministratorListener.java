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
package com.keba.kemro.kvs.teach.data.project;

/**
 * Listener um bei Änderungen der Projektstruktur benachrichtigt zu werden
 */
public abstract interface KvtProjectAdministratorListener {
   /**
    * Projektzustand hat sich geändert
    *
    * @param prj Projekt dass sich geändert hat
    */
   public void projectStateChanged (KvtProject prj);
   
   /**
    * Programmstatus hat sich geändert
    *
    * @param prg Das Program das sich geändert hat.
    */
   public void programStateChanged (KvtProgram prg);

   /**
    * Projektliste hat sich geändert.
    */
   public void projectListChanged ();
}
