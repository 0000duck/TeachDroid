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
 * Listener um bei �nderungen der Projektstruktur benachrichtigt zu werden
 */
public abstract interface KvtProjectAdministratorListener {
   /**
    * Projektzustand hat sich ge�ndert
    *
    * @param prj Projekt dass sich ge�ndert hat
    */
   public void projectStateChanged (KvtProject prj);
   
   /**
    * Programmstatus hat sich ge�ndert
    *
    * @param prg Das Program das sich ge�ndert hat.
    */
   public void programStateChanged (KvtProgram prg);

   /**
    * Projektliste hat sich ge�ndert.
    */
   public void projectListChanged ();
}
