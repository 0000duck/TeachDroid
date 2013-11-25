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
package com.keba.kemro.teach.dfl.compilerError;

import java.util.*;


/**
 * Liste mit den Kompilerfehlermeldungen
 *
 */
public class KCompilerErrorList {
   private final Vector m_errorList = new Vector(3);

   /**
    * Legt eine neue FehlerListe an
    */
   public KCompilerErrorList () {
   	// do nothing
   }

   /**
    * Fügt einen KCompilerError hinzu
    *
    * @param error Neue Fehlermeldung
    */
   public void addCompilerError (KCompilerError error) {
      m_errorList.addElement(error);
   }

   /**
    * Entfernt einen KCompilerError aus der Liste
    *
    * @param error Fehler der Entfernt werden soll
    */
   public void removeCompilerError (KCompilerError error) {
      m_errorList.removeElement(error);
   }

   /**
    * Entfernt alle KCompilerErrors aus der Liste
    */
   public void removeAllCompilerErrors () {
      m_errorList.setSize(0);
   }

   /**
    * Liefert die Anzahl der Fehlermeldungen
    *
    * @return Anzahl der Fehlermeldungen
    */
   public int getCompilerErrorCount () {
      return m_errorList.size();
   }
   
   /**
    * Liefert den KCompilerError mit dem angegebenen Index
    * @param i Index des gewünschten Fehlers
    * @return Den Fehler mit dem angegebenen Index.
    */
   public KCompilerError getCompilerError (int i) {
      return (KCompilerError) m_errorList.elementAt(i);
   }
}
