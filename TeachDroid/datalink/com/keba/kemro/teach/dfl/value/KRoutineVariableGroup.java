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
package com.keba.kemro.teach.dfl.value;

import com.keba.kemro.teach.dfl.execution.KExecUnitRoutine;
import com.keba.kemro.teach.network.TcAccessHandle;
import com.keba.kemro.teach.network.TcExecutionUnit;


/**
 * Spezielle Variablengruppe für routinen-lokale Variablen.
 */
public class KRoutineVariableGroup extends KVariableGroup {
   /** Ausführungseinheit der Variablengruppe */
   protected KExecUnitRoutine m_execRoutine;

   /**
    * Erzeugt eine Variablengruppe für routinen-lokale Variablen.
    *
    * @param sGroupName Name der Variablengruppe
    * @param listener Klient der informiert wird, wenn sich Werte ändern.
    * @param execRoutine Ausführungseinheit der Routine.
    */
   KRoutineVariableGroup (String sGroupName,  KExecUnitRoutine execRoutine) {
      super(sGroupName);
      m_execRoutine = execRoutine;
   }

   /**
    * Erzeug eine Gruppe, ohne die Ausführungseinheit zu setzen. Diese kann später 
    * über gesetzt werden. Der Konstruktor unterstützt bessere Performance
    *
    * @param sGroupName Name der Variablengruppe
    * @param listener Klient der informiert wird, wenn sich Werte ändern.
    */
   KRoutineVariableGroup (String sGroupName) {
      this(sGroupName, null);
   }

   /**
    * Setzt die Ausführungseinheit.
    *
    * @param execRoutine Ausführungseinheit.
    */
   public void setExecRoutineNode (KExecUnitRoutine execRoutine) {
//      deactivate();
      m_execRoutine = execRoutine;
      //deactivate();
      check();
   }

   /**
    * Liefert die Ausführungseinheit
    *
    * @return KExecUnitRoutine aktueller Ausführungsknoten.
    */
   public KExecUnitRoutine getExecRoutineNode () {
      return m_execRoutine;
   }

   /**
    * @see com.keba.kemro.teach.dfl.value.KVariableGroup#add(com.keba.kemro.teach.dfl.value.KStructVarWrapper)
    */
//   public void add (KStructVarWrapper variable) {
//      synchronized (m_variables) {
//         if (variable == null) {
//         	KDflLogger.error(this, "cannot add variable to list, because wrapper for data key is null!");
//            return;
//      }
//         m_variables.addElement(variable);
//         //m_values.addElement(new TcValue());
//      }
//   }

   /**
    * @see com.keba.kemro.teach.dfl.value.KVariableGroup#check()
    */
   protected void check () {
      synchronized (m_variables) {
         synchronized (m_addedVars) {
            m_addedVars.setSize(0);
            m_execUnits.setSize(0);
            m_accessHandles.setSize(0);
            m_values.setSize(0);
            if (m_execRoutine != null) {
               TcExecutionUnit tcu = m_execRoutine.getTcExecutionUnit();
               for (int i = 0; i < m_variables.size(); i++) {
                  KStructVarWrapper var = (KStructVarWrapper) m_variables.elementAt(i);
                  TcAccessHandle ah = var.getAccessHandle();
                  if (ah != null) {
                     m_addedVars.addElement(var);
                     m_accessHandles.addElement(ah);
                     m_execUnits.addElement(tcu);
                     m_values.addElement(var.getValueObject());
                  }
               }
            }
         }
      }
   }
   protected void refresh() {
//	   check();
//	   if (m_addedVars == null || m_addedVars.size() <= 0) {
//			check();
//			return;
//		}
//		synchronized (m_variables) {
//			synchronized (m_addedVars) {
//
//				Vector varsToAdd = new Vector();
//				Vector newAH = new Vector();
//				int cnt = getVarsToAdd(newAH, varsToAdd);
//				if (varsToAdd.size() > 0) {
//					System.out.println(refreshCnt + ": " + m_sGroupName + " should ADD " + varsToAdd.size() + " new vars!");
//					for (int i = 0; i < cnt; i++) {
//						m_addedVars.addElement(varsToAdd.elementAt(i));
//					}
//				}
//
//				Vector vars2Del = getVarsToDelete();
//				if (vars2Del.size() > 0) {
//					System.out.println(refreshCnt + ": " + m_sGroupName + " should DELETE " + vars2Del.size() + " vars!");
//					for (int i = 0; i < vars2Del.size(); i++) {
//						m_addedVars.removeElement(vars2Del.elementAt(i));
//					}
//				}
//			}
//		}
//		refreshCnt++;
	   super.refresh();
   }

	// protected boolean update() {
	//
	// return super.update();
	// }
}
