/* -------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Projekt   : Kemro.teachview.r2
 *    Auftragsnr: 5500821
 *    Erstautor : sinn
 *    Datum     : 13.10.2004
 *------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl.execution;

import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.network.*;

import java.util.*;


/**
 * Repr�sentiert das globale Projekt und die Wurzel aller Ausf�hrungseinheiten.
 */
public abstract class KExecUnitScope extends KExecUnitProject {
   /** In Ausf�hrung befindliche Projekte */
   Vector m_projects;

   /**
    * Konstruktor
    *
    * @param name Name des Projektes
    * @param ortsExecutionUnit TeachControll - Repr�sentation
    * @param structNode zugeh�riger Strukturbaumknoten
    * @param state Zustandsobjekt
    */
   protected KExecUnitScope (String name, TcExecutionUnit ortsExecutionUnit, KStructNode structNode,
                    TcExecutionState state, KExecUnitNode parent) {
      super(name, ortsExecutionUnit, structNode, state, parent);
   }

   /**
    * Liefert die Projektanzahl zur�ck.
    *
    * @return Projektanzahl
    */
   public int getExecUnitProjectCount () {
      if (m_projects == null) {
         return 0;
      }
      return m_projects.size();
   }

   /**
    * Liefert das Projekt mit dem angegebenen Index zur�ck.
    *
    * @param index Index
    *
    * @return Projekt
    */
   public KExecUnitProject getExecUnitProject (int index) {
      if ((m_projects == null) || (index < 0) || (m_projects.size() <= index)) {
         return null;
      }
      return (KExecUnitProject) m_projects.elementAt(index);
   }
}
