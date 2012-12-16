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
package com.keba.kemro.teach.dfl.value;

import java.util.Hashtable;
import java.util.Vector;

import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.execution.KExecUnitRoutine;
import com.keba.kemro.teach.dfl.structural.KStructNode;
import com.keba.kemro.teach.dfl.structural.KStructNodeVector;
import com.keba.kemro.teach.dfl.structural.routine.KStructRoutine;
import com.keba.kemro.teach.dfl.structural.type.KStructType;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeArray;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeMapTo;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeSimple;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeStruct;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeSubrange;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeUnit;
import com.keba.kemro.teach.dfl.structural.var.KStructVar;
import com.keba.kemro.teach.dfl.structural.var.KStructVarArray;
import com.keba.kemro.teach.dfl.structural.var.KStructVarUnit;
import com.keba.kemro.teach.dfl.util.KDflLogger;
import com.keba.kemro.teach.network.TcAccessHandle;
import com.keba.kemro.teach.network.TcDirEntry;
import com.keba.kemro.teach.network.TcExecutionUnit;
import com.keba.kemro.teach.network.TcMapTarget;
import com.keba.kemro.teach.network.TcStructuralNode;
import com.keba.kemro.teach.network.TcStructuralRoutineNode;
import com.keba.kemro.teach.network.TcStructuralTypeNode;
import com.keba.kemro.teach.network.TcStructuralVarNode;
import com.keba.kemro.teach.network.TcValue;

/**
 * Der Variablenwrapper repräsentiert die Variable und den zugehörigen Typ in
 * einem übergeordneten Wrapper-Objekt und stellt verschiedene Funktionalitäten
 * zur Verfügung die den Umgang mit Teachtalk-Variablen erleichtern.
 * 
 */
public class KStructVarWrapper {
	/** NO Typ */
	public static final byte NO_TYPE = TcStructuralTypeNode.UNKOWN_TYPE;
	/** BOOL Typ */
	public static final byte BOOL_TYPE = TcStructuralTypeNode.BOOL_TYPE;
	/** REAL Typ */
	public static final byte REAL_TYPE = TcStructuralTypeNode.REAL_TYPE;
	/** 8-Bit Integer Typ */
	public static final byte SINT_TYPE = TcStructuralTypeNode.SINT_TYPE;
	/** 16-Bit Integer Typ */
	public static final byte INT_TYPE = TcStructuralTypeNode.INT_TYPE;
	/** 32-Bit Integer Typ */
	public static final byte DINT_TYPE = TcStructuralTypeNode.DINT_TYPE;
	/** 64-Bit Integer Typ */
	public static final byte LINT_TYPE = TcStructuralTypeNode.LINT_TYPE;
	/** STRING - Typ */
	public static final byte STRING_TYPE = TcStructuralTypeNode.STRING_TYPE;
	/** SUBRANGE - Typ */
	public static final byte SUBRANGE_TYPE = TcStructuralTypeNode.SUBRANGE_TYPE;
	/** ARRAY - Typ */
	public static final byte ARRAY_TYPE = TcStructuralTypeNode.ARRAY_TYPE;
	/** ENUM - Typ */
	public static final byte ENUM_TYPE = TcStructuralTypeNode.ENUM_TYPE;
	/** STRUCT - Typ */
	public static final byte STRUCT_TYPE = TcStructuralTypeNode.STRUCT_TYPE;
	/** UNIT - Typ */
	public static final byte UNIT_TYPE = TcStructuralTypeNode.UNIT_TYPE;
	/** MAPTO - Typ */
	public static final byte MAPTO_TYPE = TcStructuralTypeNode.MAPTO_TYPE;
	/** ROUTINE - Typ */
	public static final byte ROUTINE_TYPE = TcStructuralTypeNode.ROUTINE_TYPE;
	/** 8-Bit Unsigned Integer Typ */
	public static final byte BYTE_TYPE = TcStructuralTypeNode.BYTE_TYPE;
	/** 16-Bit Unsigned Integer Typ */
	public static final byte WORD_TYPE = TcStructuralTypeNode.WORD_TYPE;
	/** 32-Bit Unsigned Integer Typ */
	public static final byte DWORD_TYPE = TcStructuralTypeNode.DWORD_TYPE;
	/** 64-Bit Unsigned Integer Typ */
	public static final byte LWORD_TYPE = TcStructuralTypeNode.LWORD_TYPE;

	public static final int	LREAL_TYPE	= TcStructuralTypeNode.LREAL_TYPE;;
	
	private static final TcValue m_WriteValue = new TcValue();
	private static final String TEACH_METHOD = "TEACH";
	private static final String ENTER_METHOD = "ENTER";
	private static final String MOVE_METHOD = "MOVE_";
	private static final String ASSIGN_METHOD = "ASSIGN";
	
	private String[] moveMacro;

	/** Referenz auf den übergeordneten Variablenwrapper */
	protected KStructVarWrapper parent;
	private KStructVarWrapper[] children;
	private int counter;
	private boolean enterChecked = false;
	private KStructVarWrapper[] enterWrappers;
	private boolean teachChecked;
	private boolean isTeachVariable;
	/**
	 * If the node is not allowed to have children, the elementcouner is set to
	 * -1 . Just to avoid the allocation of allowedChildren member var bool. An
	 * enumeration that is always empty. This is used when an enumeration of a
	 * leaf node's children is requested.
	 */
	private boolean isLoaded = false;
	private boolean forceChanged = false;
	private boolean forceMapToChanged = false;
	TcAccessHandle m_accessHandle;
	private Object m_actualValue;
	private int m_arrayIndex = -1;
	private boolean m_isArrayField = false;
	Object[] m_rootPath;
	String m_rootPathString;
	private KStructType m_type;
	private int kind = NO_TYPE;
	private TcValue m_value;
	private KStructVar m_variable;
	String key;
	private static boolean m_autoEnter = false;
	private TcMapTarget target;
	private KMapTarget actualMapTarget;
	KTcDfl dfl;
	private boolean m_hasAssignMethodChecked;
	private boolean m_hasAssignMethod;

	 /* ID with with the KStructVarWrapper can be identified within a TC Variable group
	 * @see KVariableGroup#refresh()
	 * {@link KVariableGroup}
	 */
	private int m_varId;
	
	/**
	 * flag to indicate if the variable needs cyclic updates. In most cases this flag will be false, but
	 * for example simulated hard keys only need one-way access, feedback is not required. 
	 * So if this flag is set to true, the variable will not be updated by the {@link KVariableServer}.
	 */
	private boolean m_isWriteOnly;

	public int getVarId() {
		return m_varId;
	}

	public void setVarId(int _varId) {
		this.m_varId = _varId;
	}

	/**
	 * Switchs the call of the ENTER method on or off. Is the value true then
	 * every time when the method setActualValue is called and the variable
	 * belongs to a unit with a ENTER method this method will be called.
	 * 
	 * @param autoEnter
	 *            true for on
	 */
	public static void setAutoEnter(boolean autoEnter) {
		m_autoEnter = autoEnter;
	}

	/**
	 * Returns the read only attribute
	 * 
	 * @return true if the variable is read only
	 */
	public boolean isReadOnly() {
		TcAccessHandle ah = getAccessHandle();
		if ((ah != null) && ah.hasValidAttributes()) {
			return ah.isReadOnly();
		}
		KStructVar var = getKStructVar();
		if (var != null) {
			return var.isReadOnly();
		}
		return false;
	}

	/**
	 * Returns the const attribute
	 * 
	 * @return true if the variable is const
	 */
	public boolean isConstant() {
		TcAccessHandle ah = getAccessHandle();
		if ((ah != null) && ah.hasValidAttributes()) {
			return ah.isConstant();
		}
		Object[] arr = getVariableComponentPath();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] instanceof KStructVar) {
				if (((KStructVar) arr[i]).isConstVariable()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Erzeugt einen Variablenwrapper
	 * 
	 * @param variable
	 *            Strukturvariable
	 * @param index
	 *            Indexwert, wenn es sich um ein Feldelement handelt
	 * @param type
	 *            Strukturtyp
	 */
	protected KStructVarWrapper(KStructVar variable, int index, KStructType type, KTcDfl dfl, boolean isArrayField) {
		this(variable, index, ((TcStructuralTypeNode) type.getTcStructuralNode()).getTypeKind(), dfl, isArrayField);
		m_type = type;
	}

	/**
	 * if a variable is write-only, no cyclic updates will be performed by the KVariableServer
	 * @return
	 */
	public boolean isWriteOnly() {
		return m_isWriteOnly;
	}

	/**
	 * sets the write-only flag to indicate if the variable needs cyclic updates. In most cases this flag will be false, but
	 * for example simulated hard keys only need one-way access, feedback is not required. 
	 * So if this flag is set to true, the variable will not be updated by the {@link KVariableServer}.
	 * @param _isWriteOnly
	 */
	public void setWriteOnly(boolean _isWriteOnly) {
		m_isWriteOnly = _isWriteOnly;
	}

	protected KStructVarWrapper(KStructVar variable, int index, int typeKind, KTcDfl dfl, boolean isArrayField) {
		m_variable = variable;
		m_arrayIndex = (variable == null) ? index : -1;
		m_isArrayField = isArrayField;
		kind = typeKind;
		this.dfl = dfl;

		boolean allowsChildren = (kind == TcStructuralTypeNode.ARRAY_TYPE) || (kind == TcStructuralTypeNode.UNIT_TYPE) || (kind == TcStructuralTypeNode.STRUCT_TYPE);
		setAllowsChildren(allowsChildren);
		setLoaded(!allowsChildren);
	}

	/**
	 * Returns type kind of the variable.
	 * 
	 * @return type kind
	 */
	public byte getTypeKind() {
		return (byte) kind;
	}

	/**
	 * Setzt den aktuellen Wert.
	 * 
	 * @param actualValue
	 *            aktueller Wert
	 */
	public boolean setActualValue(Object actualValue) {
		return setActualValue(actualValue, null);
	}

	public String getRootPathString() {
		if (m_rootPathString == null) {
			Object[] arr = getVariableComponentPath();
			String path = "";
			for (int i = 0; i < arr.length; i++) {
				// TODO: @ltz -> testing
				if (arr[i] instanceof Integer)
					path = path + "[" + arr[i].toString() + "]";
				else
					path = path + "." + arr[i].toString();
			}
			return path.substring(1);

		}
		return m_rootPathString;
	}

	/**
	 * Writes the actual value for a routine local variable.
	 * 
	 * @param actualValue
	 *            new value
	 * @param execRoutine
	 *            routine
	 */
	public boolean setActualValue(Object actualValue, KExecUnitRoutine execRoutine) {
		boolean done = false;
		if (actualValue != null) {
			if ((this.getAccessHandle() != null) && (actualValue != null)) {
				synchronized (m_WriteValue) {
					if ((actualValue instanceof Boolean) && (kind == TcStructuralTypeNode.BOOL_TYPE)) {
						m_WriteValue.boolValue = ((Boolean) actualValue).booleanValue();
						m_WriteValue.isValid = true;
					} else if ((actualValue instanceof Number) && ((kind == TcStructuralTypeNode.SINT_TYPE) || (kind == TcStructuralTypeNode.BYTE_TYPE))) {
						m_WriteValue.int8Value = ((Number) actualValue).byteValue();
						m_WriteValue.isValid = true;
						if (!(actualValue instanceof Byte)) {
							actualValue = new Byte(m_WriteValue.int8Value);
						}
					} else if ((actualValue instanceof Number) && ((kind == TcStructuralTypeNode.INT_TYPE) || (kind == TcStructuralTypeNode.WORD_TYPE))) {
						m_WriteValue.int16Value = ((Number) actualValue).shortValue();
						m_WriteValue.isValid = true;
						if (!(actualValue instanceof Short)) {
							actualValue = new Short(m_WriteValue.int16Value);
						}
					} else if ((actualValue instanceof Number) && ((kind == TcStructuralTypeNode.DINT_TYPE) || (kind == TcStructuralTypeNode.DWORD_TYPE) || (kind == TcStructuralTypeNode.ENUM_TYPE) || (kind == TcStructuralTypeNode.SUBRANGE_TYPE))) {
						m_WriteValue.int32Value = ((Number) actualValue).intValue();
						m_WriteValue.isValid = true;
						if (!(actualValue instanceof Integer)) {
							actualValue = new Integer(m_WriteValue.int32Value);
						}
					} else if ((actualValue instanceof Number) && ((kind == TcStructuralTypeNode.LINT_TYPE) || (kind == TcStructuralTypeNode.LWORD_TYPE))) {
						m_WriteValue.int64Value = ((Number) actualValue).longValue();
						m_WriteValue.isValid = true;
						if (!(actualValue instanceof Long)) {
							actualValue = new Long(m_WriteValue.int64Value);
						}
					} else if ((actualValue instanceof Number) && (kind == TcStructuralTypeNode.REAL_TYPE)) {
						m_WriteValue.floatValue = ((Number) actualValue).floatValue();
						m_WriteValue.isValid = true;
						if (!(actualValue instanceof Float)) {
							actualValue = new Float(m_WriteValue.floatValue);
						}
					} else if ((actualValue instanceof String) && (kind == TcStructuralTypeNode.STRING_TYPE)) {
						m_WriteValue.stringValue = (String) actualValue;
						m_WriteValue.isValid = true;
					}else if ((actualValue instanceof Double) && (kind == TcStructuralTypeNode.LREAL_TYPE)) {
						m_WriteValue.isValid= true;
						m_WriteValue.int64Value= Double.doubleToRawLongBits(((Double)actualValue).doubleValue());
					}
					else {
						m_WriteValue.isValid = false;
					}
					if (m_WriteValue.isValid) {
						TcExecutionUnit te = (execRoutine != null) ? execRoutine.getTcExecutionUnit() : null;
						done = dfl.client.execution.setActualValue(this.getAccessHandle(), te, m_WriteValue);
					} 
				}
				if (done) {
					if (m_autoEnter) {
						if (!enterVariable()) {
							KDflLogger.error(this, "KStructVarWrapper enter Method returned false");
						}
					}
					forceChanged = true;
				}
			}
		}
		return done;
	}

	/**
	 * Takes the actual value form the source variable and sets it.
	 * 
	 * @param srcActualValue
	 *            source variable
	 * @return true if the actual value was successfully set
	 */
	public boolean setActualValue(KStructVarWrapper srcActualValue) {
		TcAccessHandle srcAccessHandle = srcActualValue.getAccessHandle();
		TcAccessHandle destAccessHandle = getAccessHandle();
		if ((srcAccessHandle == null) || (destAccessHandle == null)) {
			return false;
		}
		boolean done = dfl.client.execution.copyActualValue(srcAccessHandle, null, destAccessHandle, null);
		forceChanged = true;
		return done;
	}

	/**
	 * Liefert das Zugriffshandle auf die Variable.
	 * 
	 * @return Zugriffshandle auf die Variable.
	 */
	public TcAccessHandle getAccessHandle() {
		if (m_accessHandle == null) {
			if (m_rootPathString == null) {
				Object[] path = getVariableComponentPath();
				m_rootPathString = (path != null) ? dfl.structure.getFullPath(path) : null;
			}
			if (m_rootPathString != null) {
				m_accessHandle = dfl.client.structure.getVarAccessHandle(m_rootPathString);
			}
			if (m_accessHandle == null) {
				KDflLogger.error(this, "KStructVarWrapper - getAccessHandle(): can't get variable access handle for " + m_rootPathString);
			}
		}
		return m_accessHandle;
	}

	/**
	 * Liefert den Aktualwert.
	 * 
	 * @return Aktualwertobjekt
	 */
	public Object getActualValue() {
		return m_actualValue;
	}

	/**
	 * Reads the actual value immediately from TeachControl.
	 * 
	 * @param execRoutine
	 *            only needed for routine local variables
	 * @return actual value
	 */
	public Object readActualValue(KExecUnitRoutine execRoutine) {
		Object value = null;
		if (getAccessHandle() != null) {
			synchronized (m_WriteValue) {
				TcExecutionUnit eu = (execRoutine != null) ? execRoutine.getTcExecutionUnit() : null;
				dfl.client.execution.getActualValue(getAccessHandle(), eu, m_WriteValue);
				if (m_WriteValue.isValid) {
					if (kind == TcStructuralTypeNode.BOOL_TYPE) {
						value = m_WriteValue.boolValue ? Boolean.TRUE : Boolean.FALSE;
					} else if ((kind == TcStructuralTypeNode.SINT_TYPE) || (kind == TcStructuralTypeNode.BYTE_TYPE)) {
						value = new Byte(m_WriteValue.int8Value);
					} else if ((kind == TcStructuralTypeNode.INT_TYPE) || (kind == TcStructuralTypeNode.WORD_TYPE)) {
						value = new Short(m_WriteValue.int16Value);
					} else if ((kind == TcStructuralTypeNode.DINT_TYPE) || (kind == TcStructuralTypeNode.DWORD_TYPE) || (kind == TcStructuralTypeNode.ENUM_TYPE) || (kind == TcStructuralTypeNode.SUBRANGE_TYPE)) {
						value = new Integer(m_WriteValue.int32Value);
					} else if ((kind == TcStructuralTypeNode.LINT_TYPE) || (kind == TcStructuralTypeNode.LWORD_TYPE)) {
						value = new Long(m_WriteValue.int64Value);
					} else if (kind == TcStructuralTypeNode.REAL_TYPE) {
						value = new Float(m_WriteValue.floatValue);
					} else if (kind == TcStructuralTypeNode.STRING_TYPE) {
						value = m_WriteValue.stringValue;
					}else if (kind == TcStructuralTypeNode.LREAL_TYPE) {
						value= new Double(Double.longBitsToDouble(m_WriteValue.int64Value));
					}
					else if (kind == TcStructuralTypeNode.UNKOWN) {
						System.out.println("#################  Found UNKNOWN type node kind: "+kind);
					}
				}
			}
		}
		return value;
	}

	/**
	 * Liefert den Feldindex, falls es sich um ein Feldelement handelt.
	 * 
	 * @return Feldindex
	 */
	public int getArrayIndex() {
		return m_arrayIndex;
	}

	/**
	 * Liefert den Strukturtyp.
	 * 
	 * @return Strukturtyp
	 */
	public KStructType getKStructType() {
		if (m_type == null) {
			// load type
			getVariableComponentPath();
		}
		return m_type;
	}

	/**
	 * Liefert die Strukturvariable.
	 * 
	 * @return Strukturvariable
	 */
	public KStructVar getKStructVar() {
		if ((m_variable == null) && (m_arrayIndex < 0)) {
			// load m_variable
			getVariableComponentPath();
		}
		return m_variable;
	}

	/**
	 * Liefert den Schlüssel(Name und Pfad der Variable) der Variablen.
	 * 
	 * @return Schlüsselwert
	 */
	public String getKey() {
		if (key == null) {
			// load root path
			getVariableComponentPath();
			if (isArrayField()) {
				key = "[" + m_arrayIndex + "]";
			} else if (m_variable != null) {
				key = m_variable.getKey();
			} else if (m_type != null) {
				key = m_type.getKey();
			} else {
				key = "";
			}
		}
		return key;
	}

	/**
	 * Liefert den Pfad zur Wurzelvariablen in einem Objektfeld.
	 * 
	 * @return Objektfeld von Variablenwrapper-Objekten
	 */
	public Object[] getVariableComponentPath() {
		if (m_rootPath == null) {
			if (getParent() != null) {
				KStructVarWrapper p = getParent();
				Object[] path = p.getVariableComponentPath();
				m_rootPath = new Object[path.length + 1];
				System.arraycopy(path, 0, m_rootPath, 0, path.length);
				if (isArrayField()) {
					m_rootPath[path.length] = new Integer(m_arrayIndex);
				} else {
					m_rootPath[path.length] = m_variable;
				}
			} else if (m_rootPathString != null) {
				m_rootPath = dfl.structure.getVariableComponentPath(m_rootPathString);
				if (m_rootPath == null) {
					m_rootPath = new Object[0];
				}
				if (0 < m_rootPath.length) {
					Object o = m_rootPath[m_rootPath.length - 1];
					if (o instanceof Integer) {
						m_type = getArrayElementType(m_rootPath);
					} else if (o instanceof KStructVar) {
						m_variable = (KStructVar) o;
						m_type = skipMapTo(m_variable.getKStructType());
					}
				}
			}
		}
		return m_rootPath;
	}

	/**
	 * Liefert die Wurzelvariable.
	 * 
	 * @return Strukturvariable
	 */
	public KStructVar getRootVariable() {
		KStructVarWrapper p = getParent();
		if (p != null) {
			return p.getRootVariable();
		}
		Object[] path = getVariableComponentPath();
		if ((path != null) && (0 < path.length)) {
			return (KStructVar) path[0];
		}
		return null;
	}

	/**
	 * Liefert wahr, wenn der Wrapper ein Feldelement eines Feldes ist.
	 * 
	 * @return Wahrheitswert
	 */
	public boolean isArrayField() {
		return m_isArrayField;
	}

	/**
	 * Liefert die Variable als Zeichenkette.
	 * 
	 * @return Zeichenkette
	 */
	public String toString() {
		return getKey();
	}

	/**
	 * Liefert ein Wertobjekt des Wrappers zurück
	 * 
	 * @return Wertobjekt
	 */
	TcValue getValueObject() {
		if (m_value == null) {
			m_value = new TcValue();
		}
		return m_value;
	}

	void setValueObject(TcValue value) {
		m_value = value;
	}

	/**
	 * Die Methode veranlaßt die Erzeugung der untergeordneten Variablenwrapper.
	 * Dazu wird die Zusammensetzung des Variablentyps analysiert und für die
	 * untergeordneten Komponenten einzelne Variablenwrapper erzeugt, die mit
	 * dem übergeordneten Wrapper verkettet sind.
	 */
	private void loadChildren() {
		setLoaded(true);
		KStructType t = skipAlias(getKStructType());
		if (t instanceof KStructTypeUnit) {
			KStructTypeUnit base = ((KStructTypeUnit) t).getBaseUnit();
			if (base != null) {
				loadChildren(t, base, new Hashtable(29), true);
			} else {
				loadChildren(t, null, null, false);
			}
		} else {
			loadChildren(t, null, null, false);
		}
	}

	/**
	 * Erzeugt für einen angegebenen zusammengesetzten Variablentyp die
	 * einezelnen Variablenwrapper-Objekte. Dabei werden die Komponenten des
	 * Basistyps berücksichtigt und unterschieden, ob private Komponenten
	 * erzeugt werden sollen.
	 * 
	 * @param type
	 *            Typ der Variable
	 * @param baseUnit
	 *            Basistyp, wenn es sich um einen abgeleiteten Typ handelt
	 * @param nameBuffer
	 *            speichert die Namen der Komponenten
	 * @param includePrivates
	 *            gibt an, ob private Komponenten erzeugt werden sollen
	 */
	private void loadChildren(KStructType type, KStructTypeUnit baseUnit, Hashtable nameBuffer, boolean includePrivates) {
		if (type instanceof KStructTypeArray) {
			int lb = ((KStructTypeArray) type).getLowerBound().intValue();
			int up = ((KStructTypeArray) type).getUpperBound().intValue();
			KStructType arrayType = skipMapTo(((KStructTypeArray) type).getArrayElementKStructType());
			for (int i = lb; i <= up; i++) {
				addChild(new KStructVarWrapper(null, i, arrayType, dfl, true));
			}
			return;
		}
		if (baseUnit != null) {
			loadChildren(baseUnit, baseUnit.getBaseUnit(), nameBuffer, false);
		}
		KStructNodeVector components = null;
		if (type instanceof KStructTypeStruct) {
			components = ((KStructTypeStruct) type).components;
		} else if (type instanceof KStructTypeUnit) {
			components = ((KStructTypeUnit) type).components;
		}
		if (components != null) {
			KStructNode node;
			for (int i = 0; i < components.getChildCount(); i++) {
				node = components.getChild(i);
				if (node instanceof KStructVar) {
					KStructType t = skipMapTo(((KStructVar) node).getKStructType());
					if (nameBuffer != null) {
						if ((includePrivates) || (!(includePrivates || node.isPrivate()))) {
							String name = node.getKey();
							if (nameBuffer.get(name) == null) {
								addChild(new KStructVarWrapper((KStructVar) node, -1, t, dfl, false));
							}
							nameBuffer.put(name, Boolean.TRUE);
						}
					} else {
						addChild(new KStructVarWrapper((KStructVar) node, -1, t, dfl, false));
					}
				}
			}
		}
	}

	/**
	 * Ändert den Aktualwert (Objekt) des Wrappers. Je nach Variablentyp wird
	 * dem Aktualwert ein entsprechendes Objekt zugewiesen.
	 * 
	 * @return wahr, wenn sich der aktuelle Wert geändert hat
	 */
	boolean valueChanged() {
		boolean changed = forceChanged;
		forceChanged = false;
		if (kind == TcStructuralTypeNode.BOOL_TYPE) {
			changed = changed || ((m_actualValue == null) && m_value.isValid) || ((m_actualValue != null) && !m_value.isValid) || ((m_actualValue != null) && (((Boolean) m_actualValue).booleanValue() != m_value.boolValue));
			if (changed && m_value.isValid) {
				m_actualValue = m_value.boolValue ? Boolean.TRUE : Boolean.FALSE;
			} else if (!m_value.isValid) {
				m_actualValue = null;
			}
		} else if ((kind == TcStructuralTypeNode.SINT_TYPE) || (kind == TcStructuralTypeNode.BYTE_TYPE)) {
			changed = changed || ((m_actualValue == null) && m_value.isValid) || ((m_actualValue != null) && !m_value.isValid) || ((m_actualValue != null) && (((Byte) m_actualValue).byteValue() != m_value.int8Value));
			if (changed && m_value.isValid) {
				m_actualValue = new Byte(m_value.int8Value);
			} else if (!m_value.isValid) {
				m_actualValue = null;
			}
		} else if ((kind == TcStructuralTypeNode.INT_TYPE) || (kind == TcStructuralTypeNode.WORD_TYPE)) {
			changed = changed || ((m_actualValue == null) && m_value.isValid) || ((m_actualValue != null) && !m_value.isValid) || ((m_actualValue != null) && (((Short) m_actualValue).shortValue() != m_value.int16Value));
			if (changed && m_value.isValid) {
				m_actualValue = new Short(m_value.int16Value);
			} else if (!m_value.isValid) {
				m_actualValue = null;
			}
		} else if ((kind == TcStructuralTypeNode.DWORD_TYPE)) {
			changed = changed || ((m_actualValue == null) && m_value.isValid) || ((m_actualValue != null) && !m_value.isValid) || ((m_actualValue != null) && (((DWord) m_actualValue).intValue() != m_value.int32Value));
			if (changed && m_value.isValid) {
				m_actualValue = new DWord(m_value.int32Value);
			} else if (!m_value.isValid) {
				m_actualValue = null;
			}

		} else if ((kind == TcStructuralTypeNode.DINT_TYPE) || (kind == TcStructuralTypeNode.DWORD_TYPE) || (kind == TcStructuralTypeNode.ENUM_TYPE) || (kind == TcStructuralTypeNode.SUBRANGE_TYPE)) {
			changed = changed || ((m_actualValue == null) && m_value.isValid) || ((m_actualValue != null) && !m_value.isValid) || ((m_actualValue != null) && (((Integer) m_actualValue).intValue() != m_value.int32Value));
			if (changed && m_value.isValid) {
				m_actualValue = new Integer(m_value.int32Value);
			} else if (!m_value.isValid) {
				m_actualValue = null;
			}
		} else if ((kind == TcStructuralTypeNode.LINT_TYPE) || (kind == TcStructuralTypeNode.LWORD_TYPE)) {
			changed = changed || ((m_actualValue == null) && m_value.isValid) || ((m_actualValue != null) && !m_value.isValid) || ((m_actualValue != null) && (((Long) m_actualValue).longValue() != m_value.int64Value));
			if (changed && m_value.isValid) {
				m_actualValue = new Long(m_value.int64Value);
			} else if (!m_value.isValid) {
				m_actualValue = null;
			}
		} else if (kind == TcStructuralTypeNode.REAL_TYPE) {
			changed = changed || ((m_actualValue == null) && m_value.isValid) || ((m_actualValue != null) && !m_value.isValid) || ((m_actualValue != null) && (((Float) m_actualValue).floatValue() != m_value.floatValue)); // $codepro.audit.disable
			// floatComparison
			if (changed && m_value.isValid) {
				m_actualValue = new Float(m_value.floatValue);
			} else if (!m_value.isValid) {
				m_actualValue = null;
			}
		} else if (kind == TcStructuralTypeNode.STRING_TYPE) {
			changed = changed || ((m_actualValue == null) && m_value.isValid) || ((m_actualValue != null) && !m_value.isValid) || ((m_actualValue != null) && !m_actualValue.equals(m_value.stringValue));
			if (changed && m_value.isValid) {
				m_actualValue = m_value.stringValue;
			} else if (!m_value.isValid) {
				m_actualValue = null;
			}
		}else if(kind == TcStructuralTypeNode.LREAL_TYPE) {
			changed = changed ||((m_actualValue == null ) && m_value.isValid) || ((m_actualValue != null) && !m_value.isValid) ||((m_actualValue != null) && (((Double)m_actualValue).doubleValue() != Double.longBitsToDouble(m_value.int64Value)));
			if(changed && m_value.isValid) {
				m_actualValue = new Double(Double.longBitsToDouble(m_value.int64Value));
			}else if(!m_value.isValid) {
				m_actualValue=null;
			}
		} else {
			m_actualValue = null;
		}
		return changed;
	}

	/**
	 * Description of the Method
	 * 
	 * @param type
	 *            Description of the Parameter
	 * 
	 * @return Description of the Return Value
	 */
	static KStructType skipMapTo(KStructType type) {
		while (type instanceof KStructTypeMapTo) {
			type = type.getKStructType();
		}
		return type;
	}

	static KStructType skipAlias(KStructType type) {
		while ((type != null) && type.isAliasType()) {
			type = type.getKStructType();
		}
		return type;
	}

	private static KStructType getArrayElementType(Object[] path) {
		KStructType elementType = null;
		int i = path.length - 2;
		while ((0 <= i) && (path[i] instanceof Integer)) {
			i--;
		}
		if ((0 <= i) && (path[i] instanceof KStructVarArray)) {
			KStructType t = skipMapTo(((KStructVarArray) path[i]).getKStructType());
			elementType = (t instanceof KStructTypeArray) ? ((KStructTypeArray) t).getArrayElementKStructType() : null;
			elementType = skipMapTo(elementType);
			while ((i < path.length) && (elementType instanceof KStructTypeArray)) {
				elementType = ((KStructTypeArray) elementType).getArrayElementKStructType();
				elementType = skipMapTo(elementType);
				i++;
			}
		}
		return elementType;
	}

	/**
	 * @return true if the variable or component is a map variable
	 */
	public boolean isMapTo() {
		Object[] path = getVariableComponentPath();
		int i = path.length - 1;
		if ((0 <= i) && (path[i] instanceof KStructVar)) {
			return ((KStructVar) path[i]).isMapTo();
		}
		// is array field
		i = path.length - 2;
		while ((0 <= i) && (path[i] instanceof Integer)) {
			i--;
		}
		KStructType elementType = null;
		if ((0 <= i) && (path[i] instanceof KStructVarArray)) {
			KStructType t = skipMapTo(((KStructVarArray) path[i]).getKStructType());
			elementType = (t instanceof KStructTypeArray) ? ((KStructTypeArray) t).getArrayElementKStructType() : null;
			t = skipMapTo(elementType);
			while ((i < path.length) && (t instanceof KStructTypeArray)) {
				elementType = ((KStructTypeArray) t).getArrayElementKStructType();
				t = skipMapTo(elementType);
				i++;
			}
		}
		return elementType instanceof KStructTypeMapTo;
	}

	/**
	 * Returns true if the variable is referenced.
	 * 
	 * @return true if the variable is referenced
	 */
	public boolean isReferenced() {
		KStructVar rv = getRootVariable();
		return (rv != null) && rv.isReferenced();
	}

	/**
	 * Prüft die Variable auf eine SAVE-Variable.
	 * 
	 * @return wahr, wenn die Variable als SAVE deklariert ist.
	 */
	public boolean isSaveVar() {
		Object[] arr = getVariableComponentPath();
		if (arr != null) {
			Object o;
			for (int i = (arr.length - 1); i >= 0; i--) {
				o = arr[i];
				if ((o instanceof KStructVar) && ((KStructVar) o).isSave()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if this variable or component is a user node.
	 * 
	 * @return true for a user node
	 */
	public boolean isUserVar() {
		TcAccessHandle ah = getAccessHandle();
		if ((ah != null) && ah.hasValidAttributes()) {
			return ah.isUser();
		}
		Object[] arr = getVariableComponentPath();
		if ((arr != null) && (0 < arr.length) && (((KStructVar) arr[0]).isUserNode())) {
			Object o;
			for (int i = (arr.length - 1); i >= 0; i--) {
				o = arr[i];
				if (o instanceof KStructVar) {
					if (((KStructVar) o).isUserNode()) {
						return true;
					}
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * @return true if the variable is declared in a user program
	 */
	public boolean isDeclaredInUserProgram() {
		KStructVar rv = getRootVariable();
		if ((rv != null) && (rv.getTcStructuralNode().getDeclarationNode() != null)) {
			TcStructuralNode tcn = rv.getTcStructuralNode().getDeclarationNode();
			TcDirEntry de = dfl.client.structure.getDirEntry(tcn);
			if (de != null) {
				return (de.getKind() == TcDirEntry.USER_PROGRAM) || (de.getKind() == TcDirEntry.USER_DATA);
			}
		}
		return false;
	}

	/**
	 * Setzt eine Variable als geladen.
	 * 
	 * @param b
	 *            Wahrheitswert
	 */
	private void setLoaded(boolean b) {
		isLoaded = b;
	}
	
	/**
	 * Indicates whether the variable is already loaded (filled with data from TC)
	 * @return
	 */
	public boolean isLoaded() {
		return isLoaded;
	}

	/**
	 * Die Methode ermöglich das nachladen von untergeordneten Variablenwrapper-
	 * Objekten zu verhindern.
	 * 
	 * @param b
	 *            falsch, Nachladen wird verhindert.
	 */
	private void setAllowsChildren(boolean b) {
		if (!b) {
			counter = -1;
		} else if (counter < 0) {
			counter = 0;
		}
	}

	/**
	 * Setzt das übergeordnete Variablenwrapper-Objekt
	 * 
	 * @param parent
	 *            Variablenwrapper
	 */
	private void setParent(KStructVarWrapper parent) {
		this.parent = parent;
	}

	/**
	 * Liefert ein untergeordnetes Wrapper-Objekt entsprechend einem angegebenen
	 * Index.
	 * 
	 * @param childIndex
	 *            Index des untergeordneten Wrappers
	 * 
	 * @return Variablenwrapper-Objekt
	 */
	public KStructVarWrapper getChildAt(int childIndex) {
		if (!isLoaded && getAllowsChildren()) {
			loadChildren();
		}
		if ((0 <= childIndex) && (childIndex < counter)) {
			return children[childIndex];
		}
		return null;
	}

	/**
	 * Liefert die Anzahl der untergeordneten Variablenwrapper
	 * 
	 * @return Anzahl der Wrapper
	 */
	public int getChildCount() {
		if (!isLoaded && getAllowsChildren()) {
			loadChildren();
		}
		return counter;
	}

	/**
	 * Liefert den Index für einen untergeordneten Variablenwrapper.
	 * 
	 * @param child
	 *            Variablenwrapper-Objekt
	 * 
	 * @return Index des Wrappers
	 */
	public int getChildIndex(KStructVarWrapper child) {
		if (!isLoaded && getAllowsChildren()) {
			loadChildren();
		}
		if ((children != null) && (child != null)) {
			int i = 0;
			while (i < counter) {
				if (child.equals(children[i])) {
					return i;
				}
				i++;
			}
		}
		return -1;
	}

	/**
	 * Liefert den übergeordneten Variablenwrapper
	 * 
	 * @return übergeordneter Wrapper oder null
	 */
	public KStructVarWrapper getParent() {
		return parent;
	}

	/**
	 * Liefert den Wahrheitswert für das Nachladen untergeordneter
	 * Wrapper-Objekte.
	 * 
	 * @return wahr, wenn untergeordnete Wrapper nachgeladen werden können.
	 */
	public boolean getAllowsChildren() {
		return (counter >= 0);
	}

	private void addChild(KStructVarWrapper child) {
		if (children == null) {
			children = new KStructVarWrapper[10];
			counter = 0;
		} else if (counter == children.length) {
			KStructVarWrapper[] help = new KStructVarWrapper[children.length + 5];
			System.arraycopy(children, 0, help, 0, children.length);
			children = help;
		}
		children[counter] = child;
		counter++;
		child.setParent(this);
	}

	/**
	 * Liefert den unteren Wertebereich des Variablentyps, wenn es sich um einen
	 * einfachen Typ handelt, sonst null.
	 * 
	 * @return Nummernobjekt
	 */
	public Number getLowerRange() {
		KStructType t = skipAlias(getKStructType());
		if (t instanceof KStructTypeSubrange) {
			return ((KStructTypeSubrange) t).getLowerBound();
		}
		if (t instanceof KStructTypeSimple) {
			return ((KStructTypeSimple) t).getDefaultLowerRange();
		}
		return null;
	}

	/**
	 * Liefert den oberen Wertebereich des Variablentyps, wenn es sich um einen
	 * einfachen Typ handelt, sonst null.
	 * 
	 * @return Nummernobjekt
	 */
	public Number getUpperRange() {
		KStructType t = skipAlias(getKStructType());
		if (t instanceof KStructTypeSubrange) {
			return ((KStructTypeSubrange) t).getUpperBound();
		}
		if (t instanceof KStructTypeSimple) {
			return ((KStructTypeSimple) t).getDefaultUpperRange();
		}
		return null;
	}

	/**
	 * Die Methode prüft, ob eine Variable teachbar ist.
	 * 
	 * @return wahr, wenn die Variable teachbar ist.
	 */
	private boolean isEnterVariable() {
		if (!enterChecked) {
			enterChecked = true;
			Vector v = new Vector(5);
			Object[] arr = getVariableComponentPath();
			if (arr != null) {
				for (int i = (arr.length - 1); i >= 0; i--) {
					if (arr[i] instanceof Integer) {
						Object[] newArr = new Object[i + 1];
						System.arraycopy(arr, 0, newArr, 0, i + 1);
						KStructVarWrapper w = dfl.variable.createKStructVarWrapper(newArr);
						if (w != null) {
							KStructType t = skipAlias(w.getKStructType());
							if (t instanceof KStructTypeUnit) {
								KStructRoutine routine = ((KStructTypeUnit) t).getRoutine(ENTER_METHOD);
								if (routine != null) {
									v.addElement(w);
								}
							}
						}
					} else if (arr[i] instanceof KStructVarUnit) {
						KStructTypeUnit t = (KStructTypeUnit) skipAlias(skipMapTo(((KStructVarUnit) arr[i]).getKStructType()));
						KStructRoutine routine = t.getRoutine(ENTER_METHOD);
						if (routine != null) {
							Object[] newArr = new Object[i + 1];
							System.arraycopy(arr, 0, newArr, 0, i + 1);
							KStructVarWrapper w = dfl.variable.createKStructVarWrapper(newArr);
							if (w != null) {
								v.addElement(w);
							}
						}
					}
				}
				if (0 < v.size()) {
					enterWrappers = new KStructVarWrapper[v.size()];
					v.copyInto(enterWrappers);
				}
			}
		}
		return enterWrappers != null;
	}

	private boolean enterVariable() {
		// only for user variables the enter routine should be called
		if (isUserVar() && isEnterVariable()) {
			Object[] param = new Object[1];
			for (int i = 0; i < enterWrappers.length; i++) {
				KStructType type = skipAlias(enterWrappers[i].getKStructType());
				KStructRoutine routine = ((KStructTypeUnit) type).getRoutine(ENTER_METHOD);
				param[0] = enterWrappers[i];
				Object result = dfl.execution.executeRoutine(routine, enterWrappers[i], param);
				if (!(result instanceof Boolean) || !((Boolean) result).booleanValue()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Die Methode startet den Teachvorgang für einen Variable.
	 * 
	 * @return wahr, wenn das Teachen erfolgreich war.
	 */
	public boolean executeMoveMacro(String command) {
		if ((getMoveMacros() != null) && (getMoveMacros().length > 0)) {
			KStructType type = skipAlias(getKStructType());
			if (type instanceof KStructTypeUnit) {
				// teaching parameter-variable must be a block, that contains a
				// teach-method
				KStructRoutine routine = ((KStructTypeUnit) type).getRoutine(MOVE_METHOD + command);
				Object result = dfl.execution.executeRoutine(routine, this, new Object[0]);
				if (result instanceof Boolean) {
					return ((Boolean) result).booleanValue();
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Die Methode startet den Teachvorgang für einen Variable.
	 * 
	 * @return wahr, wenn das Teachen erfolgreich war.
	 */
	public boolean teachVariable() {
		if (isTeachVariable()) {
			KStructType type = skipAlias(getKStructType());
			if (type instanceof KStructTypeUnit) {
				// teaching parameter-variable must be a block, that contains a
				// teach-method
				KStructRoutine routine = ((KStructTypeUnit) type).getRoutine(TEACH_METHOD);
				Object[] param = new Object[routine.parameters.getChildCount()];
				param[0] = this;

				Object result = dfl.execution.executeRoutine(routine, this, param);
				if ((result instanceof Boolean) && ((Boolean) result).booleanValue()) {
					if (m_autoEnter) {
						if (!enterVariable()) {
							KDflLogger.error(this, "KStructVarWrapper enter Method returned false");
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Die Methode prüft, ob eine Variable teachbar ist.
	 * 
	 * @return wahr, wenn die Variable teachbar ist.
	 */

	public String[] getMoveMacros() {
		if (moveMacro == null) {
			KStructType type = skipAlias(getKStructType());
			if (type instanceof KStructTypeUnit) {
				// teaching parameter-variable must be a block, that contains a
				// teach-method
				Vector mm = new Vector();
				for (int i = 0; i < ((KStructTypeUnit) type).routines.getChildCount(); i++) {
					String name = ((KStructTypeUnit) type).routines.getChild(i).getKey();
					if (name.toUpperCase().startsWith(MOVE_METHOD)) {
						mm.addElement(name.substring(MOVE_METHOD.length()));
					}
				}
				moveMacro = new String[mm.size()];
				for (int i = 0; i < mm.size(); i++) {
					moveMacro[i] = (String) mm.elementAt(i);
				}
			}
		}
		if ((moveMacro != null) && (moveMacro.length == 0)) {
			return null;
		}
		return moveMacro;
	}

	public boolean assignVariable(KStructVarWrapper _srcVariable) {
		Object res=null;
		if (hasAssignMethod()) {
			
			KStructType type = skipAlias(getKStructType());
			if (type instanceof KStructTypeUnit) {
				KStructRoutine assignRoutine = ((KStructTypeUnit) type).getRoutine(ASSIGN_METHOD);
				//normally there should be one parameter
				int cnt = assignRoutine.parameters.getChildCount();
				int b=0;
				if(cnt == 1) {
					res = dfl.execution.executeRoutine(assignRoutine, this, new Object[] {_srcVariable});
				}
			}
		}
		return res!=null;
	}

	public boolean hasAssignMethod() {
		if(!m_hasAssignMethodChecked) {
			m_hasAssignMethodChecked = true;
			KStructType type = skipAlias(getKStructType());
			if (type instanceof KStructTypeUnit) {
				// teaching parameter-variable must be a block, that contains a
				// teach-method
				m_hasAssignMethod = ((KStructTypeUnit) type).getRoutine(ASSIGN_METHOD) != null;
			}			
		}
		return m_hasAssignMethod;
	}

	/**
	 * Calls the TEACH Routine with the additional parameter
	 * 
	 * @param parameter
	 *            parameter is a KStructVarWrapper of the caller or project and
	 *            program (String)
	 * @return true if the routine was successfully executed and has returned
	 *         TRUE
	 */
	public boolean teachVariable(Object[] parameter) {
		if (isTeachVariable()) {
			KStructType type = skipAlias(getKStructType());
			if (type instanceof KStructTypeUnit) {
				// teaching parameter-variable must be a block, that contains a
				// teach-method
				KStructRoutine routine = ((KStructTypeUnit) type).getRoutine(TEACH_METHOD);
				Object result = null;
				if (routine.parameters.getChildCount() == parameter.length + 1) {
					// a additional parameter is required
					Object[] param = new Object[parameter.length + 1];
					param[0] = this;
					System.arraycopy(parameter, 0, param, 1, param.length - 1);
					result = dfl.execution.executeRoutine(routine, this, param);
				} else {
					// only one parameter is required
					Object[] param = new Object[1];
					param[0] = this;
					result = dfl.execution.executeRoutine(routine, this, param);
				}
				if ((result instanceof Boolean) && ((Boolean) result).booleanValue()) {
					if (m_autoEnter) {
						if (!enterVariable()) {
							KDflLogger.error(this, "KStructVarWrapper enter Method returned false");
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Die Methode prüft, ob eine Variable teachbar ist.
	 * 
	 * @return wahr, wenn die Variable teachbar ist.
	 */
	public boolean isTeachVariable() {
		if (!teachChecked) {
			teachChecked = true;
			KStructType type = skipAlias(getKStructType());
			if (type instanceof KStructTypeUnit) {
				// teaching parameter-variable must be a block, that contains a
				// teach-method
				isTeachVariable = ((KStructTypeUnit) type).getRoutine(TEACH_METHOD) != null;
			}
		}
		return isTeachVariable;
	}

	/**
	 * Writes the new variable declaration with the new init values. For the new
	 * init values are taken the actual values.
	 * 
	 * @return true if the declaration is successfully written
	 */
	public boolean writeBackInitValue() {
		KStructNode var = getRootVariable();
		if ((var != null) && (var.getTcStructuralNode() instanceof TcStructuralVarNode)) {
			if (dfl.client.structure.writeBackInitValues((TcStructuralVarNode) var.getTcStructuralNode())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Writes back the save value.
	 * 
	 * @return true if the save value is successfully written
	 */
	public boolean writeBackSaveValue() {
		KStructNode var = getRootVariable();
		if ((var != null) && (var.getTcStructuralNode() instanceof TcStructuralVarNode)) {
			TcStructuralNode prog = var.getTcStructuralNode().getDeclarationNode();
			if (dfl.client.structure.writeBackSaveValues(prog)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Reads the map target immediately from TeachControl.
	 * 
	 * @return KMapTarget map target
	 */
	public KMapTarget readMapTarget() {
		return readMapTarget(null);
	}

	/**
	 * Reads the map target immediately from TeachControl.
	 * 
	 * @param execRoutine
	 *            only needed for routine local variables
	 * @return KMapTarget KStructVarWrapper, KStructRoutine for a internal
	 *         target or or a String for a external target
	 */
	public KMapTarget readMapTarget(KExecUnitRoutine execRoutine) {
		TcAccessHandle ah = getAccessHandle();
		if ((ah != null)) {
			TcExecutionUnit eu = (execRoutine != null) ? execRoutine.getTcExecutionUnit() : null;
			TcMapTarget mapTarget = new TcMapTarget();
			if (dfl.client.execution.getMapTarget(ah, eu, mapTarget)) {
				if (mapTarget.isInternal()) {
					KStructRoutine routine = null;
					if (mapTarget.isInternalMapToRoutine()) {
						routine = dfl.structure.getKStructRoutine(mapTarget.getRoutine());
						return new KMapTargetInternalRoutine(mapTarget, routine, dfl);
					}
					return new KMapTargetInternal(mapTarget, dfl);
				}
				// is externally mapped
				return new KMapTargetExternal(mapTarget.getExternalMap(), dfl);
			}
		}
		return null;
	}

	TcMapTarget getMapTargetObject() {
		if (target == null) {
			target = new TcMapTarget();
		}
		return target;
	}

	boolean mapTargetChanged() {
		boolean changed = forceMapToChanged;
		forceMapToChanged = false;
		if ((actualMapTarget != null) && !target.isValid()) {
			actualMapTarget = null;
			changed = true;
		} else if (target.isValid()) {
			if (target.isInternalMapToRoutine()) {
				if ((actualMapTarget == null) || !(actualMapTarget instanceof KMapTargetInternalRoutine) || !((KMapTargetInternalRoutine) actualMapTarget).getRoutine().getTcStructuralNode().equals(target.getRoutine())) {
					KStructRoutine routine = dfl.structure.getKStructRoutine(target.getRoutine());
					actualMapTarget = new KMapTargetInternalRoutine(target, routine, dfl);
					changed = true;
				} else if (target.getPath() != null) {
					Object[] tcPath = target.getPath();
					if (!((KMapTargetInternalRoutine) actualMapTarget).compareTcStructuralNodePath(tcPath)) {
						KStructRoutine routine = dfl.structure.getKStructRoutine(target.getRoutine());
						actualMapTarget = new KMapTargetInternalRoutine(target, routine, dfl);
						changed = true;
					}
				}
			} else if (!target.isInternal() && ((actualMapTarget == null) || !(actualMapTarget instanceof KMapTargetExternal) || !((KMapTargetExternal) actualMapTarget).getName().equals(target.getExternalMap()))) {
				actualMapTarget = new KMapTargetExternal(target.getExternalMap(), dfl);
				changed = true;
			} else if (target.isInternal() && (target.getPath() != null)) {
				if ((actualMapTarget == null) || (actualMapTarget instanceof KMapTargetExternal) || (actualMapTarget instanceof KMapTargetInternalRoutine)) {
					actualMapTarget = new KMapTargetInternal(target, dfl);
					changed = true;
				} else {
					Object[] tcPath = target.getPath();
					if (!((KMapTargetInternal) actualMapTarget).compareTcStructuralNodePath(tcPath)) {
						actualMapTarget = new KMapTargetInternal(target, dfl);
						changed = true;
					}
				}
			}
		}
		return changed;
	}

	/**
	 * Returns the map target.
	 * 
	 * @return KStructVarWrapper, KStructRoutine or a String for a external map
	 *         target
	 */
	public KMapTarget getMapTarget() {
		return actualMapTarget;
	}

	public boolean isExportVariable() {
		return (getKStructVar() != null) && getKStructVar().isExportVariable();
	}

	private KStructVar getArrayVariable() {
		if (this.isArrayField() && m_rootPath != null) {
			int pos = m_rootPath.length - 1;
			while ((pos >= 0) && (m_rootPath[pos] instanceof Integer)) {
				pos--;
			}
			if ((pos >= 0) && (m_rootPath[pos] instanceof KStructVar)) {
				return (KStructVar) m_rootPath[pos];
			}
		}
		return null;

	}

	public boolean containsAttribute(String key) {
		KStructVar var = null;
		if (isArrayField()) {
			var = getArrayVariable();
		} else {
			var = getKStructVar();
		}
		if (var != null) {
			if (var.containsAttribute(key)) {
				return true;
			}
			KStructType type = var.getKStructType();
			return ((type != null) && (type.containsAttribute(key)));
		}
		return false;
	}

	public Object getAttribute(String key) {
		KStructVar var = getKStructVar();
		if (var != null) {
			if (var.containsAttribute(key)) {
				return var.getAttribute(key);
			}
			KStructType type = var.getKStructType();
			if ((type != null) && (type.containsAttribute(key))) {
				return type.getAttribute(key);
			}
		}
		return null;
	}

	/**
	 * Sets the map target. Target is a teachtalk variable or component.
	 * 
	 * @param target
	 *            map destination
	 * @return true if the target is successfully set
	 */
	public boolean setMapTarget(KMapTarget target) {
		return setMapTarget(null, target);
	}

	/**
	 * Sets the map target. Target is a teachtalk variable or component.
	 * 
	 * @param execRoutine
	 *            only needed for routine local variables
	 * @param target
	 *            map destination
	 * @return true if the target is successfully set
	 */
	public boolean setMapTarget(KExecUnitRoutine execRoutine, KMapTarget target) {
		if (!isExportVariable()) {
			TcExecutionUnit eu = (execRoutine != null) ? execRoutine.getTcExecutionUnit() : null;
			if (target instanceof KMapTargetExternal) {
				return callsAfterChangeTarget(target, dfl.client.execution.setMapTarget(getAccessHandle(), eu, ((KMapTargetExternal) target).getName()));
			}
			if (target instanceof KMapTargetInternalRoutine) {
				TcStructuralRoutineNode rsn = (TcStructuralRoutineNode) ((KMapTargetInternalRoutine) target).getRoutine().getTcStructuralNode();
				Object[] path = ((KMapTargetInternalRoutine) target).getTcComponentPath();
				return callsAfterChangeTarget(target, dfl.client.execution.setMapTarget(getAccessHandle(), eu, path, rsn));
			} else if (target instanceof KMapTargetInternal) {
				Object[] path = ((KMapTargetInternal) target).getTcComponentPath();
				return callsAfterChangeTarget(target, dfl.client.execution.setMapTarget(getAccessHandle(), eu, path));
			}
		}
		return false;
	}

	private boolean callsAfterChangeTarget(KMapTarget target, boolean result) {
		if (result) {
			actualMapTarget = target;
			forceMapToChanged = true;
			String v = getRootPathString();
			int i = v.lastIndexOf(".");
			if (i > 0) {
				KStructVarWrapper var = dfl.variable.createKStructVarWrapper(v.substring(0, i));
				if ((var != null) && var.isMapTo()) {
					var.enterVariable();
					return result;
				}
			}
			enterVariable();
			enterChecked = false; // reload enter table because of change
		}
		return result;
	}

//	public boolean equals(Object _other) {
//		if(_other == null || !(_other instanceof KStructVarWrapper))
//			return false;
//		
//		KStructVarWrapper o = (KStructVarWrapper)_other;
//		boolean res = getRootPathString().equalsIgnoreCase(o.getRootPathString());
//		return res;
//		
//	}

}