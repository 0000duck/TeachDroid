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
package com.keba.kemro.teach.dfl.dir;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.util.KDflLogger;
import com.keba.kemro.teach.network.TcConnectionListener;
import com.keba.kemro.teach.network.TcDirEntry;

/**
 * Mit Hilfe dieser Klasse können Dateien / Verzeichnisse (= Programme,Bausteine
 * / Projekte) erstellt, geändert und gelöscht werden. Weiters können Listener
 * eingehängt werden die bei Veränderungen der Dateistruktur benachrichtigt
 * werden.
 * 
 * @author ede
 */
public class KDirectoryAdministrator {
	private final String	GLOBAL			= File.separator + "_global";
	private final Vector m_dirListener = new Vector();
	/**
	 * List that contains all projects of the currently selected robot including system and global
	 */
	private Vector m_projectList;
	/**
	 * List that contains all projects that exist on the CF card
	 */
	private Vector m_projectListAll;
	/**
	 * Contains the global projects of all robots
	 */
	private Vector m_globalList;
	private final Hashtable m_programs = new Hashtable();
	private KDirEntry root;
	private final Object lock = new Object();
	/**
	 * Identifies the currently selected robot. All Projects whose paths start with that string are put in the {@link KDirectoryAdministrator#m_projectList} list
	 */
	private String globalFilter;

	private KTcDfl dfl;

	protected KDirectoryAdministrator(KTcDfl dfl) {
		this.dfl = dfl;
	}

	protected void init() {
		dfl.client.addConnectionListener(new TcConnectionListener() {
			/**
			 * @see com.keba.kemro.teach.network.TcConnectionListener#connectionStateChanged(boolean)
			 */
			public void connectionStateChanged(boolean isConnected) {
				if (isConnected) {
				} else {
					synchronized (lock) {
						m_projectList = null;
						m_globalList = null;
						m_programs.clear();
					}
					fireDirectoryProjectsChanged();
				}
			}
		});
		refreshProjects();
	}

	/**
	 * Fügt einen Listener hinzu der bei Änderungen der Verzeichnisstruktur
	 * benachrichtigt wird.
	 * 
	 * @param newListener
	 *            Der zu benachrichtigende Listener
	 */
	public void addDirectoryAdminListener(KDirectoryAdministratorListener newListener) {
		m_dirListener.addElement(newListener);
	}

	/**
	 * Entfernt den Listener
	 * 
	 * @param inListener
	 *            Der Listener der entfernt werden soll.
	 */
	public void removeDirectoryAdminListener(KDirectoryAdministratorListener inListener) {
		m_dirListener.removeElement(inListener);
	}

	/**
	 * Diese Methode benachrichtigt alle Listener dass sich die
	 * Verzeichnisstruktur verändert hat.
	 */
	private void fireDirectoryProjectsChanged() {
		for (int i = 0; i < m_dirListener.size(); i++) {
			((KDirectoryAdministratorListener) m_dirListener.elementAt(i)).directoryProjectsChanged();
		}
	}

	/**
	 * Liefert den KDirEntry für das Wurzelverzeichnis unter dem die Projekte
	 * liegen.
	 * 
	 * @return Der KDirEntry des Wurzelverzeichnisses
	 */
	public KDirEntry getRoot() {
		if (root == null) {
			root = new KDirEntry(dfl.client.directory.getRoot());
		}
		return root;
	}

	public String getLibPath() {
		return dfl.client.directory.getLibPath();
	}

	/**
	 * Liefert einen Vector der alle Projekte enthält.
	 * 
	 * @return Vector mit allen Projekten
	 */
	public Vector getAllProjects() {
		synchronized (lock) {
			if (m_projectList == null) {
				loadAllProjectEntries();
			}
		}
		return m_projectList;
	}

	/**
	 * @return all global projects
	 */
	public Vector getAllGlobalProjects() {
		synchronized (lock) {
			if (m_projectList == null) {
				loadAllProjectEntries();
			}
		}
		return m_globalList;

	}

	/**
	 * Liefert alle Programme (KDirEntry) die sich in dem übergebenen Projekt
	 * befinden
	 * 
	 * @param project
	 *            Das Projekt dessen Programme gewünscht werden
	 * 
	 * @return Vector mit allen Programmen (KDirEntry) des Projektes
	 */
	public Vector getAllPrograms(KDirEntry project) {
		Enumeration e;
		Vector prgs;
		if (project != null) {
			TcDirEntry tcProject = project.getTcDirEntry();
			synchronized (lock) {
				prgs = (Vector) m_programs.get(tcProject);
				if (prgs == null) {
					e = dfl.client.directory.getEntries(tcProject, TcDirEntry.FILTER_PROGRAM_AND_USER_PROGRAM);
					prgs = new Vector();
					while (e.hasMoreElements()) {
						TcDirEntry entry;

						entry = (TcDirEntry) e.nextElement();
						prgs.addElement(new KDirEntry(entry));
					}
					if (prgs.size() == 0) {
						KDflLogger.debug(KDirectoryAdministrator.class, "getAllPrograms: no programms returned");
					}
					m_programs.put(tcProject, prgs);
				}
				return prgs;
			}
		}
		return null;
	}

	private void loadAllProjectEntries() {
		m_projectListAll = new Vector();
		m_projectList = new Vector();
		m_globalList = new Vector();
		Enumeration e = dfl.client.directory.getEntries(getRoot().getTcDirEntry(), TcDirEntry.PROJECT);
		while (e.hasMoreElements()) {
			TcDirEntry entry = (TcDirEntry) e.nextElement();
			String path = entry.getDirEntryPath();
			if ((globalFilter == null) || path.toLowerCase().startsWith(globalFilter.toLowerCase() + File.separatorChar) || entry.isSystem() || (GLOBAL.equals(globalFilter) && (path.indexOf(File.separatorChar, 1) == -1))) {
				m_projectList.addElement(new KDirEntry(entry));
			}
			if (globalFilter != null) {// || path.startsWith(globalFilter +
				// File.separatorChar) ||
				// entry.isSystem() ||
				// (GLOBAL.equals(globalFilter) &&
				// (path.indexOf(File.separatorChar, 1)
				// == -1))) {
				m_projectListAll.addElement(new KDirEntry(entry));
			}
			if (entry.isGlobal()) {
				m_globalList.addElement(new KDirEntry(entry));
			}
		}
		if (m_projectList.size() == 0) {
			KDflLogger.debug(KDirectoryAdministrator.class, "loadAllProjectEntries: no projects returned");
		}
		if (m_globalList.size() == 0) {
			KDflLogger.debug(KDirectoryAdministrator.class, "loadAllProjectEntries: no global projects returned");
		}
	}

	/**
	 * Erzwingen eines Updates der Projektstruktur
	 */
	public void refreshProjects() {
		synchronized (lock) {
			m_globalList = null;
			m_projectList = null;
			m_projectListAll = null;
			m_programs.clear();
			loadAllProjectEntries();
		}
		fireDirectoryProjectsChanged();
	}

	/**
	 * Sets the filter for multi kinematic,
	 * 
	 * @param filter
	 *            kinematic directory
	 */
	public void setGlobalFilter(String filter) {
		synchronized (lock) {
			if (filter != null) {
				if (filter.startsWith(File.separator)) {
					globalFilter = filter;// .toUpperCase();
				} else {
					globalFilter = File.separator + filter;// .toUpperCase();
				}
			} else {
				globalFilter = null;
			}
		}
		refreshProjects();
	}

	/**
	 * Liefert den KDirEntry des Projekts, Bausteines oder Programmes mit dem
	 * gewünschten Namen vorausgesetzt dass dieser sich unter dem Parent Knoten
	 * befindet.
	 * 
	 * @param parent
	 *            Übergeordneter Knoten
	 * @param name
	 *            Name des geünschten Knotens
	 * @param kind
	 *            Typ des gewünschten Knotens
	 * 
	 * @return KDirEntry des gewünschten Knotens sonst null
	 */
	private KDirEntry getDirEntry(KDirEntry parent, String name, int kind, boolean includeAllElements) {
		TcDirEntry tcParent = parent.getTcDirEntry();
		TcDirEntry result = null;
		TcDirEntry help = null;
		boolean userMode = includeAllElements && dfl.client.getUserMode();
		if (userMode) {
			dfl.client.setUserMode(!userMode);
		}
		try {
			Enumeration e = dfl.client.directory.getEntries(tcParent, kind);
			while (e.hasMoreElements()) {
				help = (TcDirEntry) e.nextElement();
				if ((help.getName().equalsIgnoreCase(name))) {
					result = help;
				}
			}
		} catch (Exception ex) {
			// nothing to do ... just to reset the usermode
		}
		if (userMode) {
			dfl.client.setUserMode(userMode);
		}
		if (result != null) {
			return new KDirEntry(result);
		}
		return null;
	}

	private Vector getAttributeEntries(KDirEntry parent, String name, boolean includeAllElements) {

		name = name.toLowerCase();
		Vector result = null;
		boolean userMode = includeAllElements && dfl.client.getUserMode();
		if (userMode) {
			dfl.client.setUserMode(!userMode);
		}
		try {
			TcDirEntry tcParent = parent.getTcDirEntry();
			Enumeration e = dfl.client.directory.getEntries(tcParent, TcDirEntry.UNKOWN);
			TcDirEntry help = null;

			while (e.hasMoreElements()) {
				help = (TcDirEntry) e.nextElement();
				String n = help.getDirEntryPath().toLowerCase();
				int index = getLastIndexOfPathDelimitor(n);
				if (index > 0) {
					n = (n.substring(index + 1)).toLowerCase();
					if (n.startsWith(name) && ((n.equalsIgnoreCase(name + ".properties")) || (n.equalsIgnoreCase(name + ".gif")) || (n.equalsIgnoreCase(name + ".png")) || (n.endsWith(".properties") && (n.length() == name.length() + 14)))) {
						if (result == null) {
							result = new Vector();
						}
						result.addElement(help);

					}
				}
			}
		} catch (Exception exp) {
			// nothing to do ... just to reset the usermode
		}
		if (userMode) {
			dfl.client.setUserMode(userMode);
		}
		return result;
	}

	private int getLastIndexOfPathDelimitor(String str) {
		int index = -1;
		if ((str != null) && (str.length() > 0)) {
			index = str.lastIndexOf('\\');
			if (index < 0) {
				index = str.lastIndexOf('/');
			}
		}
		return index;
	}

	private KDirEntry getProject(KDirEntry program, boolean includeOtherKin) {
		String path = program.getDirEntryPath();
		int end = path.toLowerCase().indexOf(".tt");
		if (0 < end) {
			path = path.substring(0, end + 3);
			synchronized (lock) {
				if (m_projectList == null) {
					loadAllProjectEntries();
				}
				if (includeOtherKin) {
					for (int i = 0; i < m_projectListAll.size(); i++) {
						KDirEntry proj = (KDirEntry) m_projectListAll.elementAt(i);
						if (proj.getDirEntryPath().equals(path)) {
							return proj;
						}
					}
				} else {
					for (int i = 0; i < m_projectList.size(); i++) {
						KDirEntry proj = (KDirEntry) m_projectList.elementAt(i);
						if (proj.getDirEntryPath().equals(path)) {
							return proj;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Liefert den KDirEntry des Projektes mit dem angegebenen Namen
	 * 
	 * @param name
	 *            Name des Projektes
	 * 
	 * @return Den KDirEntry des Projektes falls vorhanden
	 */
	public KDirEntry getProject(String name) {
		synchronized (lock) {
			if (m_projectList == null) {
				loadAllProjectEntries();
			}
			for (int i = 0; i < m_projectList.size(); i++) {
				KDirEntry proj = (KDirEntry) m_projectList.elementAt(i);
				if (proj.getName().equalsIgnoreCase(name)) {
					return proj;
				}
			}
		}
		return null;
	}

	private KDirEntry getProject(TcDirEntry tcProject) {
		synchronized (lock) {
			if (m_projectList == null) {
				loadAllProjectEntries();
			}
			for (int i = 0; i < m_projectList.size(); i++) {
				KDirEntry proj = (KDirEntry) m_projectList.elementAt(i);
				if (proj.getTcDirEntry().equals(tcProject)) {
					return proj;
				}
			}
		}
		return null;
	}

	/**
	 * Creates a new project.
	 * 
	 * @param name
	 *            Project name
	 */
	public KDirEntry createProject(String name) {
		String directroy = ((globalFilter != null) && !GLOBAL.equals(globalFilter)) ? globalFilter : File.separator;
		TcDirEntry newDirEntry = dfl.client.directory.add(directroy, name, TcDirEntry.PROJECT);
		if (newDirEntry != null) {
			refreshProjects();
			return getProject(newDirEntry);
		}
		return null;
	}

	/**
	 * Creates a new program.
	 * 
	 * @param project
	 *            Project
	 * @param name
	 *            Program name
	 * 
	 * @return True wenn das Anlegen erfolgreich war.
	 */
	public KDirEntry createProgram(KDirEntry project, String name) {
		TcDirEntry newDirEntry = dfl.client.directory.add(project.getDirEntryPath(), name, TcDirEntry.PROGRAM);
		if (newDirEntry != null) {
			KDirEntry prog = new KDirEntry(newDirEntry);
			synchronized (lock) {
				TcDirEntry tcProject = project.getTcDirEntry();
				Vector prgs = (Vector) m_programs.get(tcProject);
				if (prgs != null) {
					prgs.addElement(prog);
				}
			}
			return prog;
		}
		return null;
	}

	/**
	 * Copies the given project.
	 * 
	 * @param srcProject
	 *            project to copy
	 * @param newName
	 *            name of the copy
	 * 
	 * @return new project
	 */
	public KDirEntry copyProject(KDirEntry srcProject, String newName) {
		String directroy = ((globalFilter != null) && !GLOBAL.equals(globalFilter)) ? globalFilter : File.separator;

		TcDirEntry newDirEntry = dfl.client.directory.copy(srcProject.getTcDirEntry(), directroy, newName);
		if (newDirEntry != null) {
			refreshProjects();
			return getProject(newDirEntry);
		}
		return null;

	}

	/**
	 * Copies the program and all its files which belongs to the program.
	 * 
	 * @param srcProgram
	 *            program to copy
	 * @param destProject
	 *            destination project
	 * @param newName
	 *            name of the new program
	 * 
	 * @return new program
	 */

	public KDirEntry copyProgram(KDirEntry srcProgram, KDirEntry destProject, String newName) {
		return copyProgram(srcProgram, destProject.getDirEntryPath(), destProject, newName);
	}

	private KDirEntry copyProgram(KDirEntry srcProgram, String destProjectPath, KDirEntry destProject, String newName) {
		TcDirEntry newDirEntry = dfl.client.directory.copy(srcProgram.getTcDirEntry(), destProjectPath, newName);
		if (newDirEntry != null) {
			// copy also ttd or tiv files

			KDirEntry project = getProject(srcProgram, true);
			int kind = srcProgram.getKind() == KDirEntry.PROGRAM ? TcDirEntry.DATA : TcDirEntry.USER_DATA;
			KDirEntry entry = getDirEntry(project, srcProgram.getName(), kind, true);
			int a = 0;
			if (entry != null) {
				dfl.client.directory.copy(entry.getTcDirEntry(), destProjectPath, newName);
			}
			String seachName = srcProgram.getName();
			Vector v = getAttributeEntries(project, seachName, true);
			if (v != null) {
				for (int i = 0; i < v.size(); i++) {
					TcDirEntry e = (TcDirEntry) v.elementAt(i);
					String n = e.getDirEntryPath();
					int index = getLastIndexOfPathDelimitor(n);
					if (index > 0) {
						n = n.substring(index + 1).toLowerCase();
						String name = newName + n.substring(seachName.length());
						dfl.client.directory.copy(e, destProjectPath, name.toLowerCase());
					}
				}
			}
			KDirEntry prog = new KDirEntry(newDirEntry);
			synchronized (lock) {
				if (destProject != null) {
					TcDirEntry tcProject = destProject.getTcDirEntry();
					Vector prgs = (Vector) m_programs.get(tcProject);
					if (prgs != null) {
						prgs.addElement(prog);
					}
				}
			}
			return prog;
		}
		return null;
	}
		
	
	/**
	 * Deletes the project.
	 * 
	 * @param project
	 *            project to delete
	 * @return true if the project has been successfully deleted
	 */
	public boolean deleteProject(KDirEntry project) {
		if (dfl.client.directory.delete(project.getTcDirEntry())) {
			refreshProjects();
			return true;
		}
		return false;
	}

	

	/**
	 * Deletes the program.
	 * 
	 * @param program
	 *            program to delete
	 * 
	 * @return true if the program has been successfully deleted
	 */
	public boolean deleteProgram(KDirEntry program) {
		KDirEntry project = getProject(program, false);
		String name = program.getName();
		if (dfl.client.directory.delete(program.getTcDirEntry())) {
			// delete also ttd or tiv files
			int kind = program.getKind() == KDirEntry.PROGRAM ? TcDirEntry.DATA : TcDirEntry.USER_DATA;
			KDirEntry entry = getDirEntry(project, name, kind, false);
			if (entry != null) {
				dfl.client.directory.delete(entry.getTcDirEntry());
			}
			Vector v = getAttributeEntries(project, name, false);
			if (v != null) {
				for (int i = 0; i < v.size(); i++) {
					dfl.client.directory.delete((TcDirEntry) v.elementAt(i));
				}
			}
			synchronized (lock) {
				TcDirEntry tcProject = project.getTcDirEntry();
				Vector prgs = (Vector) m_programs.get(tcProject);
				if (prgs != null) {
					prgs.removeElement(program);
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Renames the project.
	 * 
	 * @param project
	 *            project to rename.
	 * @param newName
	 *            new name
	 * @return true if the project has been successfully renamed
	 */
	public KDirEntry renameProject(KDirEntry project, String newName) {
		TcDirEntry newDirEntry = dfl.client.directory.rename(project.getTcDirEntry(), newName);
		if (newDirEntry != null) {
			refreshProjects();
			return getProject(newDirEntry);
		}
		return null;
	}

	/**
	 * Renames the program.
	 * 
	 * @param program
	 *            program to rename
	 * @param newName
	 *            new name
	 * @return true if the program has been successfully renamed
	 */
	public KDirEntry renameProgram(KDirEntry program, String newName) {
		TcDirEntry newDirEntry = dfl.client.directory.rename(program.getTcDirEntry(), newName);
		if (newDirEntry != null) {
			// rename also ttd or tiv files
			KDirEntry project = getProject(program, false);
			int kind = program.getKind() == KDirEntry.PROGRAM ? TcDirEntry.DATA : TcDirEntry.USER_DATA;
			KDirEntry entry = getDirEntry(project, program.getName(), kind, false);
			if (entry != null) {
				dfl.client.directory.rename(entry.getTcDirEntry(), newName);
			}
			String seachName = program.getName();
			Vector v = getAttributeEntries(project, seachName, false);
			if (v != null) {
				for (int i = 0; i < v.size(); i++) {
					TcDirEntry e = (TcDirEntry) v.elementAt(i);
					String n = e.getDirEntryPath();
					int index = getLastIndexOfPathDelimitor(n);
					if (index > 0) {
						n = n.substring(index + 1).toLowerCase();
						String name = newName + n.substring(seachName.length());
						dfl.client.directory.rename(e, name);
					}
				}
			}
			KDirEntry prog = new KDirEntry(newDirEntry);
			synchronized (lock) {
				TcDirEntry tcProject = project.getTcDirEntry();
				Vector prgs = (Vector) m_programs.get(tcProject);
				if (prgs != null) {
					prgs.removeElement(program);
					prgs.addElement(prog);
				}
			}
			return prog;
		}
		return null;
	}
}
