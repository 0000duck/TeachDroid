/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Projekt   : KEMRO.teachview.4
 *    Auftragsnr: 5500395
 *    Erstautor : wies
 *    Datum     : 01.04.2003
 *--------------------------------------------------------------------------
 *      Revision:
 *        Author:
 *          Date:
 *------------------------------------------------------------------------*/
package com.keba.kemro.kvs.teach.data.program;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import android.util.Log;

import com.keba.kemro.kvs.teach.constant.KvtCAttributeKey;
import com.keba.kemro.kvs.teach.data.project.KvtProgram;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.kvs.teach.util.KvtTeachviewConnectionListener;
import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.edit.KEditKW;
import com.keba.kemro.teach.dfl.edit.KEditor;
import com.keba.kemro.teach.dfl.structural.KStructAdministratorListener;
import com.keba.kemro.teach.dfl.structural.KStructGlobal;
import com.keba.kemro.teach.dfl.structural.KStructNode;
import com.keba.kemro.teach.dfl.structural.KStructNodeVector;
import com.keba.kemro.teach.dfl.structural.KStructProgram;
import com.keba.kemro.teach.dfl.structural.KStructProject;
import com.keba.kemro.teach.dfl.structural.KStructRoot;
import com.keba.kemro.teach.dfl.structural.KStructScope;
import com.keba.kemro.teach.dfl.structural.constant.KStructConst;
import com.keba.kemro.teach.dfl.structural.routine.KStructRoutine;
import com.keba.kemro.teach.dfl.structural.type.KStructType;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeBool;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeDInt;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeDWord;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeEnum;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeLInt;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeMapTo;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeReal;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeRoutine;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeString;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeSubrange;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeUnit;
import com.keba.kemro.teach.dfl.structural.var.KStructVar;
import com.keba.kemro.teach.dfl.value.DWord;
import com.keba.kemro.teach.dfl.value.KStructVarWrapper;

/**
 * Die Klasse beschreibt alle Methoden die zum Einfügen und Löschen einer
 * Anweisung in den bzw aus dem Quelltext notwendig sind. Besitzt eine
 * einzufügende Anweisung Parameter, so werden dafür Variablen im zugehörigen
 * Programm angelegt und beim Entfernen wieder gelöscht.
 */
public class KvtStatementAdministrator implements KStructAdministratorListener {
	public static interface ChangeListener {
		public static int	CHANGE	= 0;
		public static int	REMOVE	= 1;
		public static int	INSERT	= 2;

		public void statementChanged(int type, int line, int count, String oldStatement);
	}

	// manages types and variables of specified type
	// private static KvtVarManager m_varManager = new KvtVarManager();
	// public static final String VAR_TYPES_RESOURCE = "vartypes";
	public static final String						GLOBAL_VAR_PRG		= KvtProjectAdministrator.GLOBAL_VAR_PRG;
	// /////////////////////////////////////////////
	// constant string parts for insert statement
	// /////////////////////////////////////////////
	private final static String						LEFT_BRACKET		= "(";
	private final static String						RIGHT_BRACKET		= ")";
	private final static String						SEPARATOR			= ",";
	private final static String						SEMICOLON			= ";";
	private final static String						CAR_RET				= "\n";
	private static final String						UNNAMED				= "*";
	private static final Vector						m_listener			= new Vector(3);
	private static final KvtStatementAdministrator	m_admin				= new KvtStatementAdministrator();

	private static KStructProgram					tableScope;
	private static final Hashtable					routineLockUpTable	= new Hashtable(300);
	private static Hashtable						reuseVariables		= new Hashtable();

	public static boolean							projectScope		= true;
	private static KTcDfl							dfl;

	/**
	 * adds a reuse variable to internal cache
	 * 
	 * @param key
	 *            variable key
	 * @param value
	 *            variable value
	 */
	public static void putReuseVariable(Object key, Object value) {
		reuseVariables.put(key, value);
	}

	/**
	 * gets a reuse variable from internal cache or null if none exists
	 * 
	 * @param key
	 *            variable key
	 * @return variable value or null
	 */
	public static Object getReuseVariable(Object key) {
		return reuseVariables.get(key);
	}

	/**
	 * initializes internal structures
	 */
	public static void init() {
		String s = "project";
		projectScope = (s != null) && !s.equalsIgnoreCase("program");
		reuseVariables = new Hashtable();
		KvtSystemCommunicator.addConnectionListener(new KvtTeachviewConnectionListener() {
			public void teachviewConnected() {
				dfl = KvtSystemCommunicator.getTcDfl();
				synchronized (dfl.getLockObject()) {

					dfl.structure.addStructAdministratorListener(m_admin);
					m_admin.treeChanged(dfl.structure.getRoot());

				}

			}

			public void teachviewDisconnected() {
				synchronized (dfl.getLockObject()) {
					dfl.structure.removeStructAdministratorListener(m_admin);
					dfl = null;
					KvtVarManager.removeAllVariables();
					tableScope = null;
					routineLockUpTable.clear();
					KvtUserTypeAdministrator.removeUserTypes();
				}
			}
		});
	}

	/**
	 * add listener which should be informed on statement changes
	 * 
	 * @param listener
	 *            ChangeListener instance
	 */
	public static void addListener(ChangeListener listener) {
		if (!m_listener.contains(listener)) {
			m_listener.addElement(listener);
		}
	}

	/**
	 * remove a listener
	 * 
	 * @param listener
	 *            ChangeListener instance
	 */
	public static void removeListener(ChangeListener listener) {
		m_listener.removeElement(listener);
	}

	private static void fireChanged(int type, int line, int count, String oldStatement) {
		for (int i = 0; i < m_listener.size(); i++) {
			((ChangeListener) m_listener.elementAt(i)).statementChanged(type, line, count, oldStatement);
		}
	}

	/**
	 * Die Methode liefert eine Aufzählung der Parameter einer eingefügten
	 * Anweisung
	 * 
	 * @param statement
	 *            Strukturknoten der Anweisung die eingefügt wurde
	 */
	public static Enumeration getParametersOfStatement(KStructNodeVector statement) {
		int count = statement.getChildCount();
		Vector parameters = new Vector();
		for (int i = 0; i < count; i++) {
			KStructNode param = statement.getChild(i);
			parameters.addElement(param);
		}
		return parameters.elements();
	}

	/**
	 * add a statement to a routine add the given line
	 * 
	 * @param text
	 *            statement
	 * @param routine
	 *            routine node
	 * @param lineNrOfFile
	 *            line where to add the statement
	 * @return true on success
	 */
	public static boolean insert(String text, KStructRoutine routine, int lineNrOfFile) {
		boolean inserting = false;
		if (text != null && routine != null && lineNrOfFile > -1) {
			KTcDfl d = dfl;
			if (d != null) {
				KEditor editAdmin = d.editor.getKEditor((KStructProgram) routine.getParent());
				if (editAdmin != null) {
					Log.d(KvtStatementAdministrator.class.toString(), "Insert Statement: " + text);
					inserting = editAdmin.insertTextInc(routine, lineNrOfFile, text);
					d.editor.dismissKEditor(editAdmin);
				}
				if (inserting) {
					int lines = 0;
					for (int i = 0; i < text.length(); i++) {
						if (text.charAt(i) == '\n') {
							lines++;
						}
					}
					fireChanged(ChangeListener.INSERT, lineNrOfFile, lines, "");
				}
			}
		}
		return inserting;
	}

	/**
	 * replaces some text in the program source
	 * 
	 * @param text
	 *            new statements
	 * @param routine
	 *            routine node
	 * @param lineNrOfFile
	 *            existing text start line
	 * @param lineCount
	 *            number of lines to replace
	 * @return true on success
	 */
	public static boolean replace(String text, KStructRoutine routine, int lineNrOfFile, int lineCount) {
		boolean replacing = false;
		if (text != null && routine != null && lineNrOfFile > -1) {
			KTcDfl d = dfl;
			if (d != null) {
				KEditor editAdmin = d.editor.getKEditor((KStructProgram) routine.getParent());
				if (editAdmin != null) {
					StringBuffer buf = new StringBuffer();
					for (int i = 0; i < lineCount; i++) {
						buf.append(editAdmin.getLine(lineNrOfFile + i) + "\n");
					}
					replacing = editAdmin.replaceTextInc(routine, lineNrOfFile, lineCount, text);
					d.editor.dismissKEditor(editAdmin);
					if (replacing) {
						int lines = 0;
						for (int i = 0; i < text.length(); i++) {
							if (text.charAt(i) == '\n') {
								lines++;
							}
						}
						fireChanged(ChangeListener.CHANGE, lineNrOfFile, lines, buf.toString());
					}
				}
			}
		}
		return replacing;
	}

	/**
	 * remove some text from program source
	 * 
	 * @param routine
	 *            routine node
	 * @param lineNrOfFile
	 *            start line
	 * @param lineCount
	 *            number of lines
	 * @return true on success
	 */
	public static boolean remove(KStructRoutine routine, int lineNrOfFile, int lineCount) {
		boolean deleting = false;
		KTcDfl d = dfl;
		if (d != null) {
			KEditor editAdmin = d.editor.getKEditor((KStructProgram) routine.getParent());
			if (editAdmin != null) {
				StringBuffer buf = new StringBuffer();
				for (int i = 0; i < lineCount; i++) {
					buf.append(editAdmin.getLine(lineNrOfFile + i) + "\n");
				}
				deleting = editAdmin.removeTextInc(routine, lineNrOfFile, lineCount);
				d.editor.dismissKEditor(editAdmin);
				if (deleting) {
					fireChanged(ChangeListener.REMOVE, lineNrOfFile, lineCount, buf.toString());
				}
			}
		}
		return deleting;
	}

	/**
	 * Die Methode entfernt angelegt Variablen aus dem Quelltext. Für die zu
	 * entfernenden Variablen werden die entsprechenden Strukturknoten in einer
	 * Aufzählung übergeben
	 * 
	 * @param var
	 *            Sammlung von Strukturknoten der Variablen
	 */
	public static boolean removeVariable(KStructVar var) {
		KTcDfl d = dfl;
		if (d != null) {
			return d.structure.removeVariableInc(var);
		}
		return false;
	}

	/**
	 * create a new variable of given type in the given scope
	 * 
	 * @param name
	 *            variable name
	 * @param type
	 *            variable type
	 * @param scope
	 *            where to add it
	 * @param isGlobal
	 *            global variable ?
	 * @param isDynamic
	 *            dynamic variable
	 * @return new variable node or null
	 */
	public static KStructVar addVariable(String name, KStructType type, KStructNode scope, boolean isGlobal, boolean isDynamic) {
		KStructProgram dirEntry = null;
		KStructNode parent;
		int visibility;
		KTcDfl d = dfl;
		if (d != null) {
			if (isGlobal) { // _globalVars
				KStructProject project = (scope instanceof KStructProject) ? (KStructProject) scope : scope.getKStructProject();
				if (!d.client.getUserMode() && !scope.isUserNode()) {
					dirEntry = (KStructProgram) checkInChildren(project.programs, GLOBAL_VAR_PRG, false, false);
					visibility = KStructNode.GLOBAL;
					if (dirEntry == null) {
						dirEntry = d.structure.addProgramInc(project, GLOBAL_VAR_PRG);
					}
				}
				parent = project;
				visibility = KStructNode.GLOBAL;
			} else { // program/Public
				dirEntry = (KStructProgram) ((scope instanceof KStructProgram) ? scope : scope.getParent());
				parent = dirEntry;
				visibility = KStructNode.PRIVATE;
			}
			if (parent != null) {
				boolean isSave = d.client.getUserMode() || scope.isUserNode() ? false : true;
				return d.structure.addVariableInc(dirEntry, parent, name, type, false, isSave, visibility, isDynamic);
			}
		}
		return null;
	}

	/**
	 * creates a new variable with the given prefix. searches for the correct
	 * name itself
	 * 
	 * @param prefix
	 *            variable prefix (example: str)
	 * @param type
	 *            variable type
	 * @param scope
	 *            where to add the variable
	 * @param isDynamic
	 *            variable without declaration in sourcecode
	 * @return variable node or null
	 */
	public static KStructVar createVariable(String prefix, KStructType type, KStructNode scope, boolean isGlobal, boolean isDynamic) {
		// if ((prefix != null) && (KvtSystem.VARIABLES_TO_UPPER_CASE)){
		// prefix = prefix.toUpperCase();
		// }
		KStructVar structVar = null;
		if (prefix == null) {
			prefix = createNameFor(type);
		}
		int trycount = 0;
		int varCount = KvtVarManager.getNextVarSequenceNumber(prefix, scope);
		while ((trycount < 20) && (structVar == null)) {
			String name = prefix + (varCount + trycount);
			// if (KvtSystem.VARIABLES_TO_UPPER_CASE) {
			// name = name.toUpperCase();
			// }
			if (!checkInScope(name, scope, true, true)) {
				// create a new variable in specified program
				structVar = addVariable(name, type, scope, isGlobal, isDynamic);
			}
			trycount++;
		}
		/*
		 * if (prefix != null) { int varCount =
		 * KvtVarManager.getNextVarSequenceNumber(prefix, scope); String name =
		 * prefix + varCount; if (KvtSystem.VARIABLES_TO_UPPER_CASE) { name =
		 * name.toUpperCase(); } if (!checkInScope(name, scope, true, true)) {
		 * structVar = addVariable(name, type, scope, isGlobal, isDynamic); } }
		 * else { prefix = createNameFor(type); int varCount =
		 * KvtVarManager.getNextVarSequenceNumber(prefix, scope); String name =
		 * prefix + (varCount); if (!checkInScope(name, scope, true, true)) { //
		 * create a new variable in specified program structVar =
		 * addVariable(name, type, scope, isGlobal, isDynamic); } }
		 */
		return structVar;
	}

	/**
	 * gets the prefix for a type
	 * 
	 * @param type
	 *            variable type
	 * @return prefix or var_KEY_ on error
	 */
	public static String createNameFor(KStructType type) {
		String prefix = KvtUserTypeAdministrator.getPrefixFor(type);
		if ((prefix != null) && (!prefix.trim().equals(""))) {
			// if (KvtSystem.VARIABLES_TO_UPPER_CASE){
			// return prefix.trim().toUpperCase();
			// }
			return prefix.trim();
		}
		// if (KvtSystem.VARIABLES_TO_UPPER_CASE){
		// return ("var" + type.getKey() + "_").toUpperCase();
		// }
		return "var" + type.getKey() + "_";
	}

	private static String getPrefix(KStructVar variable) {
		String key = variable.getKey();
		int index = 0;
		while ((index < key.length()) && (key.charAt(index) < '0') && ('9' < key.charAt(index))) {
			index++;
		}
		// if (KvtSystem.VARIABLES_TO_UPPER_CASE){
		// return key.substring(0, index).toUpperCase();
		// }
		return key.substring(0, index);
	}

	/**
	 * wrapper for @link KvtVarManager#getNextVarSequenceNumber(String,
	 * KStructNode) returns the next possible variable number used for creating
	 * variable
	 * 
	 * @param prefix
	 *            type prefix
	 * @param node
	 *            node where to search for matching variables
	 * @return sequence number
	 */
	public static int getNextVarSequenceNumber(String prefix, KStructNode node) {
		return KvtVarManager.getNextVarSequenceNumber(prefix, node);
	}

	/**
	 * wrapper for @link KvtVarManager#resetVarSequenceNumber(String)
	 * 
	 * @param prefix
	 *            prefix for the variable
	 */
	public static void resetVarSequenceNumber(String prefix) {
		KvtVarManager.resetVarSequenceNumber(prefix);
	}

	/**
	 * gets the default actual parameter type for a formal parameter
	 * 
	 * @param param
	 *            parameter
	 * @param scope
	 *            scope to search for types
	 * @return KStructType node of default parameter
	 */
	public static KStructType getDefaultActualParamType(Parameter param, KStructNode scope) {
		// check if parameter has the "default" attribute set
		if (param.formalVar.containsAttribute(KvtCAttributeKey.DEFAULT)) {
			String defaultType = (String) param.formalVar.getAttribute(KvtCAttributeKey.DEFAULT);
			return KvtUserTypeAdministrator.findType(defaultType.trim(), scope);
		}

		return getDefaultActualParamType(param.formalType, scope);
	}

	private static boolean isEnabled(KStructTypeUnit unit) {
		if (unit != null) {
			String enable = (String) unit.getAttribute(KvtCAttributeKey.ENABLE);
			if (enable != null) {
				KStructVarWrapper wrappi = KvtSystemCommunicator.getTcDfl().variable.createKStructVarWrapper(enable);
				if (wrappi != null) {
					Object value = wrappi.readActualValue(null);
					if (value instanceof Boolean) {
						return ((Boolean) value).booleanValue();
					} else {
						return false;
					}
				}
				return false;
			}
		}
		return true;
	}

	/**
	 * gets the default actual parameter type for a formal parameter
	 * 
	 * @param formalParamType
	 *            KStructType node
	 * @param scope
	 *            scope to search for types
	 * @return KStructType node of default parameter
	 */
	private static KStructType getDefaultActualParamType(KStructType formalParamType, KStructNode scope) {
		if (formalParamType == null) {
			return null;
		}
		// fetchs all types which aren't abstract and haven't set the 'novar'
		// attribute
		Vector userTypes = KvtUserTypeAdministrator.getNewVarUserTypes(false);
		KStructType first = null;
		String name = formalParamType.getKey();
		for (int i = 0; i < userTypes.size(); i++) {
			String t = (String) userTypes.elementAt(i);
			if (t.equalsIgnoreCase(name)) {
				// formal parameter is valid default type
				first = formalParamType;
				break;
			}
		}
		if (skipAlias(formalParamType) instanceof KStructTypeUnit) {
			// if there isn't a valid formal parameter or it is a unit then
			// search for a inherited unit which is
			// marked as default or use the first inherited unit
			Vector defaultTypes = KvtUserTypeAdministrator.getDefaultTypes();
			KStructProject project = (scope instanceof KStructProject) ? (KStructProject) scope : scope.getKStructProject();
			while (project != null) {
				// search for inherited units
				for (int i = 0; i < project.units.getChildCount(); i++) {
					KStructTypeUnit unit = (KStructTypeUnit) project.units.getChild(i);
					KStructTypeUnit baseUnit = unit.getBaseUnit();
					while ((baseUnit != null) && !baseUnit.equals(formalParamType)) {
						baseUnit = baseUnit.getBaseUnit();
					}
					if ((baseUnit != null) && (isEnabled(unit))) {
						String tName = unit.getKey();
						for (int j = 0; j < userTypes.size(); j++) {
							String t = (String) userTypes.elementAt(j);
							if (t.equalsIgnoreCase(tName)) {
								if (first == null) {
									first = unit;
								}
								// check if this type has the default attribute
								// set
								for (int k = 0; k < defaultTypes.size(); k++) {
									t = (String) defaultTypes.elementAt(k);
									if (t.equalsIgnoreCase(tName)) {
										// default found
										return unit;
									}
								}

							}
						}
					}
				}
				project = (KStructProject) project.getParent();
			}
		}
		if (first != null) {
			return first;
		}
		if (formalParamType.isAliasType()) {
			return getDefaultActualParamType(formalParamType.getKStructType(), scope);
		}
		if ((formalParamType instanceof KStructTypeBool) || (formalParamType instanceof KStructTypeLInt)
				|| (formalParamType instanceof KStructTypeReal) || (formalParamType instanceof KStructTypeString)
				|| (formalParamType instanceof KStructTypeSubrange) || (formalParamType instanceof KStructTypeEnum)) {
			return formalParamType;
		}
		return null;
	}

	private static void checkFormalType(Parameter param, KStructNode scope) {
		if (param.formalVar.containsAttribute(KvtCAttributeKey.TYPE)) {
			String allowedTypesAttr = (String) param.formalVar.getAttribute(KvtCAttributeKey.TYPE);
			StringTokenizer tokenizer = new StringTokenizer(allowedTypesAttr, ",");
			KStructType firstAllowedType = null;
			while (tokenizer.hasMoreElements()) {
				String key = ((String) tokenizer.nextElement()).trim();
				KStructType type = KvtUserTypeAdministrator.findType(key, scope);
				if (type != null) {
					if (firstAllowedType == null) {
						firstAllowedType = type;
					}
					if ((type.equals(param.formalType))) {
						return;
					}
				}
			}
			if (firstAllowedType != null) {
				param.formalType = firstAllowedType;
				return;
			}
		}
	}

	/**
	 * creates default parameters for a routine call (necessary variables will
	 * be created in this function)
	 * 
	 * @param routineParameters
	 *            parameters of the routine
	 * @param scope
	 *            scope for variable searches
	 * @param names
	 *            variable prefixes
	 * @return parameter list filled with actual parameters
	 */
	public static Parameter[] getNewParameter(KStructNodeVector routineParameters, KStructProgram scope, String[] names) {
		Enumeration paramList = getParametersOfStatement(routineParameters);
		Vector elem = new Vector();
		int id = 0;
		while (paramList.hasMoreElements()) {
			Object param = paramList.nextElement();
			Parameter p = new Parameter();
			if (param instanceof KStructVar) {
				p.formalVar = (KStructVar) param;
				p.formalName = ((KStructVar) param).getKey();
				p.formalType = ((KStructVar) param).getKStructType();
				checkFormalType(p, scope);

				KStructType t = skipAlias(getDefaultActualParamType(p, scope));
				p.reuse = KvtUserTypeAdministrator.isReuseName(t);
				p.isOptional = ((KStructVar) param).isOptional();

				if (!p.reuse && !p.isOptional && (t != null)) {
					if ((t instanceof KStructTypeBool) || (t instanceof KStructTypeLInt) || (t instanceof KStructTypeReal)
							|| (t instanceof KStructTypeString) || (t instanceof KStructTypeSubrange) || (t instanceof KStructTypeEnum)) {
						if (t instanceof KStructTypeBool) {
							p.actualValue = Boolean.FALSE;
							p.actualName = "FALSE";
							try {
								if (p.formalVar.containsAttribute(KvtCAttributeKey.D_INIT_VALUE)) {
									p.actualValue = new Boolean(Boolean.parseBoolean((String) (p.formalVar
											.getAttribute(KvtCAttributeKey.D_INIT_VALUE))));
									p.actualName = p.actualValue.toString();
								}
							} catch (Exception e) {
								// nothing to do here
							}
						} else if (t instanceof KStructTypeLInt) {
							p.actualValue = new Integer(0);
							p.actualName = "0";
							try {
								if (p.formalVar.containsAttribute(KvtCAttributeKey.D_INIT_VALUE)) {
									p.actualValue = new Integer(Integer.parseInt((String) (p.formalVar
											.getAttribute(KvtCAttributeKey.D_INIT_VALUE))));
									p.actualName = p.actualValue.toString();
								}
							} catch (Exception e) {
								// nothing to do here
							}
						} else if (t instanceof KStructTypeReal) {
							p.actualValue = new Float(0);
							p.actualName = "0.0";
							try {
								if (p.formalVar.containsAttribute(KvtCAttributeKey.D_INIT_VALUE)) {
									p.actualValue = new Float((String) (p.formalVar.getAttribute(KvtCAttributeKey.D_INIT_VALUE)));
									p.actualName = p.actualValue.toString();
								}
							} catch (Exception e) {
								// nothing to do here
							}
						} else if (t instanceof KStructTypeString) {
							p.actualValue = "\"\"";
							p.actualName = "\"\"";
							try {
								if (p.formalVar.containsAttribute(KvtCAttributeKey.D_INIT_VALUE)) {
									p.actualValue = "\"" + ((String) (p.formalVar.getAttribute(KvtCAttributeKey.D_INIT_VALUE))) + "\"";
									p.actualName = p.actualValue.toString();
								}
							} catch (Exception e) {
								// nothing to do here
							}

						} else if (t instanceof KStructTypeSubrange) {
							p.actualValue = ((KStructTypeSubrange) t).getLowerBound();
							p.actualName = p.actualValue.toString();
							try {
								if (p.formalVar.containsAttribute(KvtCAttributeKey.D_INIT_VALUE)) {
									p.actualValue = new Integer(Integer.parseInt((String) (p.formalVar
											.getAttribute(KvtCAttributeKey.D_INIT_VALUE))));
									p.actualName = p.actualValue.toString();
								}
							} catch (Exception e) {
								// nothing to do here
							}

						} else if (t instanceof KStructTypeEnum) {
							p.actualValue = ((KStructTypeEnum) t).constants.getChild(0);
							p.actualName = p.actualValue.toString();
						}
					} else {
						if (!((KStructVar) param).isOptional()) {
							String varPrefix = null;
							if ((names != null) && (names.length > id)) {
								varPrefix = names[id];
								if ((varPrefix == null) || (0 == varPrefix.length()) || varPrefix.equalsIgnoreCase("*")
										|| varPrefix.equalsIgnoreCase("?")) {
									varPrefix = null;
								}
							}
							KStructVar var = null;
							if (!KvtUserTypeAdministrator.isNoVarType(t, scope) && KvtUserTypeAdministrator.getPrefixFor(t) != null) {
								var = createVariable(varPrefix, t, scope, projectScope, false);
								p.actualName = (var != null) ? var.getKey() : null;
								KTcDfl d = dfl;
								if (d != null) {
									p.actualValue = (var != null) ? d.variable.createKStructVarWrapper(var) : null;
								}
								if (var == null) {
									// KvtErrMsgAdministrator.showError(KvtErrMsgAdministrator.ERR_TITLE,
									// KvtErrMsgAdministrator.INS_ERR_MSG,
									// null);
									Log.e("ERROR", "Could not insert!");
								}
							}
							p.createdVar = var;
						}
					}
				} else if ((p.reuse && !p.isOptional && (t != null))) {
					String name = (String) KvtStatementAdministrator.getReuseVariable(p.formalType.getKey());
					if (name != null) {
						Object actualValue = KvtStatementAdministrator.getVaraiblesOrValueForName(name, p.formalType, scope.getUnNamedRoutine());
						if (actualValue != null) {
							p.actualName = name;
							p.actualValue = actualValue;
						}
					}
				}
			}
			elem.addElement(p);
			id++;
		}
		Parameter[] e = new Parameter[elem.size()];
		for (int i = 0; i < elem.size(); i++) {
			e[i] = (Parameter) elem.elementAt(i);
		}
		return e;
	}

	/**
	 * creates a callstatement by adding parameters to the routine specified by
	 * its node
	 * 
	 * @param indentation
	 *            for indenting the statement
	 * @param statement
	 *            routine node
	 * @param params
	 *            actual parameters
	 * @return statement as string
	 */
	public static String createCallStatement(String indentation, KStructNode statement, KvtStatementAdministrator.Parameter[] params) {
		String paramStr = "";
		for (int i = 0; i < params.length; i++) {
			paramStr += (params[i].actualName != null) ? params[i].actualName : "";
			if (i < params.length - 1) {
				paramStr += SEPARATOR + " ";
			}
		}
		while (paramStr.trim().endsWith(",")) {
			paramStr = paramStr.trim();
			paramStr = paramStr.substring(0, paramStr.length() - 1);
		}

		String statementName = statement.getKey();
		if (statementName.equals(UNNAMED)) {
			statementName = statement.getParent().getKey();
		} else if ((statement instanceof KStructRoutine) && !((KStructRoutine) statement).isGlobal()) {
			// routine in program.routine notation
			statementName = statement.getParent().getKey() + "." + statement.getKey();
		}
		String callStatement = indentation + statementName + LEFT_BRACKET + paramStr + RIGHT_BRACKET + SEMICOLON + CAR_RET;
		return callStatement;
	}

	/**
	 * Listener-Methode für den KStructAdministrator aus der Daten- und
	 * Funktionsschicht: Muss implementiert werden, hat aber in dieser Klasse
	 * keine Bedeutung
	 * 
	 * @param parent
	 */
	public void treeChanged(KStructNode parent) {
		if (parent instanceof KStructProject) {
			KvtVarManager.removeAllVariables();
			tableScope = null;
			routineLockUpTable.clear();
		}
		if (parent instanceof KStructScope) {
			KvtUserTypeAdministrator.removeUserTypes();
		}
	}

	/**
	 * Listener-Methode für den KStructAdministrator aus der Daten- und
	 * Funktionsschicht: Wird aufgerufen, wenn ein neuer Strukturknoten erzeugt
	 * wurde. Handelt es sich dabei um einen Variablen-Strukturknoten in der
	 * einzufügenden Routine, so wird dieser gespeicher.
	 * 
	 * @param parent
	 *            Strukturknoten der Routine in die eingefügt wurde
	 * 
	 * @param node
	 *            Strukturknoten der neu hinzugefügt wurde
	 */
	public void nodeInserted(KStructNode parent, KStructNode node) {
		if (node instanceof KStructVar) {
			String prefix = getPrefix((KStructVar) node);
			KvtVarManager.addVariable(prefix, (KStructVar) node);
		} else {
			tableScope = null;
			routineLockUpTable.clear();
		}
	}

	/**
	 * Listener-Methode für den KStructAdministrator aus der Daten- und
	 * Funktionsschicht: Muss implementiert werden, hat aber in dieser Klasse
	 * keine Bedeutung
	 * 
	 * @param parent
	 */
	public void nodeRemoved(KStructNode parent, KStructNode node) {
		if (node instanceof KStructVar) {
			KvtVarManager.removeVariable((KStructVar) node);
		} else {
			tableScope = null;
			routineLockUpTable.clear();
		}
		if (node instanceof KStructProject) {
			KvtVarManager.removeAllVariables();
		}
		if (node instanceof KStructScope) {
			KvtUserTypeAdministrator.removeUserTypes();
		}
	}

	/**
	 * search for a text in all varibles, projects, programs, ... of the current
	 * scope
	 * 
	 * @param text
	 *            text to search
	 * @param scope
	 *            where to search
	 * @return node matching this text or null if none found
	 */
	public static boolean checkInScope(String text, KStructNode scope, boolean checkProjects, boolean caseSensitive) {
		if (checkProjects && (scope instanceof KStructGlobal)) {
			// no project or program with the name text is allowed
			KvtProject[] projects = KvtProjectAdministrator.getAllProjects();
			for (int i = 0; i < projects.length; i++) {
				KvtProject proj = projects[i];
				if (!proj.isGlobalProject() && !proj.isSystemProject()) {
					if (checkName(proj.getStructProject(), text)/*
																 * proj.toString(
																 * )
																 * .equalsIgnoreCase
																 * (text)
																 */) {
						return true;
					}
					for (int j = 0; j < proj.getProgramCount(); j++) {
						KvtProgram p = proj.getProgram(j);
						if (checkName(p.getStructProgram(), text)/*
																 * p.toString().
																 * equalsIgnoreCase
																 * (text)
																 */) {
							return true;
						}
					}
				}
			}
		}
		KStructNode nd = null;
		if (scope instanceof KStructRoutine) {
			nd = checkInChildren(((KStructRoutine) scope).variables, text, caseSensitive, false);
			if (nd != null) {
				return true;
			}
			nd = checkInChildren(((KStructRoutine) scope).parameters, text, caseSensitive, false);
			if (nd != null) {
				return true;
			}
			scope = scope.getParent();
		}
		if (scope instanceof KStructProgram) {
			nd = checkProgramOrUnit(scope, text, false, caseSensitive);
			if (nd != null) {
				return true;
			}
			scope = scope.getParent();
		}
		while (!(scope instanceof KStructRoot)) {
			nd = checkProject(scope, text, caseSensitive);
			if (nd != null) {
				return true;
			}
			scope = scope.getParent();
		}
		return KEditKW.isReserved(text, true);
	}

	private static KStructNode checkProject(KStructNode prj, String key, boolean caseSensitive) {
		if (prj instanceof KStructProject) {
			KStructNode nd = checkInChildren(((KStructProject) prj).programs, key, false, false);
			if (nd != null) {
				return nd;
			}
			nd = checkInChildren(((KStructProject) prj).units, key, false, false);
			if (nd != null) {
				return nd;
			}
			nd = checkInChildren(((KStructProject) prj).variables, key, caseSensitive, true);
			if (nd != null) {
				return nd;
			}
			nd = checkInChildren(((KStructProject) prj).routines, key, caseSensitive, true);
			if (nd != null) {
				return nd;
			}
			nd = checkInChildren(((KStructProject) prj).types, key, caseSensitive, true);
			if (nd != null) {
				return nd;
			}
			nd = checkEnumerations(((KStructProject) prj).types, key, caseSensitive);
			if (nd != null) {
				return nd;
			}
			nd = checkInChildren(((KStructProject) prj).constants, key, caseSensitive, true);
			if (nd != null) {
				return nd;
			}
		}
		return null;
	}

	private static boolean checkName(KStructNode node, String key) {
		if (node != null) {
			// if (KvtSystem.VARIABLES_TO_UPPER_CASE) {
			// return key.equals(node.getKey());
			// }
			return key.equalsIgnoreCase(node.getKey());
		}
		return false;
	}

	private static KStructNode checkProgramOrUnit(KStructNode prg, String key, boolean onlyGlobals, boolean caseSensitive) {
		if (checkName(prg, key)) {
			return prg;
		}
		if (prg instanceof KStructProgram) {
			KStructNode nd = checkInChildren(((KStructProgram) prg).variables, key, caseSensitive, onlyGlobals);
			if (nd != null) {
				return nd;
			}
			nd = checkInChildren(((KStructProgram) prg).routines, key, caseSensitive, onlyGlobals);
			if (nd != null) {
				return nd;
			}
			nd = checkInChildren(((KStructProgram) prg).types, key, caseSensitive, onlyGlobals);
			if (nd != null) {
				return nd;
			}
			nd = checkEnumerations(((KStructProgram) prg).types, key, caseSensitive);
			if (nd != null) {
				return nd;
			}
			nd = checkInChildren(((KStructProgram) prg).constants, key, caseSensitive, onlyGlobals);
			if (nd != null) {
				return nd;
			}
		}
		return null;
	}

	private static KStructNode checkEnumerations(KStructNodeVector types, String key, boolean caseSensitive) {
		if (types != null) {
			for (int i = 0; i < types.getChildCount(); i++) {
				KStructType t = (KStructType) types.getChild(i);
				if (t instanceof KStructTypeEnum) {
					KStructNode nd = checkInChildren(((KStructTypeEnum) t).constants, key, caseSensitive, false);
					if (nd != null) {
						return nd;
					}
				}
			}
		}
		return null;
	}

	private static KStructNode checkInChildren(KStructNodeVector v, String key, boolean checkCase, boolean onlyGlobals) {
		if (v != null) {
			for (int i = 0; i < v.getChildCount(); i++) {
				KStructNode child = v.getChild(i);
				if ((!onlyGlobals) || (child.isGlobal())) {
					if (checkCase) {
						if (key.equals(child.getKey())) {
							return child;
						}
					} else {
						if (checkName(child, key)) {
							return child;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * extract actual parameter list of call statement
	 * 
	 * @param callStatement
	 *            apecific call statement
	 * @return parameters as string array
	 */
	public static String[] getParameterInCallStatement(String callStatement) {
		int beg = callStatement.indexOf("(");
		int end = callStatement.lastIndexOf(")");
		if (beg < end) {
			String parameters = callStatement.substring(beg + 1, end).trim();
			if (0 < parameters.length()) {
				Vector names = new Vector(5);
				beg = 0;
				int pos = beg;
				while (beg < parameters.length()) {
					int sBeg = parameters.indexOf('"', pos);
					int sEnd = parameters.length() + 1;
					if (sBeg < 0) {
						sBeg = parameters.length() + 1;
					} else {
						sEnd = parameters.indexOf('"', sBeg + 1);
						if (sEnd < 0) {
							sEnd = parameters.length();
						}
					}
					end = parameters.indexOf(',', pos);
					if (end < 0) {
						end = parameters.length();
					}
					int left = parameters.indexOf('(', pos);
					if (left < 0) {
						left = parameters.length() + 1;
					}
					if ((sBeg < end) && (sBeg < left)) {
						// skip string
						pos = sEnd + 1;
					} else if ((end < sBeg) && (end < left)) {
						// parameter found
						String txt = parameters.substring(beg, end).trim();
						names.addElement(txt);
						beg = end + 1;
						pos = beg;
					} else {
						// ((left < end) && (left < sBeg))
						// parameter is routine call, skip it
						int count = 1;
						int p_pos = left + 1;
						while ((0 < count) && (p_pos < parameters.length())) {
							int p_sBeg = parameters.indexOf('"', p_pos);
							int p_sEnd = parameters.length() + 1;
							if (p_sBeg < 0) {
								p_sBeg = parameters.length() + 1;
							} else {
								p_sEnd = parameters.indexOf('"', p_sBeg + 1);
								if (p_sEnd < 0) {
									p_sEnd = parameters.length();
								}
							}
							int right = parameters.indexOf(')', p_pos);
							if (right < 0) {
								right = parameters.length();
							}
							int nextLeft = parameters.indexOf('(', p_pos);
							if (nextLeft < 0) {
								nextLeft = parameters.length() + 1;
							}
							if ((p_sBeg < right) && (p_sBeg < nextLeft)) {
								// skip string
								p_pos = p_sEnd + 1;
							} else if ((right < p_sBeg) && (right < nextLeft)) {
								count--;
								p_pos = right + 1;
							} else {
								// parameter is routine call, skip it
								count++;
								p_pos = nextLeft + 1;
							}
						}
						pos = p_pos;
					}
				}
				if (0 < names.size()) {
					String[] n = new String[names.size()];
					for (int i = 0; i < names.size(); i++) {
						n[i] = (String) names.elementAt(i);
					}
					return n;
				}
			}
		}
		return new String[0];
	}

	/**
	 * extract routine from a callstatement
	 * 
	 * @param callStatement
	 *            specific call statement
	 * @param scope
	 *            where to search for the routine
	 * @return routine node or null if not found
	 */
	public static KStructRoutine getRoutineInCallStatement(String callStatement, KStructRoutine scope) {
		KStructRoutine rt = null;
		int index = 0;
		index = callStatement.indexOf("(");
		if (index > 0) {
			String routine = callStatement.substring(0, index).trim();
			rt = getRoutineInScope(routine, scope);
		}
		return rt;
	}

	/**
	 * fill in Parameter nodes from kstructnodevector definitions
	 * 
	 * @param routineParameters
	 *            defines parameters
	 * @param names
	 *            default names
	 * @param scope
	 *            scope where to search for matches
	 * @return Parameters array (@link KvtStatementAdministrator#Parameter)
	 */
	public static Parameter[] getParameters(KStructNodeVector routineParameters, String[] names, KStructRoutine scope) {
		int parmCount = routineParameters.getChildCount();
		Parameter[] params = new Parameter[parmCount];
		for (int i = 0; i < parmCount; i++) {
			params[i] = new Parameter();
			params[i].formalVar = (routineParameters != null) ? (KStructVar) routineParameters.getChild(i) : null;
			params[i].formalName = (params[i].formalVar != null) ? params[i].formalVar.getKey() : "";
			KStructType t = (params[i].formalVar != null) ? params[i].formalVar.getKStructType() : null;
			while (t instanceof KStructTypeMapTo) {
				t = t.getKStructType();
			}
			params[i].formalType = t;
			params[i].isOptional = (params[i].formalVar != null) ? params[i].formalVar.isOptional() : false;
			params[i].reuse = KvtUserTypeAdministrator.isReuseName(params[i].formalType);
			params[i].actualName = (names.length > i) ? names[i] : null;
			params[i].actualValue = (names.length > i) ? getVaraiblesOrValueForName(names[i], t, scope) : null;
		}
		return params;
	}

	/**
	 * search for routine node specified by its name
	 * 
	 * @param name
	 *            routine name
	 * @param scope
	 *            where to search
	 * @return routine node or null
	 */
	public static KStructRoutine getRoutineInScope(String name, KStructRoutine scope) {
		KStructProgram p = (KStructProgram) scope.getParent();
		if ((tableScope == null) || !p.equals(tableScope)) {
			routineLockUpTable.clear();
			tableScope = p;
			buildRoutineTable(tableScope, routineLockUpTable);
		}
		return (KStructRoutine) routineLockUpTable.get(name);
	}

	private static void buildRoutineTable(KStructNode scope, Hashtable table) {
		if (scope instanceof KStructRoot) {
			addRoutineTable("", ((KStructRoot) scope).routines, false, table);
			return;
		}
		if (scope instanceof KStructProject) {
			buildRoutineTable(scope.getParent(), table);
			addRoutineTable("", ((KStructProject) scope).routines, false, table);
		} else if (scope instanceof KStructProgram) {
			buildRoutineTable(scope.getParent(), table);
			KStructProgram p = (KStructProgram) scope;
			addRoutineTable(p.getKey() + ".", p.routines, true, table);
		}
	}

	private static void addRoutineTable(String prefix, KStructNodeVector routines, boolean all, Hashtable table) {
		for (int i = 0; i < routines.getChildCount(); i++) {
			KStructRoutine r = (KStructRoutine) routines.getChild(i);
			if (all || !r.isPrivate()) {
				if (!r.isNamedRoutine()) {
					table.put(r.getParent().getKey(), r);
				} else if (all || r.isGlobal()) {
					table.put(r.getKey(), r);
				} else {
					table.put(prefix + r.getKey(), r);
				}
			}
		}
	}

	/**
	 * intelligently search for an Object matching formalType/name
	 * 
	 * @param name
	 *            constants (like TRUE, FALSE, ...), variable names, ...
	 * @param formalType
	 *            formal type to match
	 * @param scope
	 *            where to search
	 * @return constant or variable or routine or ...
	 */
	public static Object getVaraiblesOrValueForName(String name, KStructType formalType, KStructRoutine scope) {
		KTcDfl d = dfl;
		if ((name.length() == 0) || (d == null)) {
			return null;
		}
		if (formalType instanceof KStructTypeBool) {
			if ("TRUE".equals(name)) {
				return Boolean.TRUE;
			} else if ("FALSE".equals(name)) {
				return Boolean.FALSE;
			}
		} else if (formalType instanceof KStructTypeString) {
			if ((name.charAt(0) == '"') && (name.charAt(name.length() - 1) == '"')) {
				return name;
			}
		} else if (formalType instanceof KStructTypeLInt) {
			if (formalType instanceof KStructTypeDWord) {
				// 32 Bit
				try {
					return DWord.valueOf(name);
				} catch (Exception e) {
					// ignore
				}
			} else if (formalType instanceof KStructTypeDInt) {
				// 32 Bit
				try {
					return Integer.valueOf(name);
				} catch (Exception e) {
					// ignore
				}
			} else {
				// 64 Bit
				try {
					return Long.valueOf(name);
				} catch (Exception e) {
					// ignore
				}
			}
		} else if (formalType instanceof KStructTypeReal) {
			try {
				return Float.valueOf(name);
			} catch (Exception e) {
				// ignore
			}
		} else if (formalType instanceof KStructTypeEnum) {
			KStructNode n = checkInChildren(((KStructTypeEnum) KvtStatementAdministrator.skipAlias(formalType)).constants, name, true, false);
			if (n != null) {
				return n;
			}
		} else if (formalType instanceof KStructTypeSubrange) {
			try {
				Integer value = Integer.valueOf(name);
				Integer lowerBound = ((KStructTypeSubrange) formalType).getLowerBound();
				Integer upperBound = ((KStructTypeSubrange) formalType).getUpperBound();
				if ((lowerBound != null) && (upperBound != null) && (lowerBound.intValue() <= value.intValue())
						&& (value.intValue() <= upperBound.intValue())) {
					return value;
				}
			} catch (Exception e) {
				// ignore
			}
		}

		KStructProject project = scope.getKStructProject();
		int index = name.indexOf('.');
		int bracket = name.indexOf('[');
		int parentheses = name.indexOf('(');
		if (index < 0) {
			index = name.length();
		}
		if ((0 < bracket) && (bracket < index)) {
			index = bracket;
		}
		if ((0 < parentheses) && (parentheses < index)) {
			index = parentheses;
		}
		String varName = name;
		if (0 < index) {
			// maybe a program
			String progName = name.substring(0, index);
			if (progName != null) {
				progName = progName.trim();
			}
			KStructProgram p = (KStructProgram) checkInChildren(project.programs, progName, false, false);
			if ((p == null) && !(project instanceof KStructGlobal)) {
				p = (KStructProgram) checkInChildren(project.getKStructProject().programs, progName, false, false);
			}

			if (p != null) {
				String keyPath = p.getPath() + ".";
				int beg = index + 1;
				int end = name.indexOf('.', beg);
				bracket = name.indexOf('[', beg);
				parentheses = name.indexOf('(');
				if (end < 0) {
					end = name.length();
				}
				if ((0 < bracket) && (bracket < end)) {
					end = bracket;
				}
				if ((0 < parentheses) && (parentheses < end)) {
					end = parentheses;
				}
				if (beg > end) {
					varName = "";
				} else {
					varName = name.substring(beg, end);
				}
				KStructVarWrapper var = d.variable.createKStructVarWrapper(keyPath + varName);
				if (var != null) {
					return var;
				}
				// maybe a constant
				KStructNode constant = checkInChildren(p.constants, varName, true, false);
				if (constant != null) {
					return constant;
				}
				if (((varName == null) || (varName.trim().equals(""))) && (formalType == null)) { // was
					// a
					// program
					// node
					return p;
				}

				// maybe a routine specified in program.routine notation
				for (int i = 0; i < p.routines.getChildCount(); i++) {
					KStructRoutine elem = (KStructRoutine) p.routines.getChild(i);
					if (elem.getKey().equals(varName)) {
						return elem;
					}
				}

			} else {
				// maybe a routine
				KStructRoutine r = getRoutineInScope(progName, scope);
				if (r == null) { // unit.routine
					KStructProject n = scope.getKStructProject();
					while ((n != null) && (r == null)) {
						KStructTypeUnit u = (KStructTypeUnit) checkInChildren(n.units, progName, false, true);
						if ((u != null) && (index + 1 < parentheses)) {
							varName = name.substring(index + 1, parentheses);
							while ((u != null) && (r == null)) {
								r = (KStructRoutine) checkInChildren(u.routines, varName, true, false);
								u = u.getBaseUnit();
							}
						}
						n = (KStructProject) n.getParent();
					}
				}
				if (r != null) {
					return r;
				}
			}
			// wasn't a program
			varName = progName;
		} else {
			index = name.indexOf('[');
			parentheses = name.indexOf('(');
			if ((0 < parentheses) && (parentheses < index)) {
				index = parentheses;
			}
			if (0 < index) {
				varName = name.substring(0, index);
			}
		}
		KStructNode sn = scope.getParent();
		KStructNode var = null;
		while ((var == null) && (sn != null)) {
			if (sn instanceof KStructProgram) {
				var = checkInChildren(((KStructProgram) sn).variables, varName, true, false);
			} else if (sn instanceof KStructProject) {
				var = checkInChildren(((KStructProject) sn).variables, varName, true, true);
				if (var != null) {
					break;
				}
			}
			sn = sn.getParent();
		}
		if (var != null) {
			KStructNode parent = var.getParent();
			String keyPath = parent.getPath() + ".";
			parentheses = name.indexOf('(');
			if ((0 < index) && (index < name.length()) && (0 < parentheses)) {
				// must be a routine call
				index = name.lastIndexOf('.', parentheses);
				if (index != -1) {
					varName = name.substring(0, index);
				}
				KStructVarWrapper w = d.variable.createKStructVarWrapper(keyPath + varName);
				if (w != null) {
					String routineName = name.substring(index + 1, parentheses).trim();
					KStructType t = skipAlias(w.getKStructType());
					if (t instanceof KStructTypeUnit) {
						KStructRoutine r = (KStructRoutine) ((KStructTypeUnit) t).routines.getChild(routineName, false);
						while ((r == null) && (((KStructTypeUnit) t).getBaseUnit() != null)) {
							t = skipAlias(((KStructTypeUnit) t).getBaseUnit());
							r = (KStructRoutine) ((KStructTypeUnit) t).routines.getChild(routineName, false);
						}
						return r;
					} else if (t instanceof KStructTypeRoutine) {
						return t;
					}
				}
				return null;
			}
			return d.variable.createKStructVarWrapper(keyPath + name);
		}
		// maybe a constant value
		sn = scope.getParent();
		KStructConst c = null;
		while ((c == null) && (sn != null)) {
			if (sn instanceof KStructProgram) {
				c = (KStructConst) checkInChildren(((KStructProgram) sn).constants, varName, true, false);
			} else if (sn instanceof KStructProject) {
				for (int i = 0; i < ((KStructProject) sn).programs.getChildCount(); i++) {
					KStructProgram p = (KStructProgram) ((KStructProject) sn).programs.getChild(i);
					c = (KStructConst) checkInChildren(p.constants, varName, true, true);
					if (c != null) {
						break;
					}
				}
			}
			sn = sn.getParent();
		}
		if (c != null) {
			return c;
		}
		// maybe a constant value of a type enumeration
		sn = scope.getParent();
		while ((c == null) && (sn != null)) {
			if (sn instanceof KStructProgram) {
				c = (KStructConst) checkInTypeEnums(((KStructProgram) sn).types, varName);
			} else if (sn instanceof KStructProject) {
				for (int i = 0; i < ((KStructProject) sn).programs.getChildCount(); i++) {
					KStructProgram p = (KStructProgram) ((KStructProject) sn).programs.getChild(i);
					c = (KStructConst) checkInTypeEnums(p.types, varName);
					if (c != null) {
						break;
					}
				}
			}
			sn = sn.getParent();
		}
		return c;
	}

	/**
	 * if type is an alias type return base type. otherwise return type itself
	 * 
	 * @param type
	 *            type to convert
	 * @return type or its base type
	 */
	public static KStructType skipAlias(KStructType type) {
		while ((type != null) && type.isAliasType()) {
			type = type.getKStructType();
		}
		return type;
	}

	/**
	 * seraches for name in Enum constants
	 * 
	 * @param types
	 *            where to search
	 * @param name
	 *            what to search for
	 * @return node matching name
	 */
	private static KStructNode checkInTypeEnums(KStructNodeVector types, String name) {
		for (int i = 0; i < types.getChildCount(); i++) {
			KStructNode t = types.getChild(i);
			if (t instanceof KStructTypeEnum) {
				KStructNodeVector constants = ((KStructTypeEnum) t).constants;
				for (int j = 0; j < constants.getChildCount(); j++) {
					KStructNode c = constants.getChild(j);
					if (name.equals(c.getKey())) {
						return c;
					}
				}
			}
		}
		return null;
	}

	/**
	 * describes a routine parameter
	 */
	public static class Parameter {
		public String		formalName;
		public KStructVar	formalVar;
		public KStructType	formalType;
		public String		actualName;
		public Object		actualValue;
		public KStructVar	createdVar;
		public boolean		reuse;
		public boolean		isOptional;
	}

}