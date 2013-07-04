/*-------------------------------------------------------------------------
*                   (c) 1999 by KEBA Ges.m.b.H & Co
*                            Linz/AUSTRIA
*                         All rights reserved
*--------------------------------------------------------------------------
*    Projekt   : KEMRO.teachview.4
*    Auftragsnr: 5500395
*    Erstautor : tur
*    Datum     : 01.04.2003
*--------------------------------------------------------------------------
*      Revision:
*        Author:
*          Date:
*------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl.structural;

/**
 * Listener-Schnittstelle die vom Strukturadministrator zur Verfügung gestellt 
 * wird. Der Administrator informiert registrierte Klienten(Listener) über
 * Veränderungen im Strukturbaum.
 */
public abstract interface KStructAdministratorListener {
   /**
    * Die Methode wird aufgerufen, wenn sich der Strukturbaum geändert hat.
    * Der übergebene Strukturknoten hat sich verändert u. muss ausgetauscht werden.
    *
    * @param parent veränderter Strukturknoten
    */
   public void treeChanged (KStructNode parent);

   /**
    * Die Methode wird aufgerufen, wenn ein neuer Strukturknoten dem Strukturbaum
    * hinzugefügt wird.
    *
    * @param parent übergeordneter Strukturknoten
    * @param node neuer Strukturknoten
    */
   public void nodeInserted (KStructNode parent, KStructNode node);

   /**
    * Wird aufgerufen, wenn ein Strukturknoten aus dem Strukturbaum entfernt
    * wird. 
    *
    * @param parent übergeordneter Strukturknoten
    * @param node entfernter Strukturknoten
    */
   public void nodeRemoved (KStructNode parent, KStructNode node);
}
