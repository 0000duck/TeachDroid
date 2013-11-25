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
package com.keba.kemro.teach.network;

/**
 * TcEditorModel ermöglicht das Auslesen und Ändern von Teachtalk - Programmen/Bausteinen.
 *
 */
public interface TcEditorModel {

   /**
    * Schließt das Editormodell und gibt die Resourcen im TeachControl frei.
    */
   public void close ();

   /**
    * Fügt den Text text an der Position pos ein.
    *
    * @param pos gibt die Einfügeposition an.
    * @param text enthält den Text der eingefügt werden soll.
    *
    * @return true für das erfolgreiche Einfügen
    */
   public boolean insertText (int line, String text);
   
   /**
    * Löscht den angegeben Textbereich range.
    * @param range Textbereich
    *
    * @return true für das erfolgreiche Löschen
    */
   public boolean deleteText (int line, int count);
   
   /**
    * Speichert den Editormodellinhalt.
    *
    * @return true, wenn das Speichern erfolgreich durchgeführt wurde
    */
   public boolean save ();
   
   /**
    * Liefert den Textbereich zurück, das dem angegebenen Strukturbaumknoten entspricht.
    *
    * @return Textbereich
    * @see TcEditorTextRange
    */
   public TcEditorTextRange getTextRange (TcStructuralNode node);
   
   /**
    * Returns the number of lines.
    * @return number of lines
    */
   public int getLineCount ();
   
   /**
    * Gets up to 50 lines.
    * @param startLine start position
    * @return lines
    */
   public String[] getLines (int startLine);
}
