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
package com.keba.kemro.kvs.teach.model;

/**
 * KRoutineModelListener beschreibt die Schnittstelle zum Routinenmodell.
 *
 */
public interface KvtRoutineModelListener {
   /**
    * Gibt die �nderung des Routineinhaltes bekannt.
    */
   public void modelChanged ();

   /**
    * Gibt die �nderung der Routine bekannt.
    */
   public void routineChanged ();

   /**
    * Gibt das Hinzuf�gen eines Breakpoints bekannt.
    *
    * @param lineOfView Anzeigenzeilennummer
    */
   public void breakpointAdded (int lineOfView);

   /**
    * Gibt das Entfernen eines Breakpoints bekannt.
    *
    * @param lineOfView Anzeigenzeilennummer
    */
   public void breakpointRemoved (int lineOfView);

   /**
    * Gibt die Zustands�nderung des Breakpoints bekannt.
    *
    * @param lineOfView Anzeigenzeilennummer
    */
   public void breakpointChanged (int lineOfView);

   /**
    * Gibt die �nderung des Hauptlaufzeigers bekannt.
    */
   public void mainFlowPointerChanged ();
}
