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
package com.keba.kemro.teach.dfl.value;

/**
 * Schnittstelle f�r Variablengruppen-Klienten (Listener)
 */
public interface KVariableGroupListener {
   /**
    * Der Wert einer Variable hat sich ge�ndert
    *
    * @param variable Variablen-Wrapper-Objekt, dessen Variable sich ge�ndert hat.
    */
   public void changed (KStructVarWrapper variable);

   /**
    * Die Werte aller Variablen einer Variablengruppe sind aktualisiert worden.
    */
   public void allActualValuesUpdated ();
}
