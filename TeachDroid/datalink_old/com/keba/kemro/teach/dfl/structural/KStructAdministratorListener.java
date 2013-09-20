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
 * Listener-Schnittstelle die vom Strukturadministrator zur Verf�gung gestellt 
 * wird. Der Administrator informiert registrierte Klienten(Listener) �ber
 * Ver�nderungen im Strukturbaum.
 */
public abstract interface KStructAdministratorListener {
   /**
    * Die Methode wird aufgerufen, wenn sich der Strukturbaum ge�ndert hat.
    * Der �bergebene Strukturknoten hat sich ver�ndert u. muss ausgetauscht werden.
    *
    * @param parent ver�nderter Strukturknoten
    */
   public void treeChanged (KStructNode parent);

   /**
    * Die Methode wird aufgerufen, wenn ein neuer Strukturknoten dem Strukturbaum
    * hinzugef�gt wird.
    *
    * @param parent �bergeordneter Strukturknoten
    * @param node neuer Strukturknoten
    */
   public void nodeInserted (KStructNode parent, KStructNode node);

   /**
    * Wird aufgerufen, wenn ein Strukturknoten aus dem Strukturbaum entfernt
    * wird. 
    *
    * @param parent �bergeordneter Strukturknoten
    * @param node entfernter Strukturknoten
    */
   public void nodeRemoved (KStructNode parent, KStructNode node);
}
