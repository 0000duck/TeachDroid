/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Project : KEMRO.teachview.4
 *    Author  : mau
 *    Date    : 26.01.2005
 *------------------------------------------------------------------------*//*
 */
package com.keba.kemro.kvs.teach.data.program;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.Vector;

import com.keba.kemro.kvs.teach.constant.KvtCAttributeKey;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.teach.dfl.structural.KStructNode;
import com.keba.kemro.teach.dfl.structural.KStructNodeVector;
import com.keba.kemro.teach.dfl.structural.KStructProgram;
import com.keba.kemro.teach.dfl.structural.KStructProject;
import com.keba.kemro.teach.dfl.structural.routine.KStructRoutine;
import com.keba.kemro.teach.dfl.structural.type.KStructType;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeDInt;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeEnum;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeSubrange;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeUnit;
import com.keba.kemro.teach.dfl.value.KStructVarWrapper;

/**
 * manages the usertype.properties file
 */
public class KvtUserTypeAdministrator {
	private static Vector varUserTypes;
	private static Vector userTypes;
	private static Vector defaultTypes;

	/**
	 * describes a usertype with the configured parameters
	 */
	public static class UserType {
		static Hashtable table = new Hashtable(11);
		private static Vector simpleTypes = new Vector();
		public String key;
		private boolean isNoVar;
		public boolean simpleType;
		public String noVarKey;
		public boolean isReUse;
		public boolean isDefault;
		public String prefix;

		public UserType() {

		}

		protected UserType(String keyType, String keyPrefix) {
			key = keyType;
			simpleType = true;
			try {
				prefix = "_var_";// Config.getStringProperty(keyPrefix);
			} catch (MissingResourceException me) {
				// ignore
			}
			simpleTypes.addElement(this);
		}

		public boolean isNoVar() {
			if (noVarKey != null) {
				Object val = table.get(noVarKey);
				if (val instanceof Boolean) {
					return ((Boolean) val).booleanValue();
				} else {
					KStructVarWrapper wrap = KvtSystemCommunicator.getTcDfl().variable.createKStructVarWrapper(noVarKey);
					if (wrap != null) {
						Object o = wrap.readActualValue(null);
						if (o instanceof Boolean) {
							table.put(noVarKey, o);
							return ((Boolean) o).booleanValue();
						}
					}
				}
			}
			return isNoVar;
		}
	}

	/**
	 * get matching usertypes for a given formal parameter type (used in @link
	 * KvtParameterController)
	 * 
	 * @param formalParamType
	 *            formal parameter
	 * @param scope
	 *            scope to search for types
	 * @return vector of strings
	 */
	public static Vector getUserTypes(KStructType formalParamType, KStructNode scope, boolean showNoVars) {
		if (formalParamType == null) {
			return getUserTypes();
		}
		if (userTypes == null) {
			loadUserTypes();
		}
		return getUserTypes(userTypes, formalParamType, scope, showNoVars);
	}

	public static Vector getNewVarUserTypes(KStructType formalParamType, KStructNode scope) {
		if (formalParamType == null) {
			return getNewVarUserTypes();
		}
		if (userTypes == null) {
			loadUserTypes();
		}
		return getUserTypes(varUserTypes, formalParamType, scope, false);
	}

	public static Vector getUserTypes(Vector data, KStructType formalParamType, KStructNode scope, boolean showNoVars) {
		Vector types = new Vector();
		if (data == null) { // on error return empty set
			return types;
		}

		if (!(formalParamType instanceof KStructTypeUnit)) {
			String name = formalParamType.getKey();
			boolean found = false;
			for (int i = 0; i < data.size(); i++) {
				UserType t = (UserType) data.elementAt(i);
				if (!t.isNoVar() || showNoVars) {
					if (t.key.equalsIgnoreCase(name) || (formalParamType.getKey().equalsIgnoreCase("ANY"))) {
						types.addElement(t.key);
						found = true;
					} else if (formalParamType instanceof KStructTypeDInt) {
						KStructType type = findType(t.key, scope);
						if (type instanceof KStructTypeEnum) {
							found = true;
							types.addElement(t.key);
						} else if (type instanceof KStructTypeSubrange) {
							found = true;
							types.addElement(t.key);
						}
					}
				}
			}
			if (found) {
				return types;
			}
			if (formalParamType.isAliasType()) {
				return getUserTypes(formalParamType.getKStructType(), scope, showNoVars);
			}

		} else if (scope != null) {
			KStructProject project = (scope instanceof KStructProject) ? (KStructProject) scope : scope.getKStructProject();
			while (project != null) {
				// search for inherited units
				for (int i = 0; i < project.units.getChildCount(); i++) {
					KStructTypeUnit unit = (KStructTypeUnit) project.units.getChild(i);
					KStructTypeUnit baseUnit = unit;
					while ((baseUnit != null) && !baseUnit.equals(formalParamType)) {
						baseUnit = baseUnit.getBaseUnit();
					}
					if (baseUnit != null) {
						String tName = unit.getKey();
						for (int j = 0; j < data.size(); j++) {
							UserType t = (UserType) data.elementAt(j);
							if (!t.isNoVar() || showNoVars) {
								if (t.key.equalsIgnoreCase(tName)) {
									types.addElement(t.key);
								}
							}
						}
					}
				}
				project = (KStructProject) project.getParent();
			}
		}
		return types;
	}

	/**
	 * gets the configured prefix for a type
	 * 
	 * @param t
	 *            type
	 * @return prefix as string
	 */
	public static String getPrefixFor(KStructType t) {
		if (userTypes == null) {
			loadUserTypes();
		}
		if (userTypes != null) {
			Enumeration e = userTypes.elements();
			while (e.hasMoreElements()) {
				UserType elem = (UserType) e.nextElement();
				if (elem.key.equalsIgnoreCase(t.getKey()) && elem.prefix != null) {
					// if (KvtSystem.VARIABLES_TO_UPPER_CASE) {
					// return elem.prefix.trim().toUpperCase();
					// }
					return elem.prefix.trim();
				}
			}
		}
		return null;
	}

	/**
	 * gets all user types
	 * 
	 * @param showNoVars
	 *            include noVar types ?
	 * @return vector of strings
	 */
	public static Vector getUserTypes(boolean showNoVars) {
		if (userTypes == null) {
			loadUserTypes();
		}
		return getUserTypes(userTypes, showNoVars);
	}

	private static Vector getUserTypes(Vector data, boolean showNoVars) {
		Vector types = new Vector();
		UserType.table.clear();
		if (data != null) {
			Enumeration e = data.elements();
			while (e.hasMoreElements()) {
				UserType elem = (UserType) e.nextElement();
				if (showNoVars || !elem.isNoVar()) {
					types.addElement(elem.key);
				}
			}
		}
		return types;
	}

	/**
	 * gets all user types available for new variables
	 * 
	 * @param showNoVars
	 *            include noVar types ?
	 * @return vector of strings
	 */
	public static Vector getNewVarUserTypes(boolean showNoVars) {
		if (userTypes == null) {
			loadUserTypes();
		}
		return getUserTypes(varUserTypes, showNoVars);
	}

	/**
	 * returns all usertypes
	 * 
	 * @return vector of strings
	 */
	public static Vector getUserTypes() {
		return getUserTypes(true);
	}

	/**
	 * returns all usertypes
	 * 
	 * @return vector of strings
	 */
	public static Vector getSortedUserTypes() {
		if (userTypes == null) {
			loadUserTypes();
		}
		SortVector types = new SortVector();
		UserType.table.clear();
		if (userTypes != null) {
			Enumeration e = userTypes.elements();
			while (e.hasMoreElements()) {
				UserType elem = (UserType) e.nextElement();
				if (!elem.simpleType) {
					types.addElement(elem.key);
				}
			}
			types.sort();
			for (int i = 0; i < UserType.simpleTypes.size(); i++) {
				types.insertElementAt(((UserType)UserType.simpleTypes.elementAt(i)).key, i);
			}
		}
		return types;
	}

	/**
	 * returns all usertypes except abstract units
	 * 
	 * @return vector of strings
	 */
	public static Vector getNewVarUserTypes() {
		return getNewVarUserTypes(true);
	}

	/**
	 * get default types (specified by "default" in usertypes.properties
	 * 
	 * @return vector of strings
	 */
	public static Vector getDefaultTypes() {
		Vector v = new Vector();
		if (defaultTypes == null) {
			loadUserTypes();
		}
		if (defaultTypes != null) {
			Enumeration e = defaultTypes.elements();
			while (e.hasMoreElements()) {
				UserType elem = (UserType) e.nextElement();
				v.addElement(elem.key);
			}
		}
		return v;
	}

	/**
	 * searches a given nodevector for usertypes and adds them to the usertypes
	 * and defaulttypes structures
	 */
	private static void searchUserTypesNodeVect(Vector varUserTypes, Vector userTypes, Vector defaultTypes, KStructNodeVector vect) {
		for (int i = 0; i < vect.getChildCount(); i++) {
			KStructType elem = (KStructType) vect.getChild(i);
			String var_prefix = (String) elem.getAttribute(KvtCAttributeKey.PREFIX);
			UserType u = new UserType();
			u.key = elem.getKey();
			u.prefix = var_prefix;
			if (elem.containsAttribute(KvtCAttributeKey.NOVAR)) {
				u.noVarKey = (String) elem.getAttribute(KvtCAttributeKey.NOVAR);
				u.isNoVar = true;
			}
			if (elem.containsAttribute(KvtCAttributeKey.DEFAULT)) {
				u.isDefault = true;
			}
			if (elem.containsAttribute(KvtCAttributeKey.REUSE)) {
				u.isReUse = true;
			}
			if (u.isDefault) {
				defaultTypes.addElement(u);
			}
			userTypes.addElement(u);
			if (!((elem instanceof KStructTypeUnit) && (((KStructTypeUnit) elem).isAbstract()))) {
				varUserTypes.addElement(u);
			}
		}
	}

	/**
	 * searches for user types using the source code attributes and adds them to
	 * the usertypes and defaulttypes structures
	 */
	private static void searchUserTypes(Vector varUserTypes, Vector userTypes, Vector defaultTypes) {
		KvtProject proj = KvtProjectAdministrator.getGlobalProject();
		// global project
		KStructProject sProj = (proj != null) ? proj.getStructProject() : null;
		if (sProj != null) {
			searchUserTypesNodeVect(varUserTypes, userTypes, defaultTypes, sProj.types);
			searchUserTypesNodeVect(varUserTypes, userTypes, defaultTypes, sProj.units);
		}

		// system project
		proj = KvtProjectAdministrator.getSystemProject();
		sProj = (proj != null) ? proj.getStructProject() : null;
		if (sProj != null) {
			searchUserTypesNodeVect(varUserTypes, userTypes, defaultTypes, sProj.types);
			searchUserTypesNodeVect(varUserTypes, userTypes, defaultTypes, sProj.units);
		}
	}

	private static void getPrimitiveTypes(Vector userTypes) {
		UserType.simpleTypes = new Vector();
		userTypes.addElement(new UserType("BOOL", "VarNamePrefixBOOL"));
		userTypes.addElement(new UserType("REAL", "VarNamePrefixREAL"));
		userTypes.addElement(new UserType("LREAL", "VarNamePrefixLREAL"));
		userTypes.addElement(new UserType("STRING", "VarNamePrefixSTRING"));
		userTypes.addElement(new UserType("DINT", "VarNamePrefixDINT"));
		userTypes.addElement(new UserType("DWORD", "VarNamePrefixDWORD"));
	}

	public static void removeUserTypes() {
		userTypes = null;
		varUserTypes = null;
		defaultTypes = null;
	}

	private static Vector loadUserTypes() {
		if (userTypes == null) {
			try {
				varUserTypes = new Vector();
				userTypes = new Vector();
				defaultTypes = new Vector();
				getPrimitiveTypes(userTypes);
				for (int i = 0; i < userTypes.size(); i++) {
					varUserTypes.addElement(userTypes.elementAt(i));
				}
				searchUserTypes(varUserTypes, userTypes, defaultTypes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return userTypes;
	}

	/**
	 * searches for a type in scope
	 * 
	 * @param typeName
	 *            type to search
	 * @param scope
	 *            scope where to search
	 * @return type node
	 */
	public static KStructType findType(String typeName, KStructNode scope) {
		if (scope instanceof KStructProgram) {
			KStructProgram p = (KStructProgram) scope;
			for (int i = 0; i < p.types.getChildCount(); i++) {
				KStructType elem = (KStructType) p.types.getChild(i);
				if (elem.getKey().equalsIgnoreCase(typeName)) {
					return elem;
				}
			}
			return findType(typeName, p.getParent());
		} else if (scope instanceof KStructProject) {
			KStructProject p = (KStructProject) scope;
			for (int i = 0; i < p.types.getChildCount(); i++) {
				KStructType elem = (KStructType) p.types.getChild(i);
				if (elem.getKey().equalsIgnoreCase(typeName)) {
					return elem;
				}
			}
			for (int i = 0; i < p.units.getChildCount(); i++) {
				KStructType elem = (KStructType) p.units.getChild(i);
				if (elem.getKey().equalsIgnoreCase(typeName)) {
					return elem;
				}
			}
			return findType(typeName, p.getParent());
		} else if (scope instanceof KStructRoutine) {
			return findType(typeName, scope.getParent());
		}
		return null;
	}

	/**
	 * @param type
	 *            user type as node
	 * @return true if this is a reuse name
	 */
	public static boolean isReuseName(KStructType type) {
		if (userTypes == null) {
			loadUserTypes();
		}
		if ((userTypes != null) && (type != null)) {
			Enumeration e = userTypes.elements();
			while (e.hasMoreElements()) {
				UserType elem = (UserType) e.nextElement();
				if (elem.key.equalsIgnoreCase(type.getKey())) {
					return elem.isReUse;
				}
			}
		}
		return false;
	}

	/**
	 * @param t
	 * @param scope
	 * @return true if no variable should be created by the user for those types
	 */
	public static boolean isNoVarType(KStructType type, KStructProgram scope) {
		if (userTypes == null) {
			loadUserTypes();
		}
		if ((userTypes != null) && (type != null)) {
			Enumeration e = userTypes.elements();
			while (e.hasMoreElements()) {
				UserType elem = (UserType) e.nextElement();
				if (elem.key.equalsIgnoreCase(type.getKey())) {
					return elem.isNoVar();
				}
			}
		}
		return false;
	}

	private static class SortVector extends Vector {

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
						if (((String) elementData[pointer]).compareTo(((String) elementData[gap + pointer])) > 0) {
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
}
