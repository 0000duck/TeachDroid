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
 * Repräsentiert eine in Ausführung befindliche Routine.
 */
public class KExecUnitRoutine extends KExecUnitNode {
   public static final int UNNAMED_ROUTINE = 0;
   public static final int NAMED_ROUTINE = 1;
   public static final int NEW_ROUTINE = 2;
   public static final int DELETE_ROUTINE = 3;
   /** alle Routine, die aus dieser Routine gestartet wurden */
   protected Vector m_routines;
   
   protected int kind;

   /**
    * Konstruktor
    *
    * @param name Routinenname
    * @param ortsExecutionUnit TeachControl - Repräsentation
    * @param structNode zugehöriger Strukturbaumknoten
    * @param state Zustandsobjekt
    * @param parent Routine oder Projekt
    */
   KExecUnitRoutine (String name, TcExecutionUnit ortsExecutionUnit, KStructNode structNode,
                     TcExecutionState state, KExecUnitNode parent) {
      super(name, ortsExecutionUnit, structNode, state, parent);
      this.kind = -1;
   }

   public int getKind () {
      if (kind == -1) {
         if (m_name.equals("NEW")) {
            kind = NEW_ROUTINE;
         } else if (m_name.equals("DELETE")) {
            kind = DELETE_ROUTINE;
         } else if (m_name.equals("*")) {
            kind = UNNAMED_ROUTINE;
         } else {
            kind = NAMED_ROUTINE;
         }
      }
      return kind;
   }
   /**
    * Liefert die Zeilennummer des Hauptablaufzeigers.
    *
    * @return Zeilennummer
    */
   public int getMainFlowLine () {
      return m_state.mainFlowLine;
   }

   /**
    * Liefert die Zeilennummer des Ausführungszeigers.
    *
    * @return Zeilennummer
    */
   public int getLine () {
      return m_state.line;
   }

   /**
    * Liefert die Anzahl der Unterroutinen bzw. CO-Routinen.
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
    * Liefert die Routine für den angegebenen Index.
    *
    * @param index Intex
    *
    * @return Routine
    */
   public KExecUnitRoutine getExecUnitRoutine (int index) {
      if ((m_routines == null) || (index < 0) || (m_routines.size() <= index)) {
         return null;
      }
      return (KExecUnitRoutine) m_routines.elementAt(index);
   }
   
   /**
    * Liefert den Instanzpfad der Routine zurück.
    * @return Instanzpfad
    */
   public String getInstancePath () {
      return m_ortsExecutionUnit.getCallPath();
   }
}
