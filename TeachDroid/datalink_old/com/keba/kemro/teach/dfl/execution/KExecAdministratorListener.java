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
    * Gibt das Entfernen und Hinzufügen von Ausführungseinheiten bekannt. In den Vektoren
    * sind die Elternknoten immer vor den Kinderknoten angeordnent.
    * 
    *
    * @param toRemove entfernte Ausführungseinheiten
    * @param toInsert hinzugefügte Ausführungseinheiten
    */
   public void execUnitsRemovedAdded (Vector toRemove, Vector toInsert);

   /**
    * Gibt die Gültigkeit der Zustände der Ausführungseinheiten bekannt. Diese Methode wird
    * zyklisch aufgerufen.
    */
   public void updateState ();
}
