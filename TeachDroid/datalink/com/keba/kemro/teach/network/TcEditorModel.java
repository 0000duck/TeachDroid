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
 * TcEditorModel erm�glicht das Auslesen und �ndern von Teachtalk - Programmen/Bausteinen.
 *
 */
public interface TcEditorModel {

   /**
    * Schlie�t das Editormodell und gibt die Resourcen im TeachControl frei.
    */
   public void close ();

   /**
    * F�gt den Text text an der Position pos ein.
    *
    * @param pos gibt die Einf�geposition an.
    * @param text enth�lt den Text der eingef�gt werden soll.
    *
    * @return true f�r das erfolgreiche Einf�gen
    */
   public boolean insertText (int line, String text);
   
   /**
    * L�scht den angegeben Textbereich range.
    * @param range Textbereich
    *
    * @return true f�r das erfolgreiche L�schen
    */
   public boolean deleteText (int line, int count);
   
   /**
    * Speichert den Editormodellinhalt.
    *
    * @return true, wenn das Speichern erfolgreich durchgef�hrt wurde
    */
   public boolean save ();
   
   /**
    * Liefert den Textbereich zur�ck, das dem angegebenen Strukturbaumknoten entspricht.
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
