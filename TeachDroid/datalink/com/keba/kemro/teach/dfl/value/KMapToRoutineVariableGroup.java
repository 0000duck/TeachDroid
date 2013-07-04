/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Project : Kemro.teachview.r2
 *------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl.value;

import com.keba.kemro.teach.dfl.execution.KExecUnitRoutine;
import com.keba.kemro.teach.network.TcAccessHandle;


/**
 * This variable group administrates routine local map variables. The map target is periodically polled and checked
 * when the target has changed then variable group listener will be called. The default
 * poll time is 2 seconds.
 */
public class KMapToRoutineVariableGroup extends KMapToVariableGroup {
   /** Execution unit for the routine local map variables */
   protected KExecUnitRoutine m_execRoutine;

   /**
    * Creates a new map variable group.
    *
    * @param name name of the variable group
    * @param listener variable group listener
    */
   KMapToRoutineVariableGroup (String name) {
   	super(name);
   }
   
   /**
    * Sets the routine execution unit.
    *
    * @param execRoutine routine execution unit
    */
   public void setExecRoutineNode (KExecUnitRoutine execRoutine) {
      deactivate();
      m_execRoutine = execRoutine;
      activate();
   }

   /**
    * Gets the routine execution unit
    *
    * @return KExecUnitRoutine routine execution unit
    */
   public KExecUnitRoutine getExecRoutineNode () {
      return m_execRoutine;
   }

   /* (non-Javadoc)
    * @see com.keba.kemro.teach.dfl.value.KVariableGroup#updateActualValues()
    */
	public boolean update() {
      m_pollCounter = 0;
      synchronized (m_addedVars) {
      	// check all map targets
         m_values.setSize(0);
      	for (int i = 0; i < m_addedVars.size(); i++) {
      		KStructVarWrapper var = (KStructVarWrapper) m_addedVars.elementAt(i);
      	   TcAccessHandle ah = var.getAccessHandle();
      	   if ((ah != null)) {
      	   	dfl.client.execution.getMapTarget(ah, m_execRoutine.getTcExecutionUnit(), var.getMapTargetObject());
      	   }
      	}
      }
      return true;
   }
}
