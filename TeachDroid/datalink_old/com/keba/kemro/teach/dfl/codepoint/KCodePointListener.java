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
package com.keba.kemro.teach.dfl.codepoint;

/**
 * Beschreibt die Schnittstelle eines CodePoint - Listeners, er gibt die 
 * �nderungen des KCodePointAdministrators bekannt.
 *
 */
public interface KCodePointListener {
   /**
    * Gibt bekannt, dass ein CodePoint hinzugef�gt wurde.
    *
    * @param codePoint CodePoint
    */
   public void added (KCodePointAdministrator.CodePoint codePoint);

   /**
    * Gibt bekannt, dass ein CodePoint entfernt wurde.
    *
    * @param codePoint CodePoint
    */
   public void removed (KCodePointAdministrator.CodePoint codePoint);

   /**
    * Gibt die �nderung eines CodePoints bekannt.
    *
    * @param codePoint CodePoint
    */
   public void changed (KCodePointAdministrator.CodePoint codePoint);

   /**
    * Alle Codepoints haben sich ge�ndert.
    */
   public void allChanged ();

   /**
    * Gibt bekannt, dass eine Watchpoint - Variable hinzugef�gt wurde.
    *
    * @param codePoint Watchpoint
    * @param variable Watchpoint - Variable
    */
   public void watchpointVariableAdded (
                                        KCodePointAdministrator.CodePoint codePoint,
                                        KCodePointAdministrator.WatchpointVariableNode variable);

   /**
    * Gibt bekannt, dass eine Watchpoint - Variable entfernt wurde.
    *
    * @param codePoint Watchpoint
    * @param variable Watchpoint - Variable
    */
   public void watchpointVariableRemoved (
                                          KCodePointAdministrator.CodePoint codePoint,
                                          KCodePointAdministrator.WatchpointVariableNode variable);

   /**
    * Gibt bekannt, dass sich der Wert der Watchpoint - Variable ge�ndert hat.
    *
    * @param codePoint Watchpoint
    * @param variable Watchpoint - Variable
    */
   public void watchpointVariableChanged (
                                          KCodePointAdministrator.CodePoint codePoint,
                                          KCodePointAdministrator.WatchpointVariableNode variable);
}
