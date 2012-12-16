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


/**
 * Ist die Basisklasse alle Ausführungseinheiten.
 */
public class KExecUnitNode {
   /** Ausführungszustand - Waiting, d.h. die Routine steht auf einer WAIT - Anweisung */
   public static final int STATE_WAITING = TcExecutionState.STATE_WAITING;
   /** Ausführungszustand - Running, d.h. die Routine wird ausgeführt */
   public static final int STATE_RUNNING = TcExecutionState.STATE_RUNNING;
   /** Ausführungszustand - Stepping, d.h. die Routine ist im Debug - Modus und angehalten */
   public static final int STATE_STEPPING = TcExecutionState.STATE_STEPPING;
   /** 
    * Ausführungszustand - Finished, d.h. die Routine ist beendet, aber eine oder mehrere
    * CO-Routinen sind noch in Ausführung.
    */
   public static final int STATE_FINISHED = TcExecutionState.STATE_FINISHED;
   /** Ausführungszustand - Interrupted, d.h. der Ablauf wurde unterbrochen */
   public static final int STATE_INTERRUPTED = TcExecutionState.STATE_INTERRUPTED;
   /** Ausführungszustand - Invalid, d.h. die Routine ist nicht mehr in Ausführung */
   public static final int STATE_INVALID = TcExecutionState.STATE_INVALID;
   /** execution state - project loading, means that at least one new routine isn't finished */
   public static final int STATE_PROJECT_LOADING = TcExecutionState.STATE_PROJECT_LOADING;
   /** execution state - project loading, means that at least one delete routine isn't finished */
   public static final int STATE_PROJECT_UNLOADING = TcExecutionState.STATE_PROJECT_UNLOADING;
   /** Start flag isn't supported */
   public static final int START_FLAG_NOT_SUPPORTED = TcExecutionState.START_FLAG_NOT_SUPPORTED;
   /** Start flag is set to disabled which means that the flow won't be started with the START hard key */
   public static final int START_FLAG_DISABLED = TcExecutionState.START_FLAG_DISABLED;
   /** Start flag is set to enabled which means that the flow will be started with the START hard key */
   public static final int START_FLAG_ENABLED = TcExecutionState.START_FLAG_ENABLED;
   
   /** Name der Ausführungseinheit (Projektname, Programmname bzw. Routinenname) */
   protected String m_name;
   /** Elternknoten */
   protected KExecUnitNode m_parent;
   /** TeachControl - Repräsentation */
   protected TcExecutionUnit m_ortsExecutionUnit;
   /** Zugehöriger Strukturbaumknoten */
   protected KStructNode m_structNode;
   /** Enthält die Zustände */
   protected TcExecutionState m_state;
   
   /**
    * Konstruktor
    *
    * @param name Name der Ausführungseinheit
    * @param ortsExecutionUnit TeachControl - Repräsentation
    * @param structNode zugehöriger Strukturbaumknoten
    * @param state Zustände
    * @param parent Elternknoten
    */
   KExecUnitNode (
                         String name, TcExecutionUnit ortsExecutionUnit, KStructNode structNode,
                         TcExecutionState state, KExecUnitNode parent) {
      m_name = name;
      m_ortsExecutionUnit = ortsExecutionUnit;
      m_structNode = structNode;
      m_state = state;
      m_parent = parent;
   }

   /**
    * Liefert den Name der Ausführungseinheit.
    *
    * @return Name
    */
   public String getName () {
      return m_name;
   }

   /**
    * Liefert den Elternknoten.
    *
    * @return Elternknoten
    */
   public KExecUnitNode getParent () {
      return m_parent;
   }

   /**
    * Liefert den Ausführungszustand (STATE_WAITING, STATE_RUNNING, STATE_STEPPING, 
    * STATE_FINISHED, STATE_INTERRUPTED, STATE_INVALID, STATE_PROJECT_LOADING, 
    * STATE_PROJECT_UNLOADING) zurück.
    *
    * @return Ausführungszustand
    */
   public int getExecutionState () {
      return m_state.executionState;
   }

   /**
    * Returns the stepping state.
    *
    * @return (STEP_OFF, STEP_BREAK, STEP_INTO, STEP_OVER, STEP_OUT, STEP_GOTO_WAIT, STEP_GOTO_BREAKPOINT)
    */
   public int getSteppingState () {
      return m_state.steppingState;
   }

   /**
    * Returns the execution mode (EXECUTION_MODE_FLOW, EXECUTION_MODE_ROUTINE, EXECUTION_MODE_OFF) zurück.
    *
    * @return execution mode
    */
   public int getExecutionMode () {
      return m_state.executionMode;
   }

   /**
    * Returns the main flow stepping flag.  
    * @return true if the main flow stepping is switched on ohterwise false
    */
   public boolean isMainFlowStepping () {
   	return m_state.isMainFlowStepping;
   }
   /**
    * Liefert die TeachControl - Repräsentation zurück.
    *
    * @return TeachControl - Repräsentation
    */
   public TcExecutionUnit getTcExecutionUnit () {
      return m_ortsExecutionUnit;
   }

   /**
    * Liefert den zugehörigen Strukturbaumknoten zurück.
    *
    * @return Strukturbaumknoten
    */
   public KStructNode getKStructNode () {
      return m_structNode;
   }

   /**
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals (Object o) {
     if (this == o) {
    	 return true;
     }
     if (!(o instanceof KExecUnitNode)) {
    	 return false;
     }
  	 return m_ortsExecutionUnit.equals(((KExecUnitNode) o).m_ortsExecutionUnit);
   }
   
   /**
    * @see java.lang.Object#hashCode()
    */
   public int hashCode () {
    	return m_ortsExecutionUnit.hashCode();
    }
   
   /**
    * @return START_FLAG_NOT_SUPPORTED, START_FLAG_DISABLED or START_FLAG_ENABLED
    */
   public int getStartFlag () {
   	return m_state.getStartFlag();
   }

}
