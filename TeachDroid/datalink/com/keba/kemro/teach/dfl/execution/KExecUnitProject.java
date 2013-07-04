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

import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.network.*;

import java.util.*;


/**
 * Repräsentation eines in Ausführung befindliche Projekt.
 */
public class KExecUnitProject extends KExecUnitNode {
   /** alle Ablaufwurzeln */
   protected Vector m_routines;

   /**
    * Konstruktor
    *
    * @param name Projektname
    * @param ortsExecutionUnit TeachControl - Repräsentation
    * @param structNode zugehöriger Strukturbaumknoten
    * @param state Zustandsobjekt
    * @param parent globales Projekt
    */
   KExecUnitProject (
                            String name, TcExecutionUnit ortsExecutionUnit, KStructNode structNode,
                            TcExecutionState state, KExecUnitNode parent) {
      super(name, ortsExecutionUnit, structNode, state, parent);
   }

   /**
    * Liefert die Anzahl der Ablaufwurzel (Routinen).
    *
    * @return Routinenanzahl
    */
   public int getExecUnitRoutineCount () {
      if (m_routines == null) {
         return 0;
      }
      return m_routines.size();
   }

   /**
    * Liefert die Ablaufwurzel (Routine) für den angegebenen Index.
    *
    * @param index Index
    *
    * @return Routine
    */
   public KExecUnitRoutine getExecUnitRoutine (int index) {
      if ((m_routines == null) || (index < 0) || (m_routines.size() <= index)) {
         return null;
      }
      return (KExecUnitRoutine) m_routines.elementAt(index);
   }
}
