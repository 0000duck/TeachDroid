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
package com.keba.kemro.teach.dfl.structural;

import java.util.*;

//import com.keba.kemro.kvs.teach.framework.util.KvtLogger;
import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.dir.*;
import com.keba.kemro.teach.dfl.edit.*;
import com.keba.kemro.teach.dfl.structural.constant.*;
import com.keba.kemro.teach.dfl.structural.routine.*;
import com.keba.kemro.teach.dfl.structural.type.*;
import com.keba.kemro.teach.dfl.structural.var.*;
import com.keba.kemro.teach.dfl.util.*;
import com.keba.kemro.teach.network.*;

/**
 * Der Strukturadministrator verwaltet den gesamten Strukturbaum (Sammlung von
 * Strukturknoten), der den Deklarationsteil von Teachtalk-Programmen darstellt.
 * Er stellt eine Listener-Schnittstelle zur Verfügung, die alle registrierten
 * Klienten (Listener) über Veränderungen informiert.
 */
public class KStructAdministrator {
	/** Name des Wurzelknotens */
	public final String TEACHTALK_ROOT_NAME = "TeachTalk";

	/** Anzahl der Konstanten */
	public int constCounter;
	/** Anzahl der Programme */
	public int programCounter;
	/** Anzahl der Projekte */
	public int projectCounter;
	/** Anzahl der Routinen */
	public int routineCounter;
	/** Anzahl der Typen */
	public int typeCounter;
	/** Anzahl der Variablen */
	public int varCounter;

	private long startLoad;

	private final Vector m_modelListenerList = new Vector(50);
	private final Vector m_kinematikListenerList = new Vector(3);
	private KStructRoot root;
	private final int SLEEP_TIME = 1500;
	private final int LOW_PRIORITY = Thread.NORM_PRIORITY - 3;
	private final int HIGH_PRIORITY = Thread.NORM_PRIORITY;
	protected PollThread pollThread;

	private String globalFilter;
	private boolean filterChanged;
	private int startChangeCounter;

	public KConstantFactory constFactory;
	public KRoutineFactory routineFactory;
	public KTypeFactory typeFactory;
	public KVariableFactory variableFactory;

	KTcDfl dfl;

	protected KStructAdministrator(KTcDfl dfl) {
		this.dfl = dfl;
	}

	protected void init() {
		constFactory = new KConstantFactory();
		routineFactory = new KRoutineFactory();
		typeFactory = new KTypeFactory();
		variableFactory = new KVariableFactory();

		root = new KStructRoot(TEACHTALK_ROOT_NAME, dfl);
		root.setLoaded(true);
		startLoad();
		loadRoot();
		endLoad(root.getKey());
		fireTreeChanged(root);
		pollThread = new PollThread();
		pollThread.setPriority(LOW_PRIORITY);
		pollThread.start();

		dfl.client.addConnectionListener(new TcConnectionListener() {
			public void connectionStateChanged(boolean isConnected) {
				synchronized (KStructAdministrator.this.dfl.getLockObject()) {
					if (isConnected) {
					} else {
						pollThread.stopPoll();
						// reset tree
						unloadRoot();
						fireTreeChanged(root);
					}
				}
			}
		});
	}

	protected void stop() {
		pollThread.stopPoll();
	}

	/**
	 * Inserts a new program in the structural tree and creates the program
	 * file.
	 * 
	 * @param project
	 *            project in which the program should be inserted
	 * @param name
	 *            name of the program
	 * @return the program node
	 */
	public KStructProgram addProgramInc(KStructProject project, String name) {
		synchronized (dfl.getLockObject()) {
			TcStructuralNode p = dfl.client.structure.addProgram(project.getTcStructuralNode(), name);
			if (p != null) {
				// added successfully
				String key = p.getName();
				KStructProgram program = new KStructProgram(key, dfl);
				// program.setLoaded(true);
				program.setTcStructuralNode(p);
				project.programs.addChild(program);
				TcStructuralNode dirEntryNode = p.getDeclarationNode();
				project.programsAndUnits.put(dirEntryNode, program);
				dfl.directory.refreshProjects();
				fireNodeInserted(project, program);
				return program;
			}
			return null;
		}
	}

	/**
	 * Adds a new routine incrementally to the given program.
	 * 
	 * @param program
	 *            program which should contain the routine declaration
	 * @param name
	 *            routine name
	 * @param returnType
	 *            routine type
	 * @param kind
	 *            routine kind (AT_ROUTINE, NAMED_ROUTINE, UNNAMED_ROUTINE)
	 * @param visibility
	 *            visibility (GLOBAL, PUBLIC, PRIVATE)
	 * @param eventVariable
	 *            event variable
	 * @return returns the new KStructRoutine
	 */
	public KStructRoutine addRoutineInc(KStructProgram program, String name, KStructType returnType, Integer kind, int visibility, KStructVar eventVariable) {
		synchronized (dfl.getLockObject()) {
			TcStructuralVarNode eventVar = null;
			if (eventVariable != null) {
				eventVar = (TcStructuralVarNode) eventVariable.getTcStructuralNode();
			}
			KStructNode parent = (visibility == KStructNode.GLOBAL) ? program.getParent() : program;
			TcStructuralTypeNode rt = (TcStructuralTypeNode) ((returnType != null) ? returnType.getTcStructuralNode() : null);
			TcStructuralRoutineNode r = dfl.client.structure.addRoutine(program.getTcStructuralNode(), program.getTcStructuralNode(), name, kind.byteValue(), rt, eventVar, visibility == KStructNode.PRIVATE);
			if (r != null) {
				// added successfully
				KStructRoutine routine = dfl.structure.routineFactory.addNode(parent, r);
				fireNodeInserted(program, routine);
				dfl.editor.reloadKEditor(program);
				return routine;
			}
			return null;
		}
	}

	/**
	 * Removes the routine incrementally
	 * 
	 * @param routine
	 *            routine to remove
	 * 
	 * @return true if the routine has been removed successfully
	 */
	public boolean removeRoutineInc(KStructRoutine routine) {
		synchronized (dfl.getLockObject()) {
			KStructNode parent = routine.getParent();
			TcStructuralNode tn = routine.getTcStructuralNode();
			if (dfl.client.structure.removeNode(tn)) {
				// removed successfully
				if (parent instanceof KStructProject) {
					((KStructProject) parent).routines.removeChild(routine);
				} else if (parent instanceof KStructProgram) {
					((KStructProgram) parent).routines.removeChild(routine);
				} else if (parent instanceof KStructTypeUnit) {
					((KStructTypeUnit) parent).routines.removeChild(routine);
				} else {
					KDflLogger.error(KRoutineFactory.class, "removeRoutineInc: " + parent.getKey());
				}
				routine.getKStructProject().allRoutines.remove(tn);
				fireNodeRemoved(parent, routine);
				TcStructuralNode dirEntry = tn.getDeclarationNode();
				KStructNode n = (parent instanceof KStructProgram) ? parent : getKStructNode(dirEntry);
				if (n instanceof KStructProgram) {
					dfl.editor.reloadKEditor((KStructProgram) n);
				}
				return true;
			}
			return false;
		}
	}

	/**
	 * Inserts a new variable into the structural tree and writes the new
	 * variable declaration into file of the given program. After the
	 * successfully insertion it will be notified and the editor of the program
	 * will be reloaded.
	 * 
	 * @param dirEntry
	 *            program in which the variable declaration will be inserted
	 * @param parent
	 *            scope of the variable
	 * @param name
	 *            name of the variable
	 * @param type
	 *            type of the variable
	 * @param isConst
	 *            true if the variable should be a constant variable
	 * @param isSave
	 *            true if the variable should be a save variable
	 * @param isDynamic
	 *            true if the variable should not be written into a source file
	 * @param visibility
	 *            visibility
	 * 
	 * @return the new variable
	 */
	public KStructVar addVariableInc(KStructProgram dirEntry, KStructNode parent, String name, KStructType type, boolean isConst, boolean isSave, int visibility, boolean isDynamic) {
		synchronized (dfl.getLockObject()) {
			TcStructuralVarNode v = dfl.client.structure.addVariable((dirEntry != null) ? dirEntry.getTcStructuralNode() : null, parent.getTcStructuralNode(), name, isConst ? TcStructuralVarNode.CONST_VAR_KIND : TcStructuralVarNode.VAR_KIND,
					(TcStructuralTypeNode) type.getTcStructuralNode(), false, isSave, visibility == KStructNode.PRIVATE, isDynamic);
			if (v != null) {
				// added successfully
				KStructVar variable = variableFactory.addNode(parent, v);
				fireNodeInserted(parent, variable);
				if (dirEntry != null) {
					dfl.editor.reloadKEditor(dirEntry);
				}
				return variable;
			}
			return null;
		}
	}

	/**
	 * Die Methode entfernt zur Laufzeit eine Variable und aktualisiert den
	 * Strukturbaum.
	 * 
	 * @param variable
	 *            Variable die zu entfernen ist.
	 * 
	 * @return Wahrheitswert
	 */
	public boolean removeVariableInc(KStructVar variable) {
		synchronized (dfl.getLockObject()) {
			TcStructuralVarNode v = (TcStructuralVarNode) variable.getTcStructuralNode();
			if (dfl.client.structure.removeNode(v)) {
				KStructNode parent = variable.getParent();
				variableFactory.removeNode(variable);
				fireNodeRemoved(parent, variable);
				TcStructuralNode dirEntry = v.getDeclarationNode();
				KStructNode n = (parent instanceof KStructProgram) ? parent : getKStructNode(dirEntry);
				if (n instanceof KStructProgram) {
					if (variable.isSave()) {
						// write back save values;
						TcStructuralNode prog = ((KStructProgram) n).getTcStructuralNode();
						dfl.client.structure.writeBackSaveValues(prog);
					}
					dfl.editor.reloadKEditor((KStructProgram) n);
				}
				return true;
			}
			return false;
		}
	}

	public KStructVar moveVariableInc(KStructVar variable, KStructNode dest) {
		synchronized (dfl.getLockObject()) {
			KStructNode parent = variable.getParent();
			TcStructuralNode dirEntry = variable.getTcStructuralNode().getDeclarationNode();
			KStructNode n = (parent instanceof KStructProgram) ? parent : getKStructNode(dirEntry);
			TcStructuralVarNode v = dfl.client.structure.moveVariable((TcStructuralVarNode) variable.getTcStructuralNode(), dest.getTcStructuralNode());
			if ((n instanceof KStructProgram) && (v != null)) {
				if (variable.isSave()) {
					// write back save values;
					TcStructuralNode prog = ((KStructProgram) n).getTcStructuralNode();
					dfl.client.structure.writeBackSaveValues(prog);
				}
				// remove
				variableFactory.removeNode(variable);
				fireNodeRemoved(parent, variable);
				// add
				variable = variableFactory.addNode(dest, v);
				fireNodeInserted(dest, variable);

				dfl.editor.reloadKEditor((KStructProgram) n);
				n = (parent instanceof KStructProgram) ? parent : getKStructNode(v.getDeclarationNode());
				dfl.editor.reloadKEditor((KStructProgram) n);
				return variable;
			}
			return null;
		}
	}

	/**
	 * Renames the variable.
	 * 
	 * @param variable
	 *            variable to rename
	 * @param name
	 *            new name
	 * @return returns the new variable object
	 */
	public KStructVar renameVariableInc(KStructVar variable, String name) {
		synchronized (dfl.getLockObject()) {
			KStructNode parent = variable.getParent();
			TcStructuralNode dirEntry = variable.getTcStructuralNode().getDeclarationNode();
			KStructNode n = (parent instanceof KStructProgram) ? parent : getKStructNode(dirEntry);
			TcStructuralVarNode v = dfl.client.structure.renameVariable((TcStructuralVarNode) variable.getTcStructuralNode(), name);
			if ((n instanceof KStructProgram) && (v != null)) {
				if (variable.isSave()) {
					// write back save values;
					TcStructuralNode prog = ((KStructProgram) n).getTcStructuralNode();
					dfl.client.structure.writeBackSaveValues(prog);
				}
				// remove
				variableFactory.removeNode(variable);
				fireNodeRemoved(parent, variable);
				// add
				variable = variableFactory.addNode(parent, v);
				fireNodeInserted(parent, variable);

				dfl.editor.reloadKEditor((KStructProgram) n);
				return variable;
			}
			return null;
		}
	}

	/**
	 * Liefert die Zeichenkette eines Strukturknotenpfades. path =
	 * [KStructProject] + [KStructProgram] + KStructVar + {+ KStructVar (type
	 * component) | + Integer (array index)} KStructProgram is omitted for
	 * global variables.
	 * 
	 * @param path
	 *            Strukturknotenpfad
	 * 
	 * @return vollständiger Pfad als Zeichenkette
	 */
	public String convertPath(Object[] path) {
		if (path == null) {
			return null;
		}
		String p = "";
		boolean component = false;
		String point = "";
		for (int i = 0; i < path.length; i++) {
			Object n = path[i];
			if (n == null) {
				return null;
			}
			if ((n instanceof KStructProject) || (n instanceof KStructProgram) || (!component && (n instanceof KStructTypeUnit))) {
				p += (point + ((KStructNode) n).getKey());
			} else {
				component = true;
				if (n instanceof KStructNode) {
					p += (point + ((KStructNode) n).getKey());
				} else if (n instanceof Integer) {
					p += KEditKW.KW_BRACKET_LEFT + n.toString() + KEditKW.KW_BRACKET_RIGHT;
				} else {
					return null;
				}
			}
			point = KEditKW.KW_POINT;
		}
		return p;
	}

	/**
	 * Returns the path in the structural tree variableComponentPath =
	 * KStructVar + {+ KStructVar (type component) | + Integer (array index)}
	 * 
	 * @param variableComponentPath
	 *            variable component path
	 * @return fullPath = [project + '.' ] + [program + '.' ] + variable + {'.'
	 *         + type component | '[ + array index + ']'} return path = variable
	 *         + {component | array index}. program is omitted for global
	 *         variables.
	 */
	public String getFullPath(Object[] variableComponentPath) {
		String fullPath = "";
		if ((variableComponentPath != null) && (0 < variableComponentPath.length)) {
			KStructVar var = (KStructVar) variableComponentPath[0];
			KStructNode parent = var.getParent();
			while (!(parent instanceof KStructRoot)) {
				/*
				 * if ((parent instanceof KStructProject) || (parent instanceof
				 * KStructProgram)) { fullPath = parent.getKey() + "." +
				 * fullPath; } else
				 */{
					fullPath = parent.getKey() + "." + fullPath;
				}
				parent = parent.getParent();
			}
			fullPath += convertPath(variableComponentPath);
		}
		return fullPath;
	}

	/**
	 * Returns the variable instance path. The instance path starts with a
	 * global, public or private variable followed by component variable or
	 * array index.
	 * 
	 * fullPath = [project + '.' ] + [program + '.' ] + [routine + '.' ] +
	 * variable + {'.' + type component | '[ + array index + ']'} return path =
	 * variable + {component | array index}. program is omitted for global
	 * variables.
	 * 
	 * @param fullPath
	 *            string representation of the instance component in the
	 *            structural tree
	 * 
	 * @return root variable + {component variable | index}
	 */
	public Object[] getVariableComponentPath(String fullPath) {
		KStructNode sn = getRoot();
		Vector componentPathVector = new Vector();
		int beg = 0;
		int end = -1;
		while ((sn != null) && ((sn instanceof KStructProject) || (sn instanceof KStructProgram) || (sn instanceof KStructRoutine)) && ((end + 1) < fullPath.length())) {
			beg = end + 1;
			int end0 = fullPath.indexOf(KEditKW.KW_BRACKET_LEFT, beg);
			int end1 = fullPath.indexOf(KEditKW.KW_POINT, beg);
			if ((beg < end0) && ((end0 < end1) || (end1 == -1))) {
				end = end0;
			} else if ((beg < end1) && ((end1 < end0) || (end0 == -1))) {
				end = end1;
			} else {
				end = fullPath.length();
			}
			String name = fullPath.substring(beg, end);
			sn = getChild(sn, name);
		}
		if (sn instanceof KStructVar) {
			componentPathVector.addElement(sn);
		}
		if ((sn != null) && ((end + 1) < fullPath.length())) {
			// variable found
			while ((sn != null) && ((end + 1) < fullPath.length())) {
				String name = null;
				beg = end + 1;
				if ((sn instanceof KStructVarArray) || (sn instanceof KStructTypeArray)) {
					end = fullPath.indexOf(KEditKW.KW_BRACKET_RIGHT, beg);
					if (end == -1) {
						end = fullPath.length();
					}
					name = fullPath.substring(beg, end);
					end++; // skip '.' or '['
					// check array index
					KStructType t = null;
					if (sn instanceof KStructVar) {
						t = skipType(((KStructVar) sn).getKStructType());
					} else {
						t = (KStructTypeArray) sn;
					}
					sn = null;
					if ((t != null) && (t instanceof KStructTypeArray)) {
						int lowerBound = ((KStructTypeArray) t).getLowerBound().intValue();
						int upperBound = ((KStructTypeArray) t).getUpperBound().intValue();
						int index;
						try {
							index = Integer.parseInt(name);
						} catch (NumberFormatException nf) {
							KDflLogger.debug(KStructAdministrator.class, "parseInt failed for " + name);
							return null;
						}
						if ((lowerBound <= index) && (index <= upperBound)) {
							componentPathVector.addElement(new Integer(index));
							sn = skipType(((KStructTypeArray) t).getArrayElementKStructType());
						}
					}
				} else {
					int end0 = fullPath.indexOf(KEditKW.KW_BRACKET_LEFT, beg);
					int end1 = fullPath.indexOf(KEditKW.KW_POINT, beg);
					if ((beg < end0) && ((end0 < end1) || (end1 == -1))) {
						end = end0;
					} else if ((beg < end1) && ((end1 < end0) || (end0 == -1))) {
						end = end1;
					} else {
						end = fullPath.length();
					}
					name = fullPath.substring(beg, end);
					if (sn instanceof KStructRoutine) {
						KStructNode vn = ((KStructRoutine) sn).variables.getChild(name, false);
						if (vn == null) {
							vn = ((KStructRoutine) sn).parameters.getChild(name, false);
						}
						sn = vn;
					} else {
						KStructType t = null;
						if (sn instanceof KStructVar) {
							t = skipType(((KStructVar) sn).getKStructType());
						} else if (sn instanceof KStructType) {
							t = (KStructType) sn;
						}
						if (t instanceof KStructTypeStruct) {
							sn = ((KStructTypeStruct) t).components.getChild(name, false);
						} else if (t instanceof KStructTypeUnit) {
							sn = getBaseUnitComponents(((KStructTypeUnit) t), name);
						} else {
							sn = null;
						}
					}
					if (sn instanceof KStructVar) {
						componentPathVector.addElement(sn);
					}
				}
			}
		}
		Object[] path = null;
		if ((sn != null) && (end >= fullPath.length()) && (0 < componentPathVector.size())) {
			path = new Object[componentPathVector.size()];
			componentPathVector.copyInto(path);
		}
		return path;
	}

	/**
	 * Returns the node which is specified by the given path.
	 * 
	 * @param fullPath
	 *            representation of the node in the structural tree
	 * @return structural tree node
	 */
	public KStructNode getKStructNode(String fullPath) {
		KStructNode sn = getRoot();
		int beg = 0;
		int end = 0;
		while ((sn != null) && (beg < fullPath.length())) {
			end = fullPath.indexOf(".", beg);
			if (end == -1) {
				end = fullPath.length();
			}
			String name = fullPath.substring(beg, end);
			beg = end + 1;
			if (sn instanceof KStructVarUnit){
				sn = ((KStructVar)sn).getKStructType();
			}
			sn = getChild(sn, name);
		}
		return sn;
	}

	private KStructType skipType(KStructType type) {
		while (((type != null) && type.isAliasType()) || (type instanceof KStructTypeMapTo)) {
			type = type.getKStructType();
		}
		return type;
	}

	private KStructNode getBaseUnitComponents(KStructTypeUnit base, String key) {
		base = (KStructTypeUnit) skipType(base);
		if (base == null) {
			return null;
		}
		KStructNode comp = base.components.getChild(key, false);
		if (comp == null) {
			comp = base.routines.getChild(key, false);
		}
		if (comp == null) {
			return getBaseUnitComponents(base.getBaseUnit(), key);
		}
		return comp;
	}

	/**
	 * Gets the child attribute of the KStructAdministrator class
	 * 
	 * @param node
	 *            Description of the parameter
	 * @param key
	 *            Description of the parameter
	 * 
	 * @return The child value
	 */
	private KStructNode getChild(KStructNode node, String key) {
		KStructNode found;

		if (node instanceof KStructScope) {
			found = ((KStructScope) node).projects.getChild(key, false);
			if (found != null) {
				return found;
			}
			if (node instanceof KStructRoot) {
				for (int i = 0; i < ((KStructScope) node).projects.getChildCount(); i++) {
					KStructNode n = ((KStructScope) node).projects.getChild(i);
					found = getChild(n, key);
					if (found != null) {
						return found;
					}
				}
				found = ((KStructRoot) node).types.getChild(key, false);
				if (found != null) {
					return found;
				}
				// check for builtin routines (STR, CHR; ...)
				return (getRoot()).routines.getChild(key, false);
			}
		}

		if (node instanceof KStructProject) {
			found = ((KStructProject) node).variables.getChild(key, false);
			if (found != null) {
				return found;
			}
			found = ((KStructProject) node).routines.getChild(key, false);
			if (found != null) {
				return found;
			}
			found = ((KStructProject) node).constants.getChild(key, false);
			if (found != null) {
				return found;
			}
			found = ((KStructProject) node).types.getChild(key, false);
			if (found != null) {
				return found;
			}
			found = ((KStructProject) node).units.getChild(key, true);
			if (found != null) {
				return found;
			}
			found = ((KStructProject) node).programs.getChild(key, true);
			if (found != null) {
				return found;
			}
		} else if (node instanceof KStructProgram) {
			found = ((KStructProgram) node).variables.getChild(key, false);
			if (found != null) {
				return found;
			}
			found = ((KStructProgram) node).routines.getChild(key, false);
			if (found != null) {
				return found;
			}
			found = ((KStructProgram) node).constants.getChild(key, false);
			if (found != null) {
				return found;
			}
			found = ((KStructProgram) node).types.getChild(key, false);
			if (found != null) {
				return found;
			}
		} else if (node instanceof KStructTypeUnit) {
			found = ((KStructTypeUnit) node).components.getChild(key, false);
			if (found != null) {
				return found;
			}
			found = ((KStructTypeUnit) node).routines.getChild(key, false);
			if (found != null) {
				return found;
			}
			found = ((KStructTypeUnit) node).constants.getChild(key, false);
			if (found != null) {
				return found;
			}
			found = ((KStructTypeUnit) node).types.getChild(key, false);
			if (found != null) {
				return found;
			}
		} else if (node instanceof KStructRoutine) {
			found = ((KStructRoutine) node).parameters.getChild(key, false);
			if (found != null) {
				return found;
			}
			found = ((KStructRoutine) node).variables.getChild(key, false);
			if (found != null) {
				return found;
			}
		}
		if (node instanceof KStructSystem) {
			for (int i = 0; i < ((KStructScope) node).projects.getChildCount(); i++) {
				KStructNode n = ((KStructScope) node).projects.getChild(i);
				found = getChild(n, key);
				if (found != null) {
					return found;
				}
			}
			// check for builtin routines (STR, CHR; ...)
			return (getRoot()).routines.getChild(key, false);
		}
		if (node instanceof KStructScope) {
			found = ((KStructScope) node).projects.getChild(key, true);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	/**
	 * Liefert die Struktur-Konstante für den untergeordneten
	 * TeachControll-Strukturknoten.
	 * 
	 * @param ortsConst
	 *            TeachControll-Strukturknoten
	 * 
	 * @return Struktur-Konstante
	 */
	public KStructConst getKStructConst(TcStructuralConstNode ortsConst) {
		KStructConst c;

		if (ortsConst == null) {
			return null;
		}
		c = getKStructConst(root, ortsConst);
		if (c != null) {
			return c;
		}
		// if parent hasn't loaded then load all private components and try
		// again
		TcStructuralNode p = ortsConst.getParent();
		KStructNode parent = getKStructNode(p);
		if ((parent != null) && !parent.isLoaded()) {
			parent.loadChildren();
			return getKStructConst(ortsConst);
		}
		return null;
	}

	private KStructConst getKStructConst(KStructProject parent, TcStructuralConstNode ortsConst) {
		// force load;
		parent.constants.getChildCount();
		KStructConst c = (KStructConst) parent.allConstants.get(ortsConst);
		if (c != null) {
			return c;
		}
		if (parent instanceof KStructScope) {
			for (int i = 0; i < ((KStructScope) parent).projects.getChildCount(); i++) {
				KStructProject ep;

				ep = (KStructProject) ((KStructScope) parent).projects.getChild(i);
				c = getKStructConst(ep, ortsConst);
				if (c != null) {
					return c;
				}
			}
		}
		return null;
	}

	/**
	 * Liefert den Datei-Handle(KDirEntry) eines Strukturknotens. Das Objekt
	 * KDirEntry repräsentiert die Datei eines Strukturknotens.
	 * 
	 * @param node
	 *            Strukturknoten
	 * 
	 * @return Datei-Handle
	 */
	public KDirEntry getKDirEntry(KStructNode node) {
		TcStructuralNode tsn = node.getTcStructuralNode();
		if (tsn != null) {
			TcDirEntry tcDirEntry = dfl.client.structure.getDirEntry(tsn);
			if (tcDirEntry != null) {
				return new KDirEntry(tcDirEntry);
			}
		}
		return null;
	}

	/**
	 * Liefert je nach Datei-Handle einen
	 * Programm-Strukturknoten(KStructProgram) oder einen
	 * Typ-Strukturknoten(KStructTypeUnit) zurück.
	 * 
	 * @param dirEntry
	 *            Datei-Handle
	 * 
	 * @return KStructProgram oder KStructTypeUnit
	 */
	public KStructNode getKStructNode(KDirEntry dirEntry) {
		TcStructuralNode tsn = (dirEntry != null) ? dfl.client.structure.getNode(dirEntry.getTcDirEntry()) : null;
		if (tsn != null) {
			return getKStructNode(tsn);
		}
		return null;
	}

	/**
	 * Liefert den Strukturknoten für einen untergeordneten
	 * TeachControl-Strukturknoten.
	 * 
	 * @param ortsNode
	 *            TeachControl-Strukturknoten
	 * 
	 * @return Strukturknoten
	 */
	public KStructNode getKStructNode(TcStructuralNode ortsNode) {
		KStructNode node;

		if (ortsNode instanceof TcStructuralTypeNode) {
			return getKStructType((TcStructuralTypeNode) ortsNode);
		}
		if (ortsNode instanceof TcStructuralConstNode) {
			return getKStructConst((TcStructuralConstNode) ortsNode);
		}
		if (ortsNode instanceof TcStructuralVarNode) {
			return getKStructVar((TcStructuralVarNode) ortsNode);
		}
		if (ortsNode instanceof TcStructuralRoutineNode) {
			return getKStructRoutine((TcStructuralRoutineNode) ortsNode);
		}
		node = getKStructProject(ortsNode);
		if (node != null) {
			return node;
		}
		node = getKStructProgram(ortsNode);
		if ((node == null) && (root.getTcStructuralNode() != null) && root.getTcStructuralNode().equals(ortsNode)) {
			node = root;
		}
		return node;
	}

	/**
	 * Liefert für einen untergeordneten TeachControl-Programmknoten den
	 * Strukturprogrammknoten.
	 * 
	 * @param ortsProgram
	 *            TeachControl-Programmknoten
	 * 
	 * @return Strukturprogrammknoten
	 */
	public KStructProgram getKStructProgram(TcStructuralNode ortsProgram) {
		if (ortsProgram == null) {
			return null;
		}
		KStructProgram p = getKStructProgram(root, ortsProgram);
		if (p != null) {
			return p;
		}
		// if parent hasn't loaded then load all and try again
		TcStructuralNode np = ortsProgram.getParent();
		KStructNode parent = getKStructNode(np);
		if ((parent != null) && !parent.isLoaded()) {
			parent.loadChildren();
			return getKStructProgram(ortsProgram);
		}
		return null;
	}

	private KStructProgram getKStructProgram(KStructProject parent, TcStructuralNode ortsProgram) {
		// force load
		parent.programs.getChildCount();
		KStructNode n = (KStructNode) parent.programsAndUnits.get(ortsProgram);
		if (n instanceof KStructProgram) {
			return (KStructProgram) n;
		} else if (n != null) {
			return null;
		}
		if (parent instanceof KStructScope) {
			for (int i = 0; i < ((KStructScope) parent).projects.getChildCount(); i++) {
				KStructProject ep;

				ep = (KStructProject) ((KStructScope) parent).projects.getChild(i);
				KStructProgram p = getKStructProgram(ep, ortsProgram);
				if (p != null) {
					return p;
				}
			}
		}
		return null;
	}

	/**
	 * Liefert für einen untergeordneten TeachControl-Projektknoten den
	 * Strukturprojektknoten.
	 * 
	 * @param ortsProject
	 *            TeachControl-Projektknoten
	 * 
	 * @return Strukturprojektknoten
	 */
	public KStructProject getKStructProject(TcStructuralNode ortsProject) {
		if (ortsProject == null) {
			return null;
		}
		return getKStructProject(root, ortsProject);
	}

	private KStructProject getKStructProject(KStructScope parent, TcStructuralNode ortsProject) {

		for (int i = 0; i < parent.projects.getChildCount(); i++) {
			KStructProject ep;

			ep = (KStructProject) parent.projects.getChild(i);
			if (ep.getTcStructuralNode().equals(ortsProject)) {
				return ep;
			}
			if (ep instanceof KStructScope) {
				ep = getKStructProject((KStructScope) ep, ortsProject);
				if (ep != null) {
					return ep;
				}
			}
		}

		return null;
	}

	/**
	 * Returns the project for the given name.
	 * 
	 * @param name
	 *            name of the project
	 * @return project
	 */
	public KStructProject getKStructProject(String name) {
		synchronized (dfl.getLockObject()) {
			if (name == null) {
				return null;
			}
			return getKStructProject(root, name);
		}
	}

	private KStructProject getKStructProject(KStructScope parent, String name) {
		if ((globalFilter != null) && name.equalsIgnoreCase("_global")) {
			name = globalFilter;
		}
		for (int i = 0; i < parent.projects.getChildCount(); i++) {
			KStructProject ep;

			ep = (KStructProject) parent.projects.getChild(i);
			if (name.equalsIgnoreCase(ep.getKey()) || name.equalsIgnoreCase(globalFilter+"\\"+ep.getKey())) {
				return ep;
			}
			if (ep instanceof KStructScope) {
				ep = getKStructProject((KStructScope) ep, name);
				if (ep != null) {
					return ep;
				}
			}
		}
		return null;
	}

	private KStructProject getKStructProject(KStructScope parent, String name, boolean ignoreGlobal) {
		for (int i = 0; i < parent.projects.getChildCount(); i++) {
			KStructProject ep;

			ep = (KStructProject) parent.projects.getChild(i);
			if (name.equalsIgnoreCase(ep.getKey()) && (!ignoreGlobal) || (!(ep instanceof KStructScope))) {
				return ep;
			}
			if (ep instanceof KStructScope) {
				ep = getKStructProject((KStructScope) ep, name, ignoreGlobal);
				if (ep != null) {
					return ep;
				}
			}
		}
		return null;
	}

	/**
	 * Liefert für einen untergeordneten TeachControl-Routinenknoten den
	 * Strukturroutinenknoten.
	 * 
	 * @param ortsRoutine
	 *            TeachControl-Routinenknoten
	 * 
	 * @return Strukturroutinenknoten
	 */
	public KStructRoutine getKStructRoutine(TcStructuralRoutineNode ortsRoutine) {
		if (ortsRoutine == null) {
			return null;
		}
		KStructRoutine r = getKStructRoutine(root, ortsRoutine);
		if (r != null) {
			return r;
		}
		// if parent hasn't loaded then load all private components and try
		// again
		TcStructuralNode p = ortsRoutine.getParent();
		KStructNode parent = getKStructNode(p);
		if ((parent != null) && !parent.isLoaded()) {
			parent.loadChildren();
			return getKStructRoutine(ortsRoutine);
		}
		return null;
	}

	private KStructRoutine getKStructRoutine(KStructProject parent, TcStructuralRoutineNode ortsRoutine) {
		// force load
		parent.routines.getChildCount();
		KStructRoutine r = (KStructRoutine) parent.allRoutines.get(ortsRoutine);
		if (r != null) {
			return r;
		}
		if (parent instanceof KStructScope) {
			for (int i = 0; i < ((KStructScope) parent).projects.getChildCount(); i++) {
				KStructProject ep;

				ep = (KStructProject) ((KStructScope) parent).projects.getChild(i);
				r = getKStructRoutine(ep, ortsRoutine);
				if (r != null) {
					return r;
				}
			}
		}
		return null;
	}

	/**
	 * Liefert für einen untergeordneten TeachControl-Typknoten den
	 * Strukturtypknoten.
	 * 
	 * @param ortsType
	 *            TeachControl-Typknoten
	 * 
	 * @return Strukturtypknoten
	 */
	public KStructType getKStructType(TcStructuralTypeNode ortsType) {
		if (ortsType == null) {
			return null;
		}
		KStructType t = getKStructType(root, ortsType);
		if (t != null) {
			return t;
		}
		// if parent hasn't loaded then load all private components and try
		// again
		TcStructuralNode p = ortsType.getParent();
		KStructNode parent = getKStructNode(p);
		if ((parent != null) && !parent.isLoaded()) {
			parent.loadChildren();
			return getKStructType(ortsType);
		}
		return null;
	}

	private KStructType getKStructType(KStructProject parent, TcStructuralTypeNode ortsType) {
		// force load
		parent.types.getChildCount();
		KStructType t = (KStructType) parent.allTypes.get(ortsType);
		if (t != null) {
			return t;
		}
		if (parent instanceof KStructScope) {
			for (int i = 0; i < ((KStructScope) parent).projects.getChildCount(); i++) {
				KStructProject ep;

				ep = (KStructProject) ((KStructScope) parent).projects.getChild(i);
				t = getKStructType(ep, ortsType);
				if (t != null) {
					return t;
				}
			}
		}
		return null;
	}

	/**
	 * Liefert für einen untergeordneten TeachControl-Variablenknoten den
	 * Strukturvariablenknoten.
	 * 
	 * @param ortsVar
	 *            TeachControl-Variablenknoten
	 * 
	 * @return Strukturvariablenknoten
	 */
	public KStructVar getKStructVar(TcStructuralVarNode ortsVar) {
		TcStructuralNode osn;
		KStructNode en;
		KStructVar ev;

		if (ortsVar == null) {
			return null;
		}
		osn = ortsVar.getParent();
		en = null;
		switch (osn.getKind()) {
		case TcStructuralNode.PROGRAM:
			en = getKStructProgram(osn);
			break;
		case TcStructuralNode.TYPE:
			en = getKStructType((TcStructuralTypeNode) osn);
			break;
		case TcStructuralNode.ROUTINE:
			en = getKStructRoutine((TcStructuralRoutineNode) osn);
			break;
		default:
			en = getKStructProject(osn);
			break;
		}
		ev = null;
		if (en == null) {
			return null;
		}
		if (en instanceof KStructProject) {
			ev = checkVarIn(((KStructProject) en).variables, ortsVar);
		} else if (en instanceof KStructProgram) {
			ev = checkVarIn(((KStructProgram) en).variables, ortsVar);
		} else if (en instanceof KStructTypeStruct) {
			KStructNodeVector v = ((KStructTypeStruct) en).components;
			if (v != null) {
				ev = checkVarIn(v, ortsVar);
			}
		} else if (en instanceof KStructTypeUnit) {
			KStructNodeVector v = ((KStructTypeUnit) en).components;
			if (v != null) {
				ev = checkVarIn(v, ortsVar);
			}
		} else if (en instanceof KStructRoutine) {
			ev = checkVarIn(((KStructRoutine) en).parameters, ortsVar);
			if (ev != null) {
				return ev;
			}
			ev = checkVarIn(((KStructRoutine) en).variables, ortsVar);
			if (ev != null) {
				return ev;
			}
		}
		return ev;
	}

	/**
	 * Liefert für einen untergeordneten TeachControl-Strukturknoten, der ein
	 * Datei-Handle repräsentiert, den Strukturprogrammknoten oder den
	 * Strukturtypknoten.
	 * 
	 * @param tcProgOrUnit
	 *            TeachControl-Programmknoten
	 * 
	 * @return Strukturprogrammknoten
	 */
	public KStructNode getProgramOrUnit(TcStructuralNode tcProgOrUnit) {
		return getProgramOrUnit(root, tcProgOrUnit);
	}

	public KStructNode getProgramOrUnit(KStructProject parent, TcStructuralNode tcProgOrUnit) {
		KStructNode n = (KStructNode) parent.programsAndUnits.get(tcProgOrUnit);
		if (n != null) {
			return n;
		}
		if (parent instanceof KStructScope) {
			for (int i = 0; i < ((KStructScope) parent).projects.getChildCount(); i++) {
				KStructProject ep;

				ep = (KStructProject) ((KStructScope) parent).projects.getChild(i);
				n = getProgramOrUnit(ep, tcProgOrUnit);
				if (n != null) {
					return n;
				}
			}
		}
		return null;
	}

	/**
	 * Liefert den Strukturprogramm- oder den Strukturtypknoten der durch eine
	 * Pfadangabe spezifiziert ist.
	 * 
	 * @param dirEntryPath
	 *            Pfadangabe als Zeichenkette
	 * 
	 * @return Strukturprogramm- oder Strukturtypknoten
	 */
	public KStructNode getProgramOrUnit(String dirEntryPath) {
		int endProjectName = dirEntryPath.indexOf('.');
		int begProjectName = dirEntryPath.startsWith(KStructNode.m_separator) ? 1 : 0;
		int endName = dirEntryPath.lastIndexOf('.');
		int begName = dirEntryPath.lastIndexOf(KStructNode.m_separator, endName - 1) + 1;
		if ((0 < endProjectName) && (endProjectName < begName) && (begName < endName)) {
			String projectName = dirEntryPath.substring(begProjectName, endProjectName);
			KStructProject project = getKStructProject(projectName);
			if (project != null) {
				String name;
				String extension;

				name = dirEntryPath.substring(begName, endName);
				extension = dirEntryPath.substring(endName);
				if (extension.equals(KTcDfl.PROG_FILE_EXTENSION)) {
					return (KStructProgram) project.programs.getChild(name, true);
				} else if (extension.equals(KTcDfl.OBJECT_FILE_EXTENSION)) {
					return (KStructTypeUnit) project.units.getChild(name, true);
				}
			}
		}
		return null;
	}

	/**
	 * Sets the filter for multi kinematic,
	 * 
	 * @param filter
	 *            kinematic directory
	 */
	public void setGlobalFilter(String filter) {
//		synchronized (dfl.getLockObject()) {
			if (filter != null) {
				globalFilter = filter.toLowerCase();
			} else {
				globalFilter = null;
			}
			filterChanged = true;
//		}
		pollThread.interrupt();
	}

	/**
	 * Liefert den Wurzelknoten des Strukturbaumes.
	 * 
	 * @return Wurzelknoten
	 */
	public KStructRoot getRoot() {
		return root;
	}

	/**
	 * Fügt dem Administrator einen Klienten (Listener) hinzu
	 * 
	 * @param listener
	 *            Klient
	 */
	public void addStructAdministratorListener(KStructAdministratorListener listener) {
		if (!m_modelListenerList.contains(listener)) {
			m_modelListenerList.addElement(listener);
		}
	}

	/**
	 * Fügt dem Administrator einen Klienten (Listener) hinzu
	 * 
	 * @param listener
	 *            Klient
	 */
	public void addMultikinematikListener(KMultikinematicListener listener) {
		if (!m_kinematikListenerList.contains(listener)) {
			m_kinematikListenerList.addElement(listener);
		}
	}

	/**
	 * Wird aufgerufen, wenn sich der Strukturbaum verändert hat. Die Methode
	 * informiert alle Klienten (Listener) über die Veränderung.
	 * 
	 * @param parent
	 *            Strukturknoten der sich verändert hat
	 */
	public void fireTreeChanged(KStructNode parent) {
		int priority = Thread.currentThread().getPriority();
		Thread.currentThread().setPriority(HIGH_PRIORITY);
		for (int i = 0; i < m_modelListenerList.size(); i++) {
			try {
				((KStructAdministratorListener) m_modelListenerList.elementAt(i)).treeChanged(parent);
			} catch (Exception e) {
				KDflLogger.error(KStructAdministrator.class, "", e);
			}
		}
		Thread.currentThread().setPriority(priority);
	}

	/**
	 * Wird aufgerufen, wenn zum Strukturbaum ein Knoten hinzugefügt wird. Die
	 * Methode informiert alle Klienten (Listener) über die Veränderung.
	 * 
	 * 
	 * @param parent
	 *            übergeordneter Strukturknoten
	 * @param node
	 *            neu hinzugefügter Knoten
	 */
	public void fireNodeInserted(KStructNode parent, KStructNode node) {
		int priority = Thread.currentThread().getPriority();
		Thread.currentThread().setPriority(HIGH_PRIORITY);
		for (int i = 0; i < m_modelListenerList.size(); i++) {
			try {
				((KStructAdministratorListener) m_modelListenerList.elementAt(i)).nodeInserted(parent, node);
			} catch (Exception e) {
				KDflLogger.error(KStructAdministrator.class, "", e);
			}
		}
		Thread.currentThread().setPriority(priority);
	}

	/**
	 * Wird aufgerufen, wenn zum Strukturbaum ein Knoten hinzugefügt wird. Die
	 * Methode informiert alle Klienten (Listener) über die Veränderung.
	 * 
	 * 
	 * @param parent
	 *            übergeordneter Strukturknoten
	 * @param node
	 *            neu hinzugefügter Knoten
	 */
	public void fireKinematikChanged() {
		for (int i = 0; i < m_kinematikListenerList.size(); i++) {
			try {
				((KMultikinematicListener) m_kinematikListenerList.elementAt(i)).kinematikChanged();
			} catch (Exception e) {
				KDflLogger.error(KStructAdministrator.class, "", e);
			}
		}
	}

	/**
	 * Fügt dem Administrator einen Klienten (Listener) hinzu
	 * 
	 * @param listener
	 *            Klient
	 */
	public void removeMultikinematicListener(KMultikinematicListener listener) {
		m_kinematikListenerList.removeElement(listener);
	}

	/**
	 * Wird aufgerufen, wenn aus dem Strukturbaum ein Knoten entfernt wurde. Die
	 * Methode informiert alle Klienten (Listener) über die Veränderung.
	 * 
	 * 
	 * @param parent
	 *            übergeordneter Strukturknoten
	 * @param node
	 *            neu hinzugefügter Knoten
	 */
	public void fireNodeRemoved(KStructNode parent, KStructNode node) {
		int priority = Thread.currentThread().getPriority();
		Thread.currentThread().setPriority(HIGH_PRIORITY);
		for (int i = 0; i < m_modelListenerList.size(); i++) {
			try {
				((KStructAdministratorListener) m_modelListenerList.elementAt(i)).nodeRemoved(parent, node);
			} catch (Exception e) {
				KDflLogger.error(KStructAdministrator.class, "", e);
			}
		}
		Thread.currentThread().setPriority(priority);
	}

	/**
	 * Laden der Strukturknoten für das globale Projekt.
	 */
	protected void loadRoot() {
		TcStructuralNode r = dfl.client.structure.getRoot();
		root.setTcStructuralNode(r);
		root.setLoaded(r == null);
		if (r != null) {
			root.loadChildren();
		}
	}

	/**
	 * Entladen der Strukturknoten für das globale Projekt.
	 */
	protected void unloadRoot() {
		root.setLoaded(true);
		root.setTcStructuralNode(null);
		root.clear();
	}

	protected void startLoad() {
		programCounter = 0;
		projectCounter = 0;
		constCounter = 0;
		typeCounter = 0;
		varCounter = 0;
		routineCounter = 0;
		startLoad = System.currentTimeMillis();

	}

	protected void endLoad(String name) {
		KDflLogger.info(KStructAdministrator.class, "KStructAdministrator has loaded project (" + name + "): " + (projectCounter + programCounter + constCounter + typeCounter + varCounter + routineCounter) + " nodes (" + "projects: " + projectCounter
				+ ", programs: " + programCounter + ", constants: " + constCounter + ", types: " + typeCounter + ", variables and parameters: " + varCounter + ", routines: " + routineCounter + "), time: " + (System.currentTimeMillis() - startLoad));

	}

	/**
	 * Entladen der Strukturknoten für ein angegebenes Projekt.
	 * 
	 * @param project
	 *            Projektstrukturknoten
	 */
	protected void unloadProject(KStructProject project) {
		project.clear();
	}

	/**
	 * Laden der Programmstrukturknoten für ein angegebenes Projekt.
	 * 
	 * @param parent
	 *            Projektstrukturknoten werden.
	 */
	protected void loadPrograms(KStructProject parent) {
		Enumeration e = dfl.client.structure.getNodes(parent.getTcStructuralNode(), TcStructuralNode.PROGRAM);
		while (e.hasMoreElements()) {
			programCounter++;
			TcStructuralNode p = (TcStructuralNode) e.nextElement();
			createProgram(parent, p);
		}
	}

	private KStructProgram createProgram(KStructProject project, TcStructuralNode tcp) {
		KStructProgram program = new KStructProgram(tcp.getName(), dfl);
		program.setTcStructuralNode(tcp);
		project.programs.addChild(program);
		project.programsAndUnits.put(tcp, program);
		program.setLoaded(project.hasCompileError());
		return program;
	}

	/**
	 * Erzeugt für ein angegebenes Datei-Handle das zugehörige Projekt und hängt
	 * es im Strukturbaum ein.
	 * 
	 * @param dirEntry
	 *            Datei-Handle
	 * 
	 * @return wahr, wenn beim Erzeugen keine Übersetzungsfehler aufgetreten
	 *         sind.
	 */
	public boolean buildProject(KDirEntry dirEntry) {
//		KvtLogger.error(this, "Starting to build project "+dirEntry.getDirEntryPath());
		TcDirEntry tcDirEntry = dirEntry.getTcDirEntry();
		KStructProject n = null;

		// load or reload project into structural tree
		boolean hasCompileError = true;
		synchronized (dfl.getLockObject()) {
			TcStructuralNode proj = dfl.client.structure.build(tcDirEntry);
//			KvtLogger.error(this, "TcStructuralNode obtained by dfl.client.structure.build("+dirEntry.getDirEntryPath()+") returned: "+proj);
			if (proj != null) {
				boolean wasGlobal = (proj.getKind() == TcStructuralNode.GLOBAL);
//				KvtLogger.error(this, "Project is global: "+wasGlobal);
				n = getKStructProject(proj);
				if (n != null) {
//					KvtLogger.error(this, "KStructProject not null!");
					hasCompileError = dfl.error.checkCompilerError(n);
//					KvtLogger.error(this, "Project has compile errors: "+hasCompileError);
					n.setHasCompileError(hasCompileError);
					n.clear();
					n.setLoaded(false);
					fireTreeChanged(n);
				} else {
					KStructProject old = getKStructProject(proj.getName());
					if ((old instanceof KStructGlobal) && (!wasGlobal)) {
						old = getKStructProject(root, proj.getName(), true);
					}
					if (old != null) {
						// remove old project node
						KStructScope parent = (KStructScope) old.getParent();
						parent.projects.removeChild(old);
						fireNodeRemoved(parent, old);
					}
					n = createProject(proj);
					if (n != null) {
						hasCompileError = n.hasCompileError();
					}
				}
			}else {
				TcErrorMessage msg= dfl.client.structure.getErrorMessage();
			}
		}
		return !hasCompileError;
	}

	private KStructProject createProject(TcStructuralNode proj) {
		TcStructuralNode tcp = proj.getParent();
		KStructProject parent = tcp.equals(root.getTcStructuralNode()) ? root : getKStructProject(tcp);
		if ((parent == null) && (tcp != null)) {
			parent = createProject(tcp);
		}
		if (parent instanceof KStructScope) {
			KStructProject n = getKStructProject(proj);
			if (n == null) {
				n = createProject((KStructScope) parent, proj);
			} else {
				boolean hasCompileError = dfl.error.checkCompilerError(n);
				n.setHasCompileError(hasCompileError);
			}
			if (n != null) {
				fireNodeInserted(parent, n);
			}
			return n;
		}
		return null;
	}

	/**
	 * Entfernen eines Projektes aus dem Strukturbaum.
	 * 
	 * @param project
	 *            Projekt, das zu entfernen ist.
	 * 
	 * @return wahr, wenn der Projektknoten erfolgreich entfernt wurde.
	 */
	public boolean destroyProject(KStructProject project) {
		synchronized (dfl.getLockObject()) {
			dfl.error.removeCompilerErrorList(project);
			TcStructuralNode osn = project.getTcStructuralNode();
			if ((osn != null) && dfl.client.structure.destroy(osn)) {
				KStructScope parent = (KStructScope) project.getParent();

				parent.projects.removeChild(project);
				fireNodeRemoved(parent, project);
				return true;
			}
		}
		return false;
	}

	/**
	 * Laden aller vorhandenen Projekte
	 * 
	 * @param parent
	 *            Wurzelknoten
	 */
	protected void loadProjects(KStructScope parent, boolean notify) {
		Enumeration e = dfl.client.structure.getNodes(parent.getTcStructuralNode(), TcStructuralNode.PROJECT);
		while (e.hasMoreElements()) {
			TcStructuralNode p;

			projectCounter++;
			p = (TcStructuralNode) e.nextElement();
			if ((globalFilter == null) || (p.getKind() != TcStructuralNode.GLOBAL) || (globalFilter.equals(p.getName().toLowerCase()))) {
				KStructProject proj = createProject(parent, p);
				if (proj != null && notify) {
					fireNodeInserted(parent, proj);
				}
			}
		}
	}

	/**
	 * Entfernt einen Klienten(Listener) des Strukturadministrators.
	 * 
	 * @param listener
	 *            Klient
	 */
	public void removeStructAdministratorListener(KStructAdministratorListener listener) {
		m_modelListenerList.removeElement(listener);
	}

	/**
	 * Description of the method
	 * 
	 * @param variables
	 *            Description of the parameter
	 * @param ortsVar
	 *            Description of the parameter
	 * 
	 * @return Description of the return value
	 */
	private KStructVar checkVarIn(KStructNodeVector variables, TcStructuralVarNode ortsVar) {
		for (int i = 0; i < variables.getChildCount(); i++) {
			KStructVar ev;

			ev = (KStructVar) variables.getChild(i);
			TcStructuralVarNode tsn = (TcStructuralVarNode) ev.getTcStructuralNode();
			if ((tsn != null) && tsn.equals(ortsVar)) {
				return ev;
			}
		}
		return null;
	}

	private KStructProject createProject(KStructScope parent, TcStructuralNode project) {
		for (int i = 0; i < parent.projects.getChildCount(); i++) {
			KStructProject ep = (KStructProject) parent.projects.getChild(i);
			if (ep.getTcStructuralNode().equals(project)) {
				// is already loaded
				return null;
			}
		}
		KStructProject p = null;
		String key = project.getName();
		switch (project.getKind()) {
		case TcStructuralNode.SYSTEM:
			p = new KStructSystem(key, dfl);
			break;
		case TcStructuralNode.GLOBAL:
			p = new KStructGlobal(key, dfl);
			break;
		case TcStructuralNode.PROJECT:
			p = new KStructProject(key, dfl);
			break;
		}
		if (p != null) {
			p.setTcStructuralNode(project);
			parent.projects.addChild(p);
			boolean hasCompileError = dfl.error.checkCompilerError(p);
			p.setHasCompileError(hasCompileError);
		}
		return p;
	}

	/**
	 * Description of the Method
	 */
	private void check(KStructScope parent) {
		// project changed or removed
		boolean changed = false;
		int i = 0;
		while (i < parent.projects.getChildCount()) {
			KStructProject p = (KStructProject) parent.projects.getChild(i);
			TcStructuralNode n = p.getTcStructuralNode();
			if ((dfl.client.structure.getDirEntry(n) == null) || ((p instanceof KStructGlobal) && (globalFilter != null) && !globalFilter.equals(n.getName().toLowerCase()))) {
				// remove
				parent.projects.removeChild(p);
				// clear cache
				dfl.error.removeCompilerErrorList(p);
				fireNodeRemoved(parent, p);
			} else {
				// check project
				changed = false;
				if (0 < p.programs.getChildCount()) {
					n = ((KStructProgram) p.programs.getChild(0)).getTcStructuralNode();
					changed = dfl.client.structure.getDirEntry(n) == null;
				} else if (0 < p.units.getChildCount()) {
					n = ((KStructTypeUnit) p.units.getChild(0)).getTcStructuralNode();
					changed = dfl.client.structure.getDirEntry(n) == null;
				}
				if (changed) {
					// relaod project
					p.clear();
					p.setLoaded(false);
					p.setHasCompileError(dfl.error.checkCompilerError(p));
					fireTreeChanged(p);
				}
				i++;
			}
		}

		if (parent.getTcStructuralNode() != null) {
			// check for new projects
			Enumeration e = dfl.client.structure.getNodes(parent.getTcStructuralNode(), TcStructuralNode.PROJECT);
			while (e.hasMoreElements()) {
				TcStructuralNode n = (TcStructuralNode) e.nextElement();
				KStructProject p = null;
				i = 0;
				while (i < parent.projects.getChildCount()) {
					p = (KStructProject) parent.projects.getChild(i);
					if (p.getTcStructuralNode().equals(n)) {
						break;
					}
					i++;
				}
				if (i == parent.projects.getChildCount()) {
					if ((globalFilter == null) || (n.getKind() != TcStructuralNode.GLOBAL) || globalFilter.equals(n.getName().toLowerCase())) {
						// insert project
						p = createProject(parent, n);
						if (p != null) {
							fireNodeInserted(parent, p);
						}
					}
				} else if (p instanceof KStructScope) {
					check((KStructScope) p);
				}
			}
		}
	}

	private void checkChangeList(Vector toInsertTcs) {
		TcStructuralChanges changes = dfl.client.structure.getChanges(startChangeCounter);
		if (changes != null) {
			// same changes happened
			startChangeCounter = changes.nextChangeCounter;
			for (int i = 0; i < changes.nrOfNodes; i++) {
				TcStructuralNode node = changes.nodes[i];
				if (node instanceof TcStructuralInvalidNode) {
					// node removed
					KStructNode n = removeNode(root, node);
					if (n != null) {
						KStructNode parent = n.getParent();
						fireNodeRemoved(parent, n);
					}
					if (toInsertTcs != null) {
						toInsertTcs.removeElement(node);
					}
				} else {
					// node inserted
					KStructNode n = insertNode(node);
					if (n != null) {
						KStructNode parent = n.getParent();
						fireNodeInserted(parent, n);
					}
				}
			}
			if (changes.hasMoreChanges) {
				checkChangeList(toInsertTcs);
			}
		}
	}

	private KStructNode insertNode(TcStructuralNode node) {
		KStructNode n = getKStructNode(node);
		if (n == null) {
			// isn't already loaded
			TcStructuralNode parent = node.getParent();
			if (parent != null) {
				KStructNode p = getKStructNode(parent);
				if ((p != null) && (p.isLoaded())) {
					switch (node.getKind()) {
					case TcStructuralNode.SYSTEM:
					case TcStructuralNode.GLOBAL:
					case TcStructuralNode.PROJECT:
						if (p instanceof KStructScope) {
							if ((globalFilter == null) || (node.getKind() != TcStructuralNode.GLOBAL) || (globalFilter.equals(node.getName().toLowerCase()))) {
								n = createProject((KStructScope) p, node);
							}
						}
						break;
					case TcStructuralNode.PROGRAM:
						if (p instanceof KStructProject) {
							n = createProgram((KStructProject) p, node);
						}
						break;
					case TcStructuralNode.VAR:
						n = variableFactory.addNode(p, (TcStructuralVarNode) node);
						break;
					case TcStructuralNode.TYPE:
						n = typeFactory.addNode(p, (TcStructuralTypeNode) node);
						break;
					case TcStructuralNode.ROUTINE:
						n = routineFactory.addNode(p, (TcStructuralRoutineNode) node);
						break;
					case TcStructuralNode.CONST:
						n = constFactory.addNode(p, (TcStructuralConstNode) node);
						break;
					}
					return n;
				}
			}
		}
		return null;
	}

	private KStructNode removeNode(KStructNode parent, TcStructuralNode node) {
		if (parent.isLoaded()) {
			if (parent instanceof KStructScope) {
				KStructNode n = getChild(((KStructScope) parent).projects, node);
				if (n != null) {
					((KStructScope) parent).projects.removeChild(n);
					return n;
				}
			}
			if (parent instanceof KStructProject) {
				KStructNode n = getChild(((KStructProject) parent).variables, node);
				if (n != null) {
					((KStructProject) parent).variables.removeChild(n);
					return n;
				}
				n = (KStructNode) ((KStructProject) parent).programsAndUnits.get(node);
				if (n != null) {
					((KStructProject) parent).programsAndUnits.remove(node);
					if (n instanceof KStructProgram) {
						((KStructProject) parent).programs.removeChild(n);
					} else {
						((KStructProject) parent).units.removeChild(n);
						((KStructProject) parent).allTypes.remove(node);
					}
					return n;
				}
				n = (KStructNode) ((KStructProject) parent).allTypes.get(node);
				if (n != null) {
					((KStructProject) parent).allTypes.remove(node);
					parent = n.getParent();
					if (parent instanceof KStructProject) {
						((KStructProject) parent).types.removeChild(n);
					} else if (parent instanceof KStructProgram) {
						((KStructProgram) parent).types.removeChild(n);
					} else if (parent instanceof KStructTypeUnit) {
						((KStructTypeUnit) parent).types.removeChild(n);
					}
					return n;
				}
				n = (KStructNode) ((KStructProject) parent).allConstants.get(node);
				if (n != null) {
					((KStructProject) parent).allConstants.remove(node);
					parent = n.getParent();
					if (parent instanceof KStructProgram) {
						((KStructProgram) parent).constants.removeChild(n);
					} else if (parent instanceof KStructTypeUnit) {
						((KStructTypeUnit) parent).constants.removeChild(n);
					} else if (parent instanceof KStructTypeEnum) {
						((KStructTypeEnum) parent).constants.removeChild(n);
					} else if (parent instanceof KStructProject) {
						((KStructProject) parent).constants.removeChild(n);
					}

					((KStructProject) parent).constants.removeChild(n);
					return n;
				}
				n = (KStructNode) ((KStructProject) parent).allRoutines.get(node);
				if (n != null) {
					((KStructProject) parent).allRoutines.remove(node);
					parent = n.getParent();
					if (parent instanceof KStructProgram) {
						((KStructProgram) parent).routines.addChild(n);
					} else if (parent instanceof KStructTypeUnit) {
						((KStructTypeUnit) parent).routines.addChild(n);
					} else if (parent instanceof KStructProject) {
						((KStructProject) parent).routines.addChild(n);
					}
					((KStructProject) parent).routines.removeChild(n);
					return n;
				}
				KStructNodeVector children = ((KStructProject) parent).programs;
				for (int i = 0; i < children.getChildCount(); i++) {
					n = removeNode(children.getChild(i), node);
					if (n != null) {
						return n;
					}
				}
			}
			if (parent instanceof KStructProgram) {
				KStructNode n = getChild(((KStructProgram) parent).variables, node);
				if (n != null) {
					((KStructProgram) parent).variables.removeChild(n);
					return n;
				}
			}
			if (parent instanceof KStructScope) {
				KStructNodeVector children = ((KStructScope) parent).projects;
				for (int i = 0; i < children.getChildCount(); i++) {
					KStructNode n = removeNode(children.getChild(i), node);
					if (n != null) {
						return n;
					}
				}
			}
		}
		return null;
	}

	private KStructNode getChild(KStructNodeVector children, TcStructuralNode tcn) {
		for (int i = 0; i < children.getChildCount(); i++) {
			KStructNode child = children.getChild(i);
			if (tcn.equals(child.getTcStructuralNode())) {
				return child;
			}
		}
		return null;
	}

	private class PollThread extends Thread {
		/** poll flag, true if states and executions should be checked */
		private boolean m_bDoPoll = true;

		private PollThread() {
			super("KStructAdministrator");
		}

		/**
		 * Main processing method for the PollThread object
		 */
		public void run() {
			try {
				Thread.sleep(200);
			} catch (InterruptedException ie) {
			}
			while (m_bDoPoll) {
				try {
					try {
						synchronized (dfl.getLockObject()) {
							Vector projects = null;
							KStructSystem system = null;
							boolean kinematikChanged = filterChanged;
							if (filterChanged) {
								filterChanged = false;
								// remove current global project
								system = (KStructSystem) getKStructProject("_system");
								if (system != null) {
									int i = 0;
									while (i < system.projects.getChildCount()) {
										KStructProject proj = (KStructProject) system.projects.getChild(i);
										if ((globalFilter != null) && (proj instanceof KStructGlobal) && !globalFilter.equals(proj.getKey().toLowerCase())) {
											system.projects.removeChild(i);
											dfl.error.removeCompilerErrorList(proj);
											fireNodeRemoved(system, proj);
										} else {
											i++;
										}
									}
									// get all global project which meet the
									// global filter but dosen't insert it until
									// the change list is processed.
									Enumeration e = dfl.client.structure.getNodes(system.getTcStructuralNode(), TcStructuralNode.PROJECT);
									while (e.hasMoreElements()) {
										projectCounter++;
										TcStructuralNode p = (TcStructuralNode) e.nextElement();
										if ((globalFilter == null) || (p.getKind() != TcStructuralNode.GLOBAL) || (globalFilter.equals(p.getName().toLowerCase()))) {
											if (projects == null) {
												projects = new Vector();
											}
											projects.addElement(p);
										}
									}
								}
							}
							if (dfl.client.structure.supportsChangeList()) {
								checkChangeList(projects);
							} else {
								check(root);
							}
							if ((system != null) && (projects != null)) {
								// add all global projects which aren't inserted
								// yet
								for (int i = 0; i < projects.size(); i++) {
									TcStructuralNode p = (TcStructuralNode) projects.elementAt(i);
									KStructProject proj = createProject(system, p);
									if (proj != null) {
										fireNodeInserted(system, proj);
									}
								}
							}
							if (kinematikChanged) {
								fireKinematikChanged();
							}
						}
					} catch (Exception e) {
						KDflLogger.error(this, "KStructAdministrator.PollThread:", e);
					}
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException ie) {
					// ignore
				}
			}
		}

		/**
		 * stops poll
		 */
		public void stopPoll() {
			m_bDoPoll = false;
			interrupt();
		}
	}
}
