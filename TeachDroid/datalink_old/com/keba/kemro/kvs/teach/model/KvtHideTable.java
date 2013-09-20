package com.keba.kemro.kvs.teach.model;

import java.util.StringTokenizer;
import java.util.Vector;

import com.keba.kemro.kvs.teach.constant.KvtCAttributeKey;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.structural.KStructNode;
import com.keba.kemro.teach.dfl.structural.KStructProgram;
import com.keba.kemro.teach.dfl.structural.routine.KStructRoutine;
import com.keba.kemro.teach.dfl.structural.type.KStructType;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeMapTo;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeUnit;
import com.keba.kemro.teach.dfl.structural.var.KStructVar;
import com.keba.kemro.teach.dfl.structural.var.KStructVarUnit;
import com.keba.kemro.teach.dfl.value.KStructVarWrapper;

public class KvtHideTable {
	private Vector				variables;
	private KStructVarWrapper	hideVariable;
	private KStructRoutine		hideRoutine;
	private Object				hideValue;

	private static String getPath(KStructVar var) {
		if (var != null) {
			return var.getPath();
		}
		return null;
	}

	private static KStructType skipMapToType(KStructType type) {
		if (type instanceof KStructTypeMapTo) {
			return skipMapToType(((KStructTypeMapTo) type).getKStructType());
		}
		return type;
	}

	public static KvtHideTable checkInstanceVariable(KStructVarWrapper var, boolean checkMyself) {
		try {

			if (var != null) {
				KStructVar variable = var.getKStructVar();
				String path = var.getRootPathString();
				if (path == null) {
					path = getPath(variable);
				} else {
					KStructVar rt = var.getRootVariable();
					if (rt != null) {
						String rootVarName = rt.getKey();
						String rp = getPath(rt);
						if ((rp != null) && (path.startsWith(rootVarName) && (rp.endsWith(rootVarName)))) {
							path = rp + path.substring(rootVarName.length());
						}
					}
				}
				String attr = null;
				if (variable != null) {
					attr = (String) variable.getAttribute(KvtCAttributeKey.HIDE);
				}
				String typeAttr = null;
				if (var.isMapTo()) {
					KStructType maptoType = skipMapToType(var.getKStructType());
					if (maptoType != null) {
						typeAttr = (String) maptoType.getAttribute(KvtCAttributeKey.HIDE);
					}
				}
				if (typeAttr == null) {
					typeAttr = (String) var.getKStructType().getAttribute(KvtCAttributeKey.HIDE);
				}
				if (attr == null) {
					attr = typeAttr;
				}
				if (attr != null) {
					if (checkMyself && (attr.indexOf(";") >= 0)) { // ; means
						// component
						// hiding
						return null;
					}
					return new KvtHideTable(attr, typeAttr, path);
				}
			}
		} catch (Exception e) {
			// nothing to do
		}
		return null;
	}

	public static KvtHideTable checkVariable(KStructVar var) {
		try {
			if (var != null) {
				String attr = (String) var.getKStructType().getAttribute(KvtCAttributeKey.HIDE);
				String typeAttr = (String) var.getAttribute(KvtCAttributeKey.HIDE);
				if (attr == null) {
					attr = typeAttr;
				}
				if (attr != null) {
					KvtHideTable table = new KvtHideTable(attr, typeAttr, getPath(var));
					return table;
				}
			}
		} catch (Exception e) {
			// nothing to do
		}
		return null;
	}

	public static KvtHideTable checkType(KStructType type) {
		try {
			if (type != null) {
				String attr = (String) type.getAttribute(KvtCAttributeKey.HIDE);
				if (attr != null) {
					return new KvtHideTable(attr, null, null);
				}
			}
		} catch (Exception e) {
			// nothing to do
		}
		return null;
	}

	public KStructVarWrapper getHideVariable() {
		doCheck();
		return hideVariable;
	}

	public Object getHideValue() {
		if (hideValue == null) {
			doCheck();
		}
		return hideValue;
	}

	private String getVariablePath(String path, String attribute) {
		if (attribute != null) {
			int pos = attribute.indexOf(';');
			String variableName = attribute;
			if (pos > 0) {
				variableName = attribute.substring(pos + 1).trim();
			}
			if (path != null) {
				int selfindex = variableName.indexOf("SELF.");
				if (selfindex == 0) {
					int len = 5;
					if (!path.endsWith(".")) {
						len = 4;
					}
					variableName = (path + variableName.substring(len)).trim();
				}
			}
			return variableName;
		}
		return null;

	}

	private KStructRoutine getHideRoutine(String variableName) {
		doCheck();
		KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
		if ((dfl != null) && (variableName != null) && (variableName.endsWith("()"))) {
			int pos = variableName.lastIndexOf('.');
			if (pos > 0) {
				String routineKey = variableName.substring(pos + 1, variableName.length() - 2).trim();
				variableName = variableName.substring(0, pos).trim();
				KStructNode node = dfl.structure.getKStructNode(variableName);
				if (node instanceof KStructProgram) {
					return (KStructRoutine) ((KStructProgram) node).routines.getChild(routineKey, true);
				} else if (node instanceof KStructVarUnit) {
					KStructTypeUnit type = (KStructTypeUnit) skipMapToType(((KStructVarUnit) node).getKStructType());
					return (KStructRoutine) type.routines.getChild(routineKey, true);
				}
			}
		}
		return null;
	}

	private String	m_attr;
	private String	m_typeAttr;
	private String	m_path;
	private boolean	hideChecked;

	private KvtHideTable(String attr, String typeAttr, String path) {
		m_attr = attr;
		m_typeAttr = typeAttr;
		m_path = path;
		hideChecked = false;
		if (attr.indexOf(";") < 0) {
			doCheck();
		} else {
			hideValue = Boolean.FALSE;
		}
	}

	private void doCheck() {
		if (!hideChecked) {
			hideChecked = true;
			check(m_attr, m_path);
			if ((hideValue instanceof Boolean) && !((Boolean) hideValue).booleanValue() && (!m_attr.equals(m_typeAttr)) && (m_typeAttr != null)) {
				m_attr = m_typeAttr;
				hideValue = null;
				check(m_attr, m_path);
			}

			if ((hideValue == null) && (m_attr.startsWith("SELF."))) {
				int index = m_path.lastIndexOf(".");
				if (index > 0) {
					check(m_attr, m_path.substring(0, index));
				}
			}
			m_attr = null;
			m_typeAttr = null;
			m_path = null;
		}
	}

	private void check(String attr, String path) {
		try {
			KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
			if (dfl != null) {
				String variableName = getVariablePath(path, attr);
				if ("TRUE".equalsIgnoreCase(attr) && "TRUE".equalsIgnoreCase(variableName)) {
					hideValue = Boolean.TRUE;
					return;
				}
				if (variableName != null) {
					hideValue = null;
					hideRoutine = getHideRoutine(variableName);
					if (hideRoutine != null) {
						int pos = variableName.lastIndexOf('.');
						if (pos > 0) {
							variableName = variableName.substring(0, pos).trim();
						}
						boolean grabbed = false;
						// if (!KvtSystem.hasWriteAccess()) {
						try {
							KvtSystemCommunicator.getTcDfl().client.setWriteAccess(true);
							grabbed = true;
						} catch (Exception e) {
						}
						// }
						hideValue = KvtSystemCommunicator.getTcDfl().execution.executeRoutineFromVariable(hideRoutine, variableName,
								new Object[0]);
						if (grabbed) {
							try {
								KvtSystemCommunicator.getTcDfl().client.setWriteAccess(false);
							} catch (Exception e) {
							}
						}
					} else {
						hideVariable = dfl.variable.createKStructVarWrapper(variableName);
						if (hideVariable != null) {
							hideValue = hideVariable.readActualValue(null);
						}
					}
					if (hideValue instanceof Integer) {
						int pos = attr.indexOf(';');
						String members = attr.substring(0, pos).trim();
						int i = ((Integer) hideValue).intValue();

						if (i != 0) {
							variables = new Vector(5);
							StringTokenizer tokenizer = new StringTokenizer(members, ",");
							pos = 1;
							while (tokenizer.hasMoreElements()) {
								String key = ((String) tokenizer.nextElement()).trim();
								if (isBitSet(pos, i)) {
									variables.addElement(key);
								}
								pos *= 2;
							}
						}
					} else if ((hideValue == null) && (attr.indexOf(';') < 0)) {
						StringTokenizer tokenizer = new StringTokenizer(attr, ",");
						variables = new Vector(5);
						while (tokenizer.hasMoreElements()) {
							variables.addElement(((String) tokenizer.nextElement()).trim());
						}
					}
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			// nothing to do
		}
	}

	private boolean isBitSet(int bitMask, int bitSet) {
		doCheck();
		return ((bitSet & bitMask) == bitMask);
	}

	public boolean displayElement(String key) {
		doCheck();
		if (variables != null) {
			return !variables.contains(key);
		}
		return true;
	}
}
