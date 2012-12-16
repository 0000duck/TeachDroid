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

import com.keba.kemro.teach.dfl.*;



/**
 * Die Klasse beschreibt einen speziellen Strukturknoten, der für das globale 
 * Projekt steht und wird deswegen vom Projekt-Strukturknoten abgeleitet.
 */
public class KStructGlobal extends KStructScope {

   /**
    * Erzeugt einen globalen Strukturknoten
    *
    * @param key Name des Knoten
    */
   protected KStructGlobal (String key, KTcDfl dfl) {
      super(key, dfl);
   }

}
