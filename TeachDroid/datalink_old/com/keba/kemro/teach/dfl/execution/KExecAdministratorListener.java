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
package com.keba.kemro.teach.dfl.execution;

import java.util.*;


/**
 * KExecAdministrator - Listener - Schnittstelle
 *
 */
public abstract interface KExecAdministratorListener {

   /**
    * Gibt das Entfernen und Hinzuf�gen von Ausf�hrungseinheiten bekannt. In den Vektoren
    * sind die Elternknoten immer vor den Kinderknoten angeordnent.
    * 
    *
    * @param toRemove entfernte Ausf�hrungseinheiten
    * @param toInsert hinzugef�gte Ausf�hrungseinheiten
    */
   public void execUnitsRemovedAdded (Vector toRemove, Vector toInsert);

   /**
    * Gibt die G�ltigkeit der Zust�nde der Ausf�hrungseinheiten bekannt. Diese Methode wird
    * zyklisch aufgerufen.
    */
   public void updateState ();
}
