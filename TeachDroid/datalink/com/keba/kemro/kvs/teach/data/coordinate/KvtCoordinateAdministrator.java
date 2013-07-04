/*-------------------------------------------------------------------------
*                   (c) 1999 by KEBA Ges.m.b.H & Co
*                            Linz/AUSTRIA
*                         All rights reserved
*--------------------------------------------------------------------------
*    Projekt   : KEMRO.teachview.4
*------------------------------------------------------------------------*/    
package com.keba.kemro.kvs.teach.data.coordinate;

import java.util.*;

import com.keba.kemro.kvs.teach.data.rc.*;
import com.keba.kemro.kvs.teach.util.*;
import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.constant.*;
import com.keba.kemro.teach.dfl.structural.type.*;
import com.keba.kemro.teach.dfl.value.*;

/**
 * manages coordinates
 */
public class KvtCoordinateAdministrator
	implements KVariableGroupListener, KStructAdministratorListener {
	private static KvtCoordinateAdministrator m_admin;
	private static Hashtable m_values;
	private static KVariableGroup m_group;
	private static SortVector m_activeSystems;
	private static Vector m_listeners;
	private static KStructTypeEnum m_referenceSystems;
	private static KStructConstEnum m_invalidSystem;
	private static final String TYPE_KEY = "TRcuReferenceSystem";
	private KTcDfl dfl;

	public boolean isReferenceSystem(KStructType type) {
		if (m_referenceSystems != null) {
			return m_referenceSystems.equals(type);
		}
		return false;
	}

	public KStructConstEnum getInvalidSystem() {
		return m_invalidSystem;
	}

	private KvtCoordinateAdministrator() {
		m_values = new Hashtable(400);
		m_activeSystems = new SortVector();
		m_listeners = new Vector(3);
		
		KvtSystemCommunicator.addConnectionListener(new KvtTeachviewConnectionListener() {
		   public void teachviewConnected () {
		   	dfl = KvtSystemCommunicator.getTcDfl();
				synchronized (dfl.getLockObject()) {
					createVariableGoups();
					dfl.structure.addStructAdministratorListener(KvtCoordinateAdministrator.this);
					treeChanged(dfl.structure.getRoot());
				}			
		   }
		   public void teachviewDisconnected () {
		   	synchronized (dfl.getLockObject()) {
					dfl.structure.removeStructAdministratorListener(KvtCoordinateAdministrator.this);
					removeVariables();
			   	dfl = null;	
					fillReferenceSystem();
					fireChanged();
		   	}
		   }
		});
	}
	
	private void createVariableGoups () {
		KTcDfl d = dfl;
		if (d != null) {
			m_group = d.variable.createVariableGroup("ReferenceSystems");
			m_group.addListener(this);
			m_group.setPollInterval(5000);
		}
	}

	private void removeVariables() {
		if (m_group != null) {
			m_group.release();
			m_group = null;
		}
	}
	
	public void addCoordinateListener(KvtCoordinateListener listener) {
		if (!m_listeners.contains(listener)) {
			m_listeners.addElement(listener);
		}
	}
	public void removeCoordinateListener(KvtCoordinateListener listener) {
		m_listeners.removeElement(listener);
	}

	private void fillReferenceSystem() {
		m_activeSystems.removeAllElements();
		m_activeSystems.m_changed = false;
		m_values.clear();
		m_referenceSystems = null;
		if (m_group != null) {
			m_group.release();					
			setReferenceEnumType();
			if (m_referenceSystems != null) {
				int cnt = m_referenceSystems.constants.getChildCount();
				KStructVarWrapper wrappi;
				for (int i = 0; i < cnt; i++) {
					KStructConstEnum e = (KStructConstEnum) m_referenceSystems.constants.getChild(i);
					int pos = ((Number) e.getInitValue()).intValue();
					if (pos == -1) {
						m_invalidSystem = e;
					} else {
						KTcDfl d = dfl;
						if (d != null) {
							wrappi =	d.variable.createKStructVarWrapper(KvtRcAdministrator.RCDATA_PREFIX + "gRcData.refSysMask[" + pos + "]");
							if (wrappi != null) {
								m_group.add(wrappi);
								m_values.put(wrappi, e);
							}
						}
					}
				}
			}
			m_group.activate();
		}
	}

	public Enumeration getValidSystems() {

		return m_activeSystems.elements();
	}

	private void fireChanged() {
		for (int i = 0; i < m_listeners.size(); i++) {
			((KvtCoordinateListener) m_listeners.elementAt(i)).coordinateSystemsChanged();
		}
	}

	private void setReferenceEnumType() {
		KTcDfl d = dfl;
		if (d != null) {
		   KStructNode n = d.structure.getKStructNode(TYPE_KEY);
			if (n instanceof KStructTypeEnum) {
				m_referenceSystems = (KStructTypeEnum) n;
			} else {
				m_referenceSystems = null;
			}
		} else {
			m_referenceSystems = null;
		}
	}

	public static KvtCoordinateAdministrator getAdmin() {
		return m_admin;
	}

	public static void init() {
		if (m_admin == null) {
			m_admin = new KvtCoordinateAdministrator();
		}
	}

	/**
	 * @see com.keba.kemro.teach.dfl.value.KVariableGroupListener#changed(com.keba.kemro.teach.dfl.value.KStructVarWrapper)
	 */
	public void changed(KStructVarWrapper variable) {
		synchronized (m_activeSystems) {
			Object o = variable.getActualValue();
			if (o instanceof Boolean) {
				if (((Boolean) o).booleanValue()) {
					m_activeSystems.addElement((KStructConstEnum) m_values.get(variable));
				} else {
					m_activeSystems.removeElement((KStructConstEnum) m_values.get(variable));
				}
			}
		}
	}

	/**
	 * @see com.keba.kemro.teach.dfl.value.KVariableGroupListener#allActualValuesUpdated()
	 */
	public void allActualValuesUpdated() {
		if (m_activeSystems.m_changed) {
			m_activeSystems.sort();
			m_activeSystems.m_changed = false;
			fireChanged();
		}
	}

	private static class SortVector extends Vector {
		protected boolean m_changed = false;

		public void addElement(KStructConstEnum v) {
			if ((v != null) && (!super.contains(v))) {
				super.addElement(v);
				m_changed = true;
			}
		}

		public void removeElement(KStructConstEnum v) {
			if ((v != null) && (super.removeElement(v))) {
				m_changed = true;
			}
		}

		private void sort() {
			int r;
			int gap;
			int counter;
			int pointer;
			Object temp;
			r = elementCount;
			gap = r / 2;
			while (gap > 0) {
				for (counter = gap; counter < r; counter++) {
					pointer = counter - gap;
					while (pointer >= 0) {
						int v1 = ((Integer) ((KStructConstEnum) elementData[pointer]).getInitValue()).intValue();
						int v2 = ((Integer) ((KStructConstEnum) elementData[gap + pointer]).getInitValue()).intValue();
						if (v1 > v2) {
							temp = elementData[pointer];
							elementData[pointer] = elementData[gap + pointer];
							elementData[gap + pointer] = temp;
							pointer = pointer - gap;
						} else {
							pointer = -1;
						}
					}
				}
				gap = gap / 2;
			}

		}
	}

	/**
	 * @see com.keba.kemro.teach.dfl.structural.KStructAdministratorListener#treeChanged(com.keba.kemro.teach.dfl.structural.KStructNode)
	 */
	public void treeChanged(KStructNode parent) {
		if (parent instanceof KStructScope) {
			fillReferenceSystem();
		}
	}

	/**
	 * @see com.keba.kemro.teach.dfl.structural.KStructAdministratorListener#nodeInserted(com.keba.kemro.teach.dfl.structural.KStructNode, com.keba.kemro.teach.dfl.structural.KStructNode)
	 */
	public void nodeInserted(KStructNode parent, KStructNode node) {
		if (node instanceof KStructScope) {
			fillReferenceSystem();
		}
	}

	/**
	 * @see com.keba.kemro.teach.dfl.structural.KStructAdministratorListener#nodeRemoved(com.keba.kemro.teach.dfl.structural.KStructNode, com.keba.kemro.teach.dfl.structural.KStructNode)
	 */
	public void nodeRemoved(KStructNode parent, KStructNode node) {
		if (node instanceof KStructScope) {
			fillReferenceSystem();
		}
	}

}
