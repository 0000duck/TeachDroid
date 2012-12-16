//NEW
/**
 * 
 */
package com.keba.kemro.teach.dfl.value;

import java.util.Enumeration;
import java.util.Vector;

import com.keba.kemro.teach.dfl.KTcDfl;

/**
 * @author ltz
 * 
 */
public class KvtVariableRegistry {

	/**
	 * 
	 */
	private Vector				/* <Entry> */m_entries;
	protected KVariableGroup	masterGroupFast;
	protected KVariableGroup	masterGroupMid;
	protected KVariableGroup	masterGroupSlow;
	protected KVariableGroup	masterMapGroup;
	protected KVariableGroup	masterRoutineGroup;
	private static final int	POLL_CYCLE_FAST		= 200;			// ms
	private static final int	POLL_CYCLE_MEDIUM	= 500;
	private static final int	POLL_CYCLE_SLOW		= 800;
	static final long			POLL_CYCLE_MAPTO	= 1000;
	private boolean				m_dirtyflag			= false;
	private final Object		dirtyLck			= new Object(); // monitor
																	// to sync
																	// access to
																	// m_dirtyFlag
	private KTcDfl				dfl;
	private int					fastUpdateCnt;
	private int					midCnt;
	private int					slowCnt;

	KvtVariableRegistry(KVariableServer kVariableServer, KTcDfl _dfl) {

		this.dfl = _dfl;
		masterGroupFast = this.dfl.variable.createVariableGroup("MasterGroup_FAST");
		masterGroupFast.addListener(kVariableServer);

		masterGroupMid = dfl.variable.createVariableGroup("MasterGroup_MID");
		masterGroupMid.addListener(kVariableServer);

		masterGroupSlow = dfl.variable.createVariableGroup("MasterGroup_SLOW");
		masterGroupSlow.addListener(kVariableServer);

		masterMapGroup = dfl.variable.createMapToVariableGroup("MasterGroup_MAP");
		masterMapGroup.addListener(kVariableServer);

		masterRoutineGroup = dfl.variable.createRoutineVariableGroup("MasterGroup_ROUTINE");
		masterRoutineGroup.addListener(kVariableServer);

		m_entries = new Vector();
	}

	public synchronized void addGroup(KVariableGroup _group) {

		if (_group.getVariables() == null || _group.getVariables().size() == 0) {
			return;
		}

		KVariableGroup grp = null;
		if (_group instanceof KMapToVariableGroup) {
			grp = masterMapGroup;
		} else if (_group instanceof KRoutineVariableGroup) {
			grp = masterRoutineGroup;

		} else {

			if (_group.getPollInterval() < 5) {
				grp = masterGroupFast;
			} else if (_group.getPollInterval() >= 5 && _group.getPollInterval() < 8) {
				grp = masterGroupMid;
			} else {
				grp = masterGroupSlow;
			}
		}
		for (int i = 0; i < _group.getVariables().size(); i++) {
			KStructVarWrapper v = (KStructVarWrapper) _group.getVariables().elementAt(i);
			if (!v.isWriteOnly()) {
				registerGroupForVar(_group, v); // add the variable to the var
				grp.add(v);
				if (grp == masterRoutineGroup)
					((KRoutineVariableGroup) grp).setExecRoutineNode(((KRoutineVariableGroup) _group).getExecRoutineNode());

			}
		}
		setDirty(true);
	}

	/**
	 * @param _group
	 * @param v
	 */
	private void registerGroupForVar(KVariableGroup _group, KStructVarWrapper _var) {
		if (m_entries == null) {
			m_entries = new Vector();
			return;
		}
		boolean varfound = false;
		for (int i = 0; i < m_entries.size() && !varfound; i++) {
			Entry e = (Entry) m_entries.elementAt(i);
			if (e.getVar().equals(_var)) { // check if variable exists
				varfound = true;
				if (!e.containsGroup(_group)) { // check if the same
												// group
												// is already registered
					e.addGroup(_group);
				}
			}
		}

		if (!varfound) { // add the variable and register the group, if
							// necessary
			m_entries.addElement(new Entry(_var, _group));
		}

	}

	public synchronized void removeGroup(KVariableGroup _group) {
		if (_group.getVariables() == null || _group.getVariables().size() == 0) {
			return;
		}

		KVariableGroup grp = null;

		if (_group instanceof KMapToVariableGroup) {
			grp = masterMapGroup;
		} else if (_group instanceof KRoutineVariableGroup) {
			grp = masterRoutineGroup;

		} else {

			if (_group.getPollInterval() < 5)
				grp = masterGroupFast;
			else if (_group.getPollInterval() >= 5 && _group.getPollInterval() < 8)
				grp = masterGroupMid;
			else
				grp = masterGroupSlow;
		}

		for (int i = 0; i < _group.getVariables().size(); i++) {
			KStructVarWrapper v = (KStructVarWrapper) _group.getVariables().elementAt(i);
			removeGroupForVar(_group, v);
			grp.remove(v);
		}
	}

	/**
	 * @param _group
	 * @param v
	 */
	private void removeGroupForVar(KVariableGroup _group, KStructVarWrapper v) {
		for (int i = 0; i < m_entries.size(); i++) {
			Entry e = (Entry) m_entries.elementAt(i);
			if (e.getVar().equals(v)) {
				boolean removed = e.removeGroup(_group);
				if (e.getGroups() == null || e.getGroups().size() == 0) {
					m_entries.removeElementAt(i); // also remove
													// variable if no
					// more groups are
					// registered
				}
				return; // abort search after first found variable
			}
		}

	}

	public synchronized boolean isRegistered(KStructVarWrapper _var) {

		for (int i = 0; i < m_entries.size(); i++) {
			Entry e = (Entry) m_entries.elementAt(i);
			if (e.getVar().equals(_var))
				return true;
		}
		return false;

	}

	public Vector getAllMapToGroups() {
		if (m_entries == null) {
			m_entries = new Vector();
			return null;
		}

		Vector grps = new Vector();

		for (int i = 0; i < m_entries.size(); i++) {
			Entry e = (Entry) m_entries.elementAt(i);

			Vector res = e.getGroups();

			for (int j = 0; j < res.size(); j++) {
				if (res.elementAt(j) instanceof KMapToVariableGroup)
					grps.addElement(res.elementAt(j));
			}
		}

		return grps;
	}

	public Vector getGroupsForVar(KStructVarWrapper _var) {
		Vector res = null;
		// synchronized (m_entries) {
		if (m_entries == null) {
			m_entries = new Vector();
			return null;
		}
		for (int i = 0; i < m_entries.size(); i++) {
			Entry e = (Entry) m_entries.elementAt(i);
			if (e.getVar().equals(_var)) {
				res = e.getGroups();
			}
		}
		// }
		return res;
	}

	public int size() {
		int s = -1;
		synchronized (m_entries) {
			s = m_entries.size();
		}
		return s;
	}

	public Entry elementAt(int _index) {
		return (Entry) m_entries.elementAt(_index);
	}

	class Entry {
		private KStructVarWrapper	m_key;
		private Vector				/* <KVariableGroup */m_varGroups;
		private boolean				m_forceCheck	= false;

		public Entry(KStructVarWrapper _var) {
			m_key = _var;
			m_varGroups = new Vector();
		}

		/**
		 * @param _group
		 */
		public boolean removeGroup(KVariableGroup _group) {
			return m_varGroups.removeElement(_group);
		}

		/**
		 * @param _group
		 * @return
		 */
		public boolean containsGroup(KVariableGroup _group) {
			if (m_varGroups == null)
				m_varGroups = new Vector();
			return m_varGroups.contains(_group);
		}

		/**
		 * @param _var
		 * @param _group
		 */
		public Entry(KStructVarWrapper _var, KVariableGroup _group) {
			m_key = _var;
			if (m_varGroups == null)
				m_varGroups = new Vector();
			m_varGroups.addElement(_group);
		}

		/**
		 * @param _group
		 */
		public void addGroup(KVariableGroup _group) {
			if (m_varGroups == null)
				m_varGroups = new Vector();
			m_varGroups.addElement(_group);
		}

		public KStructVarWrapper getVar() {
			return m_key;
		}

		public Vector getGroups() {
			return m_varGroups;
		}

		public String toString() {
			StringBuffer stb = new StringBuffer(m_key.getRootPathString());
			stb.append(":");

			for (int i = 0; i < m_varGroups.size(); i++) {
				stb.append(((KVariableGroup) m_varGroups.elementAt(i)).getGroupName() + ", ");
			}
			return stb.toString();
		}

		public boolean doForceCheck() {
			return m_forceCheck;
		}
	}

	/**
	 * @param i
	 * @return
	 */
	public synchronized KStructVarWrapper getVariableAt(int i) {
		Entry e = (Entry) m_entries.elementAt(i);

		if (e != null) {
			return e.getVar();
		}
		return null;
	}

	/**
	 * 
	 */
	public synchronized void refresh() {
		masterGroupFast.refresh();
		masterGroupMid.refresh();
		masterGroupSlow.refresh();
		masterMapGroup.refresh();
		masterRoutineGroup.refresh();
		setDirty(false);
	}

	/**
	 * 
	 */
	public synchronized void update() {
		long now = System.currentTimeMillis();
		if (now - masterGroupFast.getLastPollTime() >= POLL_CYCLE_FAST) {
			masterGroupFast.update();
			// System.out.println("FAST update done");
			fastUpdateCnt++;
		}
		if (now - masterGroupMid.getLastPollTime() >= POLL_CYCLE_MEDIUM) {
			masterGroupMid.update();
			// System.out.println("MID update done");
			midCnt++;
		}
		if (now - masterGroupSlow.getLastPollTime() >= POLL_CYCLE_SLOW) {
			masterGroupSlow.update();
			// System.out.println("SLOW update done");
			slowCnt++;
		}

		if (now - masterMapGroup.getLastPollTime() >= POLL_CYCLE_MAPTO) {
			masterMapGroup.update();
		}

		if (now - masterRoutineGroup.getLastPollTime() >= POLL_CYCLE_MEDIUM) {
			masterRoutineGroup.update();
		}

	}

	/**
	 * 
	 */
	public void notifyChanges() {
		if (masterGroupFast.shouldNotify())
			masterGroupFast.notifyChanges();

		if (masterGroupMid.shouldNotify())
			masterGroupMid.notifyChanges();

		if (masterGroupSlow.shouldNotify())
			masterGroupSlow.notifyChanges();

		if (masterMapGroup.shouldNotify()) {
			masterMapGroup.notifyChanges();

		}
		if (masterRoutineGroup.shouldNotify()) {
			masterRoutineGroup.notifyChanges();
		}
	}

	public synchronized boolean isDirty() {
		synchronized (dirtyLck) {
			return m_dirtyflag;
		}

	}

	/**
	 * @param b
	 */
	private void setDirty(boolean _dirty) {
		synchronized (dirtyLck) {
			m_dirtyflag = _dirty;
		}

	}

	/**
	 * @return
	 */
	public synchronized Enumeration getAllListeners() {
		Vector v = new Vector();
		for (int i = 0; i < m_entries.size(); i++) {
			Entry e = (Entry) m_entries.elementAt(i);

			for (int j = 0; j < e.getGroups().size(); j++) {
				KVariableGroupListener listener = ((KVariableGroup) e.getGroups().elementAt(j)).m_listener;
				if (!v.contains(listener)) {
					v.addElement(listener);
				}
			}

		}
		return v.elements();
	}

}