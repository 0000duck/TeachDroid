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

import java.util.Vector;

import com.keba.kemro.teach.network.TcAccessHandle;

/*
 * en the target has changed then variable
 * group listener will be called. The default poll time is 2 seconds.
 */
public class KMapToVariableGroup extends KVariableGroup {

	/**
	 * Creates a new map variable group.
	 * 
	 * @param name
	 *            name of the variable group
	 * @param listener
	 *            variable group listener
	 */
	public KMapToVariableGroup(String name) {
		super(name);
		// setPollInterval(2000);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.teach.dfl.value.KVariableGroup#updateActualValues()
	 */
	public boolean update() {
		m_pollCounter = 0;
//		System.out.println("Performing MapTo Update on "+m_addedVars.size()+" vars");
		synchronized (m_addedVars) {
			// check all map targets
			m_values.setSize(0);
			for (int i = 0; i < m_addedVars.size(); i++) {
				KStructVarWrapper var = (KStructVarWrapper) m_addedVars.elementAt(i);
				TcAccessHandle ah = var.getAccessHandle();
				if ((ah != null)) {
					dfl.client.execution.getMapTarget(ah, null, var.getMapTargetObject());
					
				}
			}
			if (m_addedVars.size() > 0)
				m_shouldNotify = true;
			else
				m_shouldNotify = false;
		}

		m_lastPollTime = System.currentTimeMillis();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.teach.dfl.value.KVariableGroup#notifyChanges()
	 */
	protected void notifyChanges() {
		synchronized (m_addedVars) {
			if (0 < m_addedVars.size()) {
				for (int i = 0; i < m_addedVars.size(); i++) {
					KStructVarWrapper var = (KStructVarWrapper) m_addedVars.elementAt(i);
					if (var.mapTargetChanged() && m_listener != null) {
						m_listener.changed(var);
					}
				}
			}
			if (0 < m_addedVars.size()) {
				m_listener.allActualValuesUpdated();
			}
		}
		m_shouldNotify = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.keba.kemro.teach.dfl.value.KVariableGroup#check()
	 */
	protected void check() {
		synchronized (m_variables) {
			synchronized (m_addedVars) {
				m_addedVars.setSize(0);
				m_execUnits.setSize(0);
				m_accessHandles.setSize(0);
				// check all variables
				for (int i = 0; i < m_variables.size(); i++) {
					KStructVarWrapper var = (KStructVarWrapper) m_variables.elementAt(i);
					TcAccessHandle ah = var.getAccessHandle();
					if (var.isMapTo() && (ah != null)) {
						m_addedVars.addElement(var);
						m_accessHandles.addElement(ah);
					}
				}
			}
		}
	}

	protected void refresh() {
		if (m_addedVars == null || m_addedVars.size() <= 0) {
			check();
			return;
		}
		synchronized (m_variables) {
			synchronized (m_addedVars) {

				Vector varsToAdd = new Vector();
				Vector newAH = new Vector();
				int cnt = getVarsToAdd(newAH, varsToAdd);
				if (varsToAdd.size() > 0) {
					System.out.println(refreshCnt + ": " + m_sGroupName + " should ADD " + varsToAdd.size() + " new vars!");
					for (int i = 0; i < cnt; i++) {
						m_addedVars.addElement(varsToAdd.elementAt(i));
					}
				}

				Vector vars2Del = getVarsToDelete();
				if (vars2Del.size() > 0) {
					System.out.println(refreshCnt + ": " + m_sGroupName + " should DELETE " + vars2Del.size() + " vars!");
					for (int i = 0; i < vars2Del.size(); i++) {
						m_addedVars.removeElement(vars2Del.elementAt(i));
					}
				}
			}
		}
		refreshCnt++;
	}

}
