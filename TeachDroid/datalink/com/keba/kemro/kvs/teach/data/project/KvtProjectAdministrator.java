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
package com.keba.kemro.kvs.teach.data.project;

import java.util.List;
import java.util.Vector;

import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener;
import com.keba.kemro.kvs.teach.util.Log;
import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.dir.KDirEntry;
import com.keba.kemro.teach.dfl.dir.KDirectoryAdministratorListener;
import com.keba.kemro.teach.dfl.execution.KExecAdministratorListener;
import com.keba.kemro.teach.dfl.execution.KExecUnitNode;
import com.keba.kemro.teach.dfl.execution.KExecUnitProject;
import com.keba.kemro.teach.dfl.execution.KExecUnitRoutine;
import com.keba.kemro.teach.dfl.execution.KExecUnitScope;
import com.keba.kemro.teach.dfl.structural.KStructAdministratorListener;
import com.keba.kemro.teach.dfl.structural.KStructNode;
import com.keba.kemro.teach.dfl.structural.KStructNodeVector;
import com.keba.kemro.teach.dfl.structural.KStructProgram;
import com.keba.kemro.teach.dfl.structural.KStructProject;
import com.keba.kemro.teach.dfl.structural.KStructRoot;
import com.keba.kemro.teach.dfl.structural.KStructScope;

/**
 * Diese Klasse liefert alle Methoden zur Bearbeitung von Projekten
 * 
 * @author ede
 */

public class KvtProjectAdministrator {
	public static final String								GLOBAL_VAR_PRG	= "_globalvars";
	private static Vector<KvtProject>						m_projects;
	private static Vector<KvtProjectAdministratorListener>	m_listener;
	private static KTcDfl									dfl;
	private static KProjectListener							listener;

	/**
	 * Initialisierung
	 */
	public static void init() {
		m_projects = new Vector<KvtProject>();
		listener = new KProjectListener();
		KvtSystemCommunicator.addConnectionListener(new KvtTeachviewConnectionListener() {
			public void teachviewConnected() {
				dfl = KvtSystemCommunicator.getTcDfl();
				synchronized (dfl.getLockObject()) {

					Log.i("TC connection", "Project administrator connecting...");
					dfl.directory.addDirectoryAdminListener(listener);
					dfl.structure.addStructAdministratorListener(listener);
					dfl.execution.addListener(listener);
					listener.directoryProjectsChanged();

					Log.i("TC connection", "Project administrator connected!");

				}
			}

			public void teachviewDisconnected() {
				// synchronized (dfl.getLockObject()) {
				dfl.directory.removeDirectoryAdminListener(listener);
				dfl.structure.removeStructAdministratorListener(listener);
				dfl.execution.removeListener(listener);
				m_projects.setSize(0);
				dfl = null;
				fireProjectListChanged();
				// }
			}
		});
	}

	/**
	 * Liefert alle Projekte
	 * 
	 * @return Eine Liste <code>KProject</code>
	 */
	public static KvtProject[] getAllProjects() {

		if (m_projects != null) {
			KvtProject[] returnList = new KvtProject[m_projects.size()];
			m_projects.copyInto(returnList);
			return returnList;
		}
		return null;

	}
	
	/**
	 * Liefert alle Projekte
	 * 
	 * @return Eine Liste <code>KProject</code>
	 */
	public static List<KvtProject> getAllProjectsList() {

		if (m_projects != null) {
			List<KvtProject> returnList = new Vector<KvtProject>();
			returnList.addAll(m_projects);
			return returnList;
		}
		return null;

	}

	/**
	 * Returns the project with the given name.
	 * 
	 * @param name
	 *            project name
	 * @return project
	 */
	public static KvtProject getProject(String name) {
		KTcDfl d = dfl;
		if (d != null) {
			synchronized (d.getLockObject()) {
				for (int i = 0; i < m_projects.size(); i++) {
					KvtProject project = (KvtProject) m_projects.elementAt(i);
					if (project.toString().equalsIgnoreCase(name)
							|| (project.isGlobalProject() && (project.getStructProject() != null) && (project.getStructProject().getKey()
									.equals(name)))) { // Multikin
						return project;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns the global project.
	 * 
	 * @return global project
	 */
	public static KvtProject getGlobalProject() {
		KTcDfl d = dfl;
		if (d != null) {
			synchronized (d.getLockObject()) {
				for (int i = 0; i < m_projects.size(); i++) {
					KvtProject project = (KvtProject) m_projects.elementAt(i);
					if (project.isGlobalProject()) {
						return project;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns the system project.
	 * 
	 * @return system project
	 */
	public static KvtProject getSystemProject() {
		KTcDfl d = dfl;
		if (d != null) {
			// synchronized (d.getLockObject()) {
			for (int i = 0; i < m_projects.size(); i++) {
				KvtProject project = (KvtProject) m_projects.elementAt(i);
				if (project.isSystemProject()) {
					return project;
				}
			}
			// }
		}
		return null;
	}

	/**
	 * Kompilieren eines Projektes
	 * 
	 * @param prj
	 *            Das Projekt das compiliert werden soll. 1
	 */
	public static boolean build(KvtProject prj) {
		int state = prj.getProjectState();
		if (state == KvtProject.NOT_BUILDED) {
			KTcDfl d = dfl;
			if (d != null) {
				return d.structure.buildProject(prj.getDirEntry());
			}
		}
		return (state >= KvtProject.BUILDED_WITHOUT_ERROR);
	}

	/**
	 * Entfernen eines Projektes aus dem Strukturbaum
	 * 
	 * @param prj
	 *            Das Projekt das entfernt werden soll.
	 */
	public static void destroy(KvtProject prj) {
		KStructProject structPrj = prj.getStructProject();
		if (structPrj != null) {
			KTcDfl d = dfl;
			if (d != null) {
				d.structure.destroyProject(structPrj);
			}
		}
	}

	/**
	 * Lädt ein Projekt
	 * 
	 * @param prj
	 *            Das Projekt das geladen werden soll
	 */
	public static void loadProject(KvtProject prj) {
		if (prj.getProjectState() == KvtProject.BUILDED_WITHOUT_ERROR) {
			KTcDfl d = dfl;
			if (d != null) {
				d.execution.loadProject(prj.getStructProject());
			}
		}
	}

	/**
	 * Entlädt das Projekt
	 * 
	 * @param prj
	 *            Das Projekt das entladen werden soll
	 */
	public static void unloadProject(KvtProject prj) {
		KExecUnitProject exeUnitPrj = prj.getExecUnitProject();
		if (exeUnitPrj != null) {
			KTcDfl d = dfl;
			if (d != null) {
				d.execution.unloadProject(exeUnitPrj);
			}
		}
	}

	/**
	 * Startet das Programm
	 * 
	 * @param prg
	 *            Das Program das gestartet werden soll.
	 */
	public static void startProgram(KvtProgram prg) {
		if (prg != null) {
			if (prg.getStructProgram() != null) {
				KTcDfl d = dfl;
				if (d != null) {
					d.execution.startProgram(prg.getStructProgram(), true, true);
				}
			}
		}
	}

	/**
	 * Stoppt ein Programm
	 * 
	 * @param prg
	 *            Das Programm das gestoppt werden soll
	 */
	public static void stopProgram(KvtProgram prg) {
		if ((prg != null) && (prg.getUnnamedExecUnitRoutine() != null)) {
			KTcDfl d = dfl;
			if (d != null) {
				d.execution.stopExecutionUnit(prg.getUnnamedExecUnitRoutine());
			}
		}

	}

	/**
	 * Unterbricht ein Programm
	 * 
	 * @param prg
	 *            Das Programm das unterbrochen werden soll
	 */
	public static void interruptProgram(KvtProgram prg) {
		if ((prg != null) && (prg.getUnnamedExecUnitRoutine() != null)) {
			KTcDfl d = dfl;
			if (d != null) {
				d.execution.interruptExecutionUnit(prg.getUnnamedExecUnitRoutine());
			}
		}
	}

	/**
	 * Unterbrochenes Program wird wieder gestartet
	 * 
	 * @param prg
	 *            Unterbrochenes Program wird wieder gestartet.
	 */
	public static void continueProgram(KvtProgram prg) {
		if ((prg != null) && (prg.getUnnamedExecUnitRoutine() != null)) {
			KTcDfl d = dfl;
			if (d != null) {
				d.execution.continueExecutionUnit(prg.getUnnamedExecUnitRoutine());
			}
		}
	}

	public static void setMainFlowStepping(KvtProgram program, boolean enable) {
		if ((program != null) && (program.getUnnamedExecUnitRoutine() != null)) {
			KTcDfl d = dfl;
			if (d != null) {
				d.execution.setMainFlowStepping(program.getUnnamedExecUnitRoutine(), enable);
			}
		}
	}

	/**
	 * Fügt einen Programlistener hinzu
	 * 
	 * @param newListener
	 *            Programlistener
	 */
	public static void addProjectListener(KvtProjectAdministratorListener newListener) {
		if (m_listener == null) {
			m_listener = new Vector<KvtProjectAdministratorListener>();
		}
		if (!m_listener.contains(newListener)) {
			m_listener.add(newListener);
		}
	}

	/**
	 * Entfernt einen Programlistener
	 * 
	 * @param listener
	 *            Programlistener
	 */
	public static void removeProjectListener(KvtProjectAdministratorListener listener) {
		if (m_listener.contains(listener)) {
			m_listener.removeElement(listener);
		}
	}

	/**
	 * Wird aufgerufen um alle Listener davon zu informieren dass sich der
	 * Projektzustand geändert hat.
	 * 
	 * @param prj
	 *            Das Projekt dessen Zustand sich geändert hat.
	 */
	public static void fireProjectStateChanged(KvtProject prj) {
		if (m_listener == null) {
			return;
		}
		for (int i = 0; i < m_listener.size(); i++) {
			try {
				((KvtProjectAdministratorListener) m_listener.elementAt(i)).projectStateChanged(prj);
			} catch (Exception e) {
				Log.e(KvtProjectAdministrator.class.toString(), "fireProjectStateChanged" + " Excp: " + e);
			}
		}
	}

	/**
	 * Benachrichtigung der listener daß sich der Zustand des Programmes prg
	 * geändert hat
	 * 
	 * @param prg
	 *            Das Programm dessen zustand sich geämdert hat.
	 */
	public static void fireProgramStateChanged(KvtProgram prg) {
		if (m_listener == null) {
			return;
		}
		for (int i = 0; i < m_listener.size(); i++) {
			try {
				((KvtProjectAdministratorListener) m_listener.elementAt(i)).programStateChanged(prg);
			} catch (Exception e) {
				Log.e(KvtProjectAdministrator.class.toString(), "fireProgramStateChanged" + " Excp: " + e);
			}
		}
	}

	/**
	 * Wird aufgerufen um die Listener zu informieren daß sich die Projektliste
	 * geändert aht
	 */
	public static void fireProjectListChanged() {
		if (m_listener == null) {
			return;
		}
		for (int i = 0; i < m_listener.size(); i++) {
			try {
				((KvtProjectAdministratorListener) m_listener.elementAt(i)).projectListChanged();
			} catch (Exception e) {
				Log.e(KvtProjectAdministrator.class.toString(), "fireProjectListChanged" + " Excp: " + e);
			}
		}
	}

	/**
	 * Liefert das laufende Projekt
	 * 
	 * @return das laufende Projekt
	 */
	public static KvtProject[] getCurrentRunningProjects() {
		KTcDfl d = dfl;
		if (d != null) {
			synchronized (d.getLockObject()) {
				KvtProject[] runningProjects = new KvtProject[m_projects.size()];
				int count = 0;
				for (int i = 0; i < m_projects.size(); i++) {
					KvtProject project = (KvtProject) m_projects.elementAt(i);
					if (!project.isGlobalProject() && !project.isSystemProject() && (KvtProject.NOT_ALREADY_LOADED <= project.getProjectState())) {
						runningProjects[count] = project;
						count++;
					}
				}
				if (0 < count) {
					KvtProject[] result = new KvtProject[count];
					System.arraycopy(runningProjects, 0, result, 0, count);
					return result;
				}
			}
		}
		return null;
	}

	private static class KProjectListener implements KDirectoryAdministratorListener, KExecAdministratorListener, KStructAdministratorListener {
		public Vector<KvtProject> directoryProjectsChanged() {
			KTcDfl d = dfl;
			Vector<KvtProject> tmp = new Vector<KvtProject>();
			if (d != null) {
				synchronized (d.getLockObject()) {
					m_projects.clear();
					Vector entryList = d.directory.getAllProjects();
					for (int i = 0; i < entryList.size(); i++) {
						KDirEntry entry = (KDirEntry) entryList.elementAt(i);
						KStructProject project = (KStructProject) d.structure.getKStructNode(entry);
						KExecUnitProject execUnit = null;
						if (project != null) {
							execUnit = checkExecUnitProject(project, d.execution.getRoot());
						}
						KvtProject prj = new KvtProject(entry, project, execUnit, d);
						m_projects.add(prj);
						Log.i("TC Connection", "project added: " + prj.getName());
						tmp.add(prj);
					}
					m_projects = tmp;
					fireProjectListChanged();
				}
			}

			return tmp;
		}

		public void nodeInserted(KStructNode parent, KStructNode node) {
			if (0 < m_projects.size()) {
				if (node instanceof KStructProject) {
					treeChanged(node);
				} else if (node instanceof KStructProgram) {
					String prjName = parent.getKey();
					KvtProject prj = null;
					for (int i = 0; i < m_projects.size(); i++) {
						KvtProject p = (KvtProject) m_projects.elementAt(i);
						if ((p != null) && prjName.equalsIgnoreCase(p.getDirEntry().getName())) {
							prj = p;
							break;
						}
					}
					if (prj != null) {
						if (prj.isLoaded() && (node instanceof KStructProgram)) {
							for (int i = 0; i < prj.getProgramCount(); i++) {
								KvtProgram p = prj.getProgram(i);
								if (p.getDirEntry().getName().equalsIgnoreCase(node.getKey())) {
									p.setStructProgram((KStructProgram) node);
									break;
								}
							}
						}
					}
				}
			}
		}

		public void nodeRemoved(KStructNode parent, KStructNode node) {
			if (0 < m_projects.size()) {
				String prjDirEntryPath = null;
				if (node instanceof KStructScope) {
					// remove projects recursive
					for (int i = 0; i < ((KStructScope) node).projects.getChildCount(); i++) {
						KStructProject p = (KStructProject) ((KStructScope) node).projects.getChild(i);
						nodeRemoved(node, p);
					}
				}
				if (node instanceof KStructProject) {
					prjDirEntryPath = node.getDirEntryPath();
				} else if (node instanceof KStructProgram) {
					prjDirEntryPath = parent.getParent().getDirEntryPath();
				}
				if (prjDirEntryPath != null) {
					KvtProject prj = getKvtProject(prjDirEntryPath);
					if (prj != null) {
						boolean all = false;
						if (node instanceof KStructProject) {
							prj.setStructProject(null);
							all = true;
						}
						if (prj.isLoaded()) {
							for (int i = 0; i < prj.getProgramCount(); i++) {
								KvtProgram p = prj.getProgram(i);
								if (all) {
									p.setStructProgram(null);
								} else if (!all && p.getDirEntry().getName().equalsIgnoreCase(node.getKey())) {
									p.setStructProgram(null);
									break;
								}
							}
						}
					} else {
						String path = node.getDirEntryPath();
						KTcDfl d = dfl;
						if (d != null) {
							String filter = d.getGlobalFilter();
							if ((filter == null) && path.startsWith(filter.toUpperCase(), 1)) {
								// kinematic filter wasn't set or project
								// belongs to the same kinematic
								// reload project list
								reloadProjectList();
							}
						}
					}
				}
			}
		}

		public void treeChanged(KStructNode parent) {
			if (m_projects.size() == 0) {
				return;
			}
			if (parent instanceof KStructProject) {
				checkProject((KStructProject) parent);
			}
			if (parent instanceof KStructScope) {
				KStructNodeVector projects = ((KStructScope) parent).projects;
				for (int i = 0; i < projects.getChildCount(); i++) {
					KStructProject p = (KStructProject) projects.getChild(i);
					treeChanged(p);
				}
			}
		}

		/**
		 * Description of the Method
		 * 
		 * @param toRemove
		 *            Description of the Parameter
		 * @param toInsert
		 *            Description of the Parameter
		 */
		public void execUnitsRemovedAdded(Vector toRemove, Vector toInsert) {
			if (0 < m_projects.size()) {
				// check toRemove-Vector
				KvtProject cachedProject = null;
				for (int i = 0; i < toRemove.size(); i++) {
					KExecUnitNode exeNode = (KExecUnitNode) toRemove.elementAt(i);
					KExecUnitProject eup = null;
					if (exeNode instanceof KExecUnitProject) {
						eup = (KExecUnitProject) exeNode;
					} else if ((exeNode instanceof KExecUnitRoutine) && (exeNode.getParent() instanceof KExecUnitProject)) {
						eup = (KExecUnitProject) exeNode.getParent();
					}
					if (eup != null) {
						if ((cachedProject == null) || (cachedProject.getStructProject() == null)
								|| !cachedProject.getStructProject().equals(eup.getKStructNode())) {
							cachedProject = getKvtProject((KStructProject) eup.getKStructNode());
						}
						if (cachedProject != null) {
							if (exeNode instanceof KExecUnitProject) {
								cachedProject.setExecUnitProject(null);
							} else {
								if (cachedProject.isLoaded() && (((KExecUnitRoutine) exeNode).getKind() == KExecUnitRoutine.UNNAMED_ROUTINE)) {
									KStructNode p = (exeNode.getKStructNode() != null) ? exeNode.getKStructNode().getParent() : null;
									if (p instanceof KStructProgram) {
										KvtProgram prog = getKvtProgram(cachedProject, (KStructProgram) p);
										if (prog != null) {
											prog.setUnnamedExecUnitRoutine(null);
										}
									}
								}
							}
						}
					}
				}
				// check insert-Vector
				cachedProject = null;
				for (int i = 0; i < toInsert.size(); i++) {
					KExecUnitNode exeNode = (KExecUnitNode) toInsert.elementAt(i);
					KExecUnitProject eup = null;
					if (exeNode instanceof KExecUnitProject) {
						eup = (KExecUnitProject) exeNode;
					} else if ((exeNode instanceof KExecUnitRoutine) && (exeNode.getParent() instanceof KExecUnitProject)) {
						eup = (KExecUnitProject) exeNode.getParent();
					}
					if (eup != null) {
						if ((cachedProject == null) || !cachedProject.getStructProject().equals(eup.getKStructNode())) {
							cachedProject = getKvtProject((KStructProject) eup.getKStructNode());
						}
						if (cachedProject != null) {
							if (exeNode instanceof KExecUnitProject) {
								cachedProject.setExecUnitProject(eup);
							} else {
								if (cachedProject.isLoaded() && (((KExecUnitRoutine) exeNode).getKind() == KExecUnitRoutine.UNNAMED_ROUTINE)) {
									KStructNode p = (exeNode.getKStructNode() != null) ? exeNode.getKStructNode().getParent() : null;
									if (p instanceof KStructProgram) {
										KvtProgram prog = getKvtProgram(cachedProject, (KStructProgram) p);
										if (prog != null) {
											prog.setUnnamedExecUnitRoutine((KExecUnitRoutine) exeNode);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		/**
		 * Description of the Method
		 */
		public void updateState() {
			for (int i = 0; i < m_projects.size(); i++) {
				KvtProject prj = (KvtProject) m_projects.elementAt(i);
				if (prj != null) {
					prj.checkProjectState(true);
					if (prj.isLoaded()) {
						for (int j = 0; j < prj.getProgramCount(); j++) {
							KvtProgram prog = prj.getProgram(j);
							prog.checkProgramState(true);
						}
					}
				}
			}
		}

		private void checkProject(KStructProject project) {
			if (project instanceof KStructRoot) {
				return;
			}
			boolean forceReload = false;
			KvtProject prj = getKvtProject(project.getDirEntryPath());
			if (prj != null) {
				prj.setStructProject(project);
				KTcDfl d = dfl;
				if (d != null) {
					KExecUnitProject execUnit = checkExecUnitProject(project, d.execution.getRoot());
					prj.setExecUnitProject(execUnit);
					if (prj.isLoaded()) {
						// check all programs
						for (int i = 0; i < project.programs.getChildCount(); i++) {
							KStructProgram p = (KStructProgram) project.programs.getChild(i);
							if (!d.client.getUserMode() || p.getDirEntryPath().endsWith(KTcDfl.USER_PROG_FILE_EXTENSION)) {
								KvtProgram prog = getKvtProgram(prj, p.getKey());
								if (prog != null) {
									prog.setStructProgram(p);
								} else if (p.isUserNode() && !p.getKey().equalsIgnoreCase(GLOBAL_VAR_PRG)) {
									forceReload = true;
								}
							}
						}
					}
				}
			} else {
				forceReload = true;
			}
			if (forceReload) {
				// reload project list
				reloadProjectList();
			}
		}

		private KvtProject getKvtProject(String dirEntryPath) {
			for (int i = 0; i < m_projects.size(); i++) {
				KvtProject prj = (KvtProject) m_projects.elementAt(i);
				if ((prj != null) && dirEntryPath.equalsIgnoreCase(prj.getDirEntry().getDirEntryPath())) {
					return prj;
				}
			}
			return null;
		}

		private KvtProgram getKvtProgram(KvtProject project, String name) {
			for (int i = 0; i < project.getProgramCount(); i++) {
				KvtProgram prog = project.getProgram(i);
				if ((prog != null) && name.equalsIgnoreCase(prog.getDirEntry().getName())) {
					return prog;
				}
			}
			return null;
		}

		private KExecUnitProject checkExecUnitProject(KStructProject project, KExecUnitScope scope) {
			for (int i = 0; i < scope.getExecUnitProjectCount(); i++) {
				KExecUnitProject ep = scope.getExecUnitProject(i);
				KStructProject sp = (KStructProject) ep.getKStructNode();
				if ((sp != null) && project.equals(sp)) {
					return ep;
				}
				if (ep instanceof KExecUnitScope) {
					KExecUnitProject result = checkExecUnitProject(project, (KExecUnitScope) ep);
					if (result != null) {
						return result;
					}
				}
			}
			return null;
		}

		private KvtProject getKvtProject(KStructProject project) {
			for (int i = 0; i < m_projects.size(); i++) {
				KvtProject prj = (KvtProject) m_projects.elementAt(i);
				if ((prj != null) && project.equals(prj.getStructProject())) {
					return prj;
				}
			}
			return null;
		}

		private KvtProgram getKvtProgram(KvtProject project, KStructProgram program) {
			for (int i = 0; i < project.getProgramCount(); i++) {
				KvtProgram prog = project.getProgram(i);
				if ((prog != null) && program.equals(prog.getStructProgram())) {
					return prog;
				}
			}
			return null;
		}
	}

	public static void reloadProjectList() {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// ignore
				}
				KTcDfl d = dfl;
				if (d != null) {
					synchronized (d.getLockObject()) {
						d.directory.refreshProjects();
					}
				}
			}
		}.start();
	}
}
