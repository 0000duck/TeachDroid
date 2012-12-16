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

import java.util.*;

//import com.keba.kemro.kvs.teach.framework.util.KvtLogger;
import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.util.KDflLogger;
import com.keba.kemro.teach.network.*;

/**
 * Eine Variablengruppe verwaltet mehrere Variablen und stellt eine
 * Listener-Schnittstelle zur Verfügung, die den Klienten über Änderungen der
 * Variablenwerte informiert. Zusätzlich kann die Abfragezykluszeit für jede
 * Variablengruppe eigens festgelegt werden.
 */
public class KVariableGroup {
	/**
	 * 
	 */
	protected static final int TC_VAR_BATCHSIZE = 150;

	/**
	 * 
	 */
	private static final int MAX_TC_VAR_AMOUNT = 150;

	private static int groupCounter;

	protected String m_sGroupName;
	protected KVariableGroupListener m_listener;
	protected int m_pollCounter = 7;
	private int m_pollInterval = 8;

	// means 800ms

	protected TcVariableGroup tcGroup;
	protected String tcGroupName;

	/** Alle Variablen einer Gruppe */
	protected Vector m_variables = new Vector();
	/** Variablen zu denen es Aktualwerte gibt. (Programm ist in Ausführung) */
	protected Vector m_addedVars = new Vector();
	/** Verschiedene Ausführungseinheiten der einzelnen Variablen */
	protected Vector m_execUnits = new Vector();
	/** Zugriffshandle für die einzelnen Variablen */
	protected Vector m_accessHandles = new Vector();
	/** Variablenwerte */
	protected Vector m_values = new Vector();
	// protected int[] variableIds;
	protected int[] changedIds;
	protected boolean firstChangeCall = true;

	protected int refreshCnt;
	protected long m_lastPollTime;

	protected boolean m_shouldNotify;

	protected static KTcDfl dfl;

	/**
	 * Erzeugt eine Variablengruppe mit Namen und erfordert dabei die
	 * Bekanntgabe des jeweilen Klienten(Listener) der Gruppe.
	 * 
	 * @param sGroupName
	 *            Name der Variablengruppe
	 * @param listener
	 *            Referenz auf das Listenerobjekt der Variablengruppe
	 */
	public KVariableGroup(String sGroupName) {
		if (sGroupName == null || sGroupName.length() <= 0) {
			// System.out.println(getClass(),
			// "Attempt to add a KVariableGroup without a name detected!");
		}
		m_sGroupName = sGroupName;
	}

	protected static void setDFL(KTcDfl df) {
		dfl = df;
	}

	public void addListener(KVariableGroupListener listener) {
		m_listener = listener;
	}

	/**
	 * Liefert den Namen der Gruppe.
	 * 
	 * @return Name der Gruppe
	 */
	public String getGroupName() {
		return m_sGroupName;
	}

	/**
	 * Fügt eine Variable in Form eines Wrappers der Gruppe hinzu.
	 * 
	 * @param variable
	 *            Wrapper-Objekt für eine Variable
	 */
	public void add(KStructVarWrapper variable) {
		synchronized (m_variables) {
			if (variable == null) {
				KDflLogger.error(this, "cannot add variable to list, because wrapper for data key is null!");
				return;
			}
			if (!m_variables.contains(variable)) {
				m_variables.addElement(variable);
				firstChangeCall = true;
			}
		}
	}

	/**
	 * Entfernt eine Variable aus einer Gruppe.
	 * 
	 * @param variable
	 *            Wrapper-Objekt für eine Variable
	 */
	public void remove(KStructVarWrapper variable) {
		synchronized (m_variables) {
			m_variables.removeElement(variable);
			firstChangeCall = true;
		}
	}

	/**
	 * Entfernt alle Variablen einer Gruppe.
	 */
	public void removeAll() {
		synchronized (m_variables) {
			m_variables.setSize(0);
			firstChangeCall = true;
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Return Value
	 */
	protected boolean shouldUpdateActualValues() {
		m_pollCounter++;
		m_pollCounter = m_pollCounter % m_pollInterval;
		return m_pollCounter == 0;
	}

	/**
	 * Setzt die Abfragezykluszeit in ms. Ist das Intervall kleiner als 100ms,
	 * wird es standardmäßig auf 100ms gesetzt und der Abfragezyklus neu
	 * gestartet.
	 * 
	 * @param interval
	 *            in ms
	 */
	public void setPollInterval(int interval) {
		if (interval < 100) {
			m_pollInterval = 1;
		} else {
			m_pollInterval = interval / 100;
		}
		m_pollCounter = m_pollInterval - 1;
		if (dfl != null) {
			dfl.variable.server.checkGroup(this);
		}
	}

	public int getPollInterval() {
		return m_pollInterval;
	}

	/**
	 * Aktiviert die Variablengruppe beim Variablen-Server.
	 */
	public void activate() {
		try {
			dfl.variable.server.activateGroup(this);
		} catch (Exception e) {
			// can be null in case of disconnect
		}
	}

	/**
	 * Entfernt die Variablengruppe vom Variablenserver und löscht die
	 * zugehörigen Variablen.
	 */
	public void release() {
		deactivate();
		removeAll();
	}

	/**
	 * Deaktiviert die Variablengruppe beim Server.
	 */
	public void deactivate() {
		if ((dfl != null) && (dfl.variable != null) && (dfl.variable.server != null)) {
			dfl.variable.server.deactivateGroup(this);
		}
	}

	public void reset() {
		synchronized (m_addedVars) {
			m_addedVars.setSize(0);
			m_execUnits.setSize(0);
			m_accessHandles.setSize(0);
			m_values.setSize(0);
			if (tcGroup != null) {
				dfl.client.execution.removeVariableGroup(tcGroup);
			}
			tcGroup = null;
			// variableIds = null;
		}
	}

	/**
	 * Unterbricht den Abfragezyklus und holt für diese Gruppe die
	 * entsprechenden aktuellen Werte.
	 */
	public void checkActualValues() {
		dfl.variable.server.checkGroup(this);
	}

	/**
	 * Updates the values.
	 * 
	 * @return true if the values are successfully updated
	 */
	protected boolean update() {
		m_pollCounter = 0;
		boolean done = false;
		synchronized (m_addedVars) {
			if (tcGroup != null) {
				if (firstChangeCall) {
					firstChangeCall = false;
					changedIds = dfl.client.execution.getVariableGroupValues(tcGroup, m_values);
					// KvtLogger.info(this, refreshCnt + ": " + m_sGroupName +
					// " updated all (" + m_addedVars.size() + "/" +
					// changedIds.length + ")= variables");

				} else {
					changedIds = dfl.client.execution.getChangedVariableGroupValues(tcGroup, m_values);
//					if (changedIds != null && changedIds.length > 0) {
//						 KvtLogger.info(this,refreshCnt + ": " + m_sGroupName
//						 + " updated " + changedIds.length + " variables");
//						for (int i = 0; i < changedIds.length; i++) {
//							 KvtLogger.info(this,"\t" +
//							 getVariableForId(changedIds[i]).m_rootPathString);
//						}
//					}
//					 }
				}
				done = true;
			} else {
				if (0 < m_addedVars.size()) {
					if (dfl.client.execution.getActualValues(m_accessHandles, m_execUnits, m_values)) {
						// KvtLogger.info(this,m_sGroupName + " fetched " +
						// m_accessHandles.size() + " variables");
						done = true;
					}
				}
			}
		}
		m_shouldNotify = true;
		m_lastPollTime = System.currentTimeMillis();
		return done;
	}

	protected boolean shouldNotify() {
		return m_shouldNotify;
	}

	protected long getLastPollTime() {
		return m_lastPollTime;
	}

	public void forceNotify() {
		synchronized (m_addedVars) {
			for (int i = 0; i < m_addedVars.size(); i++) {
				KStructVarWrapper var = (KStructVarWrapper) m_addedVars.elementAt(i);
				m_listener.changed(var);
			}
		}

	}

	/**
	 * Informiert den Listener der Gruppe, dass sich Werte einzelner Variablen
	 * in der Gruppe verändert haben.
	 */
	protected void notifyChanges() {
		synchronized (m_addedVars) {
			if (tcGroup != null) {
				int[] variableIds = getVarIDs(m_addedVars);
				if ((0 < m_addedVars.size()) && (variableIds != null) && (changedIds != null)) {
					for (int i = 0; i < changedIds.length; i++) {
						KStructVarWrapper var = getVariableForId(changedIds[i]);
						if (var != null) {
							TcValue value = (TcValue) m_values.elementAt(i);
							var.setValueObject(value);
							if (!value.isValid) {
								KDflLogger.warn(this, "Invalid value for variable " + var.getRootPathString());
							}
							if (var.valueChanged()) {
								m_listener.changed(var);
							}
						}
					}
				}
			} else {
				if (0 < m_addedVars.size()) {
					for (int i = 0; i < m_addedVars.size(); i++) {
						KStructVarWrapper var = (KStructVarWrapper) m_addedVars.elementAt(i);
						TcValue value = (TcValue) m_values.elementAt(i);
						var.setValueObject(value);
						if (var.valueChanged()) {
							if (m_listener != null)
								m_listener.changed(var);
						}
					}
				}
			}
			if (0 < m_addedVars.size()) {
				m_listener.allActualValuesUpdated();
			}
		}
		m_shouldNotify = false;
	}

	protected KStructVarWrapper getVariableForId(int id) {
		int beg = 0;
		int[] variableIds = getVarIDs(m_addedVars);
		int end = variableIds.length;
		// int index = end / 2;
		// while ((beg <= index) && (index < end)) {
		// if (id < variableIds[index]) {
		// end = index;
		// index = beg + (end - beg) / 2;
		// } else if (variableIds[index] < id) {
		// beg = index + 1;
		// index = beg + (end - beg) / 2;
		// } else {
		// return (KStructVarWrapper) m_addedVars.elementAt(index);
		// }
		// }

		for (int i = 0; i < end; i++) {
			if (variableIds[i] == id)
				return (KStructVarWrapper) m_addedVars.elementAt(i);
		}
		return null;
	}

	/**
	 * @return
	 */
	private int[] getVarIDs(Vector _kstructvarwrappers) {
		if (_kstructvarwrappers == null)
			return null;
		int num = _kstructvarwrappers.size();
		int[] ids = new int[num];
		for (int i = 0; i < _kstructvarwrappers.size(); i++) {
			ids[i] = ((KStructVarWrapper) _kstructvarwrappers.elementAt(i)).getVarId();
		}
		return ids;

	}

	/**
	 * Überprüft die Ausführungseinheiten der Variablen. Wird aufgerufen, wenn
	 * neue Ausführungseinheiten hinzukommen bzw. bestehende nicht mehr gültig
	 * sind.
	 * 
	 */
	protected void check() {
		synchronized (m_variables) {
			synchronized (m_addedVars) {
				m_addedVars.setSize(0);
				m_execUnits.setSize(0);
				m_accessHandles.setSize(0);
				m_values.setSize(0);

				// check all variables
				for (int i = 0; i < m_variables.size(); i++) {
					KStructVarWrapper var = (KStructVarWrapper) m_variables.elementAt(i);
					TcAccessHandle ah = var.getAccessHandle();
					if (ah != null) {
						m_addedVars.addElement(var);
						m_accessHandles.addElement(ah);
						m_execUnits.addElement(null); // isn't needed anymore
						m_values.addElement(new TcValue());
					}
				}
				tcGroupName = String.valueOf(getNextGroupNumber());
				tcGroup = dfl.client.execution.createVariableGroup(tcGroupName);
				if (tcGroup != null) {
					firstChangeCall = true;
					int[] variableIds = dfl.client.execution.addVariablesToGroup(tcGroup, m_accessHandles);
					setVarIds(variableIds, m_variables);
				} else {
					KDflLogger.warn(this, "cannot create variable group " + getGroupName());
				}
			}
		}
	}

	/**
	 * 
	 */
	protected void refresh() {
		if (m_addedVars == null || m_addedVars.size() <= 0) {
			check();
			return;
		}
		synchronized (m_variables) {
			synchronized (m_addedVars) {

				Vector newAccessHandles = new Vector();
				Vector varsToDelete = new Vector();
				Vector varsToAdd = new Vector();

				int remainingVars = getVarsToAdd(newAccessHandles, varsToAdd);

				int chunk = TC_VAR_BATCHSIZE; // how many variables should be
												// added in one cycle?
				int cycle = 0;

				while (remainingVars > 0) {

					// copy <chunk> variables to a temp vector
					Vector tmp = new Vector();
					int l;
					if (remainingVars > chunk) {
						l = chunk;
					} else {
						l = remainingVars;
					}
					for (int i = 0; i < l; i++) {
						tmp.addElement(newAccessHandles.elementAt(chunk * cycle + i));
					}

					// add all new vars to the TcGroup
					int[] newvariableIds = dfl.client.execution.addVariablesToGroup(tcGroup, tmp);
					if (newvariableIds != null && newvariableIds.length > 0) {
						setVarIds(newvariableIds, varsToAdd, cycle * chunk);

						// really update internal data
						for (int i = 0; i < newvariableIds.length; i++) {
							KStructVarWrapper vm = (KStructVarWrapper) varsToAdd.elementAt(cycle * chunk + i);
							m_addedVars.addElement(vm);
							m_accessHandles.addElement(vm.getAccessHandle());
							m_execUnits.addElement(null);
							m_values.addElement(new TcValue());
						}
					} else {
						// KvtLogger.error(this,refreshCnt + ": " + m_sGroupName
						// + " adding " + tmp.size() + " variables FAILED!!!");

					}
					remainingVars -= chunk;
					cycle++;
				}

				varsToDelete = getVarsToDelete();
				int[] ids = getVarIDs(varsToDelete);

				if (varsToDelete.size() > 0) {
					boolean success = dfl.client.execution.removeVariablesFromGroup(tcGroup, ids);
					String s = "";
					// debug printing
					if (varsToDelete.size() < 7) {
						for (int i = 0; i < varsToDelete.size(); i++) {
							s += varsToDelete.elementAt(i).toString() + " ";
						}
					}
					// KvtLogger.info(this,refreshCnt + ": " + m_sGroupName +
					// " deleting " + ids.length + " variables " + (success ?
					// "successful " : "NOT SUCCESSFUL ") + s);
					if (success) {
						for (int i = 0; i < varsToDelete.size(); i++) {
							m_addedVars.removeElement(varsToDelete.elementAt(i));
							m_values.removeElement(((KStructVarWrapper) varsToDelete.elementAt(i)).getValueObject());
							m_accessHandles.removeElement(((KStructVarWrapper) varsToDelete.elementAt(i)).getAccessHandle());
							m_execUnits.removeElementAt(0);
						}
					}// if success
				}// if varsToDelete
			}
		}
		refreshCnt++;

	}

	protected Vector getVarsToDelete() {
		Vector varsToDelete = new Vector();
		// determine vars to delete
		for (int j = 0; j < m_addedVars.size(); j++) {
			KStructVarWrapper vm = (KStructVarWrapper) m_addedVars.elementAt(j);
			if (!m_variables.contains(vm)) {
				varsToDelete.addElement(vm);
			}
		}
		return varsToDelete;
	}

	/**
	 * Determines how many variables must be added to the TC group
	 * 
	 * @param _accessHandles
	 *            In-Out parameter that will receive the access handles of the
	 *            variables which must be added
	 * @param _varsToAdd
	 *            In-Out parameter that will receive the variables which must be
	 *            added
	 * @return the amount of variables that where added. Equal to
	 *         _varsToAdd.size()
	 */
	protected int getVarsToAdd(Vector _accessHandles, Vector _varsToAdd) {
		int cnt = 0;
		if (_accessHandles == null || _varsToAdd == null)
			throw new NullPointerException("In-out parameter _accessHandles and _varsToAdd cannot be null!!");
		// determine vars to add
		for (int i = 0; i < m_variables.size(); i++) {
			KStructVarWrapper vm = (KStructVarWrapper) m_variables.elementAt(i);

			// only add a distinct amount of variables, TC will reject
			// anything > MAX_TC_VAR_AMOUNT
			// any variables left out here will be added in the next
			// cycle
			if (!m_addedVars.contains(vm) && cnt < MAX_TC_VAR_AMOUNT) {
				_accessHandles.addElement(vm.getAccessHandle());
				_varsToAdd.addElement(vm);

				// firstChangeCall = true;
				cnt++;
			}
		}
		return cnt;
	}

	/**
	 * @param _ids
	 * @param _kstructVarWrappers
	 */
	private void setVarIds(int[] _ids, Vector _kstructVarWrappers) {
		if (_ids.length <= _kstructVarWrappers.size()) {
			for (int i = 0; i < _ids.length; i++) {
				((KStructVarWrapper) _kstructVarWrappers.elementAt(i)).setVarId(_ids[i]);
			}
		}

	}

	private void setVarIds(int[] _ids, Vector _varWrappers, int _startIdx) {

		for (int i = 0; i < _ids.length; i++) {
			((KStructVarWrapper) _varWrappers.elementAt(_startIdx + i)).setVarId(_ids[i]);
		}

	}

	protected int getNextGroupNumber() {
		groupCounter++;
		return groupCounter;
	}

	public String toString() {
		return "GroupName: \"" + m_sGroupName + "\" Listener: \"" + m_listener.getClass().getSimpleName();
	}

	// public boolean equals(Object _other) {
	// if(_other == null || !(_other instanceof KVariableGroup)) {
	// return false;
	// }
	// boolean ret =
	// m_sGroupName.equals(((KVariableGroup)_other).getGroupName());
	// return ret;
	//
	// }

	public String toStringAll() {
		StringBuffer buf = new StringBuffer();
		buf.append(toString() + "\n");

		synchronized (m_variables) {
			for (int i = 0; i < m_variables.size(); i++) {
				KStructVarWrapper v = (KStructVarWrapper) m_variables.elementAt(i);
				buf.append("      |-->   " + v.key + "\t @ " + v.m_rootPathString + "\n");
			}
		}
		return buf.toString();
	}

	public Vector getVariables() {
		return m_variables;
	}

}
