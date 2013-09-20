/**
 * 
 */
package com.keba.kemro.kvs.teach.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import com.keba.kemro.kvs.teach.constant.KvtCAttributeKey;
import com.keba.kemro.kvs.teach.data.program.KvtStatementAdministrator;
import com.keba.kemro.kvs.teach.data.project.KvtProject;
import com.keba.kemro.kvs.teach.data.project.KvtProjectAdministrator;
import com.keba.kemro.kvs.teach.util.KvtSystemCommunicator;
import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.structural.KStructGlobal;
import com.keba.kemro.teach.dfl.structural.KStructNode;
import com.keba.kemro.teach.dfl.structural.KStructNodeVector;
import com.keba.kemro.teach.dfl.structural.KStructProgram;
import com.keba.kemro.teach.dfl.structural.KStructProject;
import com.keba.kemro.teach.dfl.structural.KStructSystem;
import com.keba.kemro.teach.dfl.structural.constant.KStructConst;
import com.keba.kemro.teach.dfl.structural.constant.KStructConstEnum;
import com.keba.kemro.teach.dfl.structural.routine.KStructRoutine;
import com.keba.kemro.teach.dfl.structural.type.KStructType;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeArray;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeBool;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeDInt;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeDWord;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeEnum;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeInt;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeLInt;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeMapTo;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeReal;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeRoutine;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeSInt;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeString;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeStruct;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeSubrange;
import com.keba.kemro.teach.dfl.structural.type.KStructTypeUnit;
import com.keba.kemro.teach.dfl.structural.var.KStructVar;
import com.keba.kemro.teach.dfl.structural.var.KStructVarArray;
import com.keba.kemro.teach.dfl.structural.var.KStructVarUnit;
import com.keba.kemro.teach.dfl.value.DWord;
import com.keba.kemro.teach.dfl.value.KMapTarget;
import com.keba.kemro.teach.dfl.value.KMapTargetExternal;
import com.keba.kemro.teach.dfl.value.KMapTargetInternal;
import com.keba.kemro.teach.dfl.value.KStructVarWrapper;

/**
 * @author ltz
 * 
 */
public class DataModel {

	protected static KEnumWrapper	m_invalidValue;
	protected KStructType			content;
	protected KStructNode			scope;
	protected Vector				allNodes;
	protected Hashtable				scopeTable;
	protected String				showInstanceString	= null;
	private Vector<Object>			mList;

	public void setRestrictedScope(KStructVarWrapper variable) {
		KStructNode scopeNew = null;
		if (variable != null) {
			KMapTarget target = variable.readMapTarget();
			String str = variable.getRootPathString();
			if (str != null) {
				int i = str.lastIndexOf('.');
				while ((target == null) && (i > 0)) {
					if (i > 0) {
						str = str.substring(0, i);
						KStructVarWrapper parentvariable = KvtSystemCommunicator.getTcDfl().variable.createKStructVarWrapper(str);
						if (parentvariable != null) {
							target = parentvariable.readMapTarget();
						}
					}
					i = str.lastIndexOf('.');
				}
			}
			if (target instanceof KMapTargetInternal) {
				KStructVarWrapper wrappi = KvtSystemCommunicator.getTcDfl().variable.createKStructVarWrapper(((KMapTargetInternal) target)
						.getVariableComponentPath());
				if (wrappi == null) {
					wrappi = KvtSystemCommunicator.getTcDfl().variable.createKStructVarWrapper(((KMapTargetInternal) target).getTcFullPath());
				}
				if (wrappi != null) {
					variable = wrappi;
				}
			}
			KStructVar root = variable.getRootVariable();
			if (root != null) {
				scopeNew = root.getParent();
			}
		}
		if ((scopeNew == null) || (!scopeNew.equals(scope))) {
			scopeTable = new Hashtable(11);
			scope = scopeNew;
			KStructNode nd = scope;
			while (nd != null) {
				scopeTable.put(nd, nd);
				nd = nd.getParent();
			}
			updateModel(1, -1);
		}
	}

	public int updateModel(int sel) {
		return updateModel(2, sel);
	}

	private int updateModel(int mode, int selection) {
		int actsel = selection;
		if ((allNodes == null) && (((mode == 1) && scope == null)) || ((mode == 2) && (showInstanceString == null))) {
			return actsel;
		}
		Object sel = null;
		if ((selection >= 0) && (selection < getSize())) {
			sel = getElementAt(selection);
		}
		if (allNodes == null) {
			allNodes = new Vector();
			for (int i = 0; i < this.getDataCount(); i++) {
				allNodes.addElement(getData(i));
			}
		}
		removeAll();
		KTcDfl dfl = KvtSystemCommunicator.getTcDfl();

		for (int i = 0; i < allNodes.size(); i++) {
			boolean add = true;
			Object data = allNodes.elementAt(i);
			if ((mode == 1) && (scope != null) && (data instanceof DataElement)) {
				if ((((DataElement) data).value != null) && (isInScope(((DataElement) data).value))) {
					add = true;
				}
			}
			if ((mode == 2) && (showInstanceString != null) && (data instanceof DataElement)) {
				String path = null;
				if (((DataElement) data).value instanceof KStructVar) {
					path = ((KStructVar) ((DataElement) data).value).getPath() + "." + showInstanceString;
				} else if (((DataElement) data).value instanceof SubComponent) {
					path = ((SubComponent) ((DataElement) data).value).getPath() + "." + showInstanceString;
				}
				if (path != null) {
					KStructVarWrapper wrp = dfl.variable.createKStructVarWrapper(path);
					if (wrp != null) {
						Object res = wrp.readActualValue(null);
						add = (res instanceof Boolean) && ((Boolean) res).booleanValue();
					}
				}
			}
			if (add) {
				if (data.equals(sel)) {
					actsel = this.getSize();
				}
				addData(data);
			}
		}
		if ((mode == 1) && (scope == null)) {
			allNodes = null;
			scopeTable = null;
		}
		return actsel;
	}

	/**
	 * @param _data
	 */
	private void addData(Object _data) {
		mList.add(_data);
	}

	/**
	 * @return
	 */
	private int getSize() {
		return getDataCount();
	}

	/**
	 * @param _selection
	 * @return
	 */
	private Object getElementAt(int _selection) {
		return getData(_selection);
	}

	private boolean isInScope(Object obj) {
		if (obj instanceof KStructProject) {
			return scopeTable.get(obj) != null;
		}
		if (obj instanceof KStructNode) {
			return isInScope(((KStructNode) obj).getParent());
		}
		return false;

	}

	public DataModel() {
		this(null);
	}

	public DataModel(Object wrappi) {
		mList = new Vector<Object>();
		if (wrappi instanceof KStructVarWrapper) {
			showInstanceString = (String) ((KStructVarWrapper) wrappi).getAttribute(KvtCAttributeKey.D_SHOW_INSTANCE);
		}
	}

	public void setContentType(KStructType ct) {
		content = ct;
	}

	public void addData(Object obj, int i) {
		// super.addData(obj, i);
		addToList(obj, i);
		if ((content == null) && (obj instanceof DataElement) && (((DataElement) obj).value instanceof KStructVar)) {
			KStructType type = ((KStructVar) ((DataElement) obj).value).getKStructType();
			if (type != null) {
				content = type;
			}
		}
	}

	private void addToList(Object _data, int _index) {
		int j = mList.size();
		if (_index < 0 || _index >= j) {
			mList.addElement(_data);
		} else {
			mList.insertElementAt(_data, _index);
		}
	}

	public void removeAll() {
		// super.removeAll();
		mList.clear();
		content = null;
	}

	public boolean couldBeMember(Object value) {
		if ((content != null) && (value instanceof KStructVar)) {
			KStructType type = ((KStructVar) value).getKStructType();
			while (type != null) {
				if (content.equals(type)) {
					return true;
				} else if (type instanceof KStructTypeUnit) {
					type = ((KStructTypeUnit) type).getBaseUnit();
				} else if (type instanceof KStructTypeMapTo) {
					type = ((KStructTypeMapTo) type).getKStructType();
				} else {
					return false;
				}
			}
			try {
				if (value instanceof KStructVarUnit) {
					String componentList = (String) ((KStructVar) value).getKStructType().getAttribute(KvtCAttributeKey.EXPORTCOMPONENT);
					if (componentList != null) {
						StringTokenizer tok = new StringTokenizer(componentList, ";");
						while (tok.hasMoreElements()) {
							String var = (String) tok.nextElement();
							int p = var.indexOf(":");
							if (p < 0) {
								type = ((KStructVar) ((KStructTypeUnit) ((KStructVarUnit) value).getKStructType()).components.getChild(var,
										false)).getKStructType();
								while (type != null) {
									if (content.equals(type)) {
										return true;
									} else if (type instanceof KStructTypeUnit) {
										type = ((KStructTypeUnit) type).getBaseUnit();
									} else if (type instanceof KStructTypeMapTo) {
										type = ((KStructTypeMapTo) type).getKStructType();
									} else {
										return false;
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}
		return false;
	}

	public KEnumWrapper getNodeForValue(Object act) {
		int cnt = getDataCount();
		for (int i = 0; i < cnt; i++) {
			Object obj = (Object) getData(i);
			if (obj instanceof KEnumWrapper) {
				KEnumWrapper wrap = (KEnumWrapper) obj;
				if (act instanceof Number) {
					if (wrap.equalsNumber((Number) act)) {
						return wrap;
					}
				} else if (act instanceof KEnumWrapper) {
					if (wrap.equals(act)) {
						return wrap;
					}
				}
			}
		}
		return m_invalidValue;
	}

	/**
	 * @return
	 */
	private int getDataCount() {
		return mList.size();
	}

	public static DataModel getScopelessVariableList(KStructNode scope, DataModel list, KStructType varType, boolean isReference,
			KStructProgram program, String targetVarName) {
		if (list == null) {
			list = new DataModel();
		}
		KStructNode projectScope = scope;
		while (!(projectScope instanceof KStructProject) && (projectScope != null)) {
			projectScope = projectScope.getParent();
		}
		KStructNode originScope = scope;
		HashVector variables = new HashVector();
		HashVector result = new HashVector();
		KTcDfl d = KvtSystemCommunicator.getTcDfl();
		scope = projectScope;

		if (d != null) {
			KStructType formalParameter = KvtStatementAdministrator.skipAlias(varType);
			String vartype = null;
			if (formalParameter != null) {
				vartype = formalParameter.getKey();
			}
			Enumeration<KStructProject> en = getLoadedProjectList();
			while (en.hasMoreElements()) {
				scope = (KStructProject) en.nextElement();
				int cc = ((KStructProject) scope).variables.getChildCount();
				for (int i = 0; i < cc; i++) {
					KStructVar elem = (KStructVar) ((KStructProject) scope).variables.getChild(i);
					KStructVarWrapper w = d.variable.createKStructVarWrapper(elem);
					String key = elem.getKey();
					if (!(variables.containsElement(key))) {
						if (checkType(w, varType, isReference)) {
							KvtHideTable table = KvtHideTable.checkInstanceVariable(w, true);
							if ((table == null) || (!(table.getHideValue() instanceof Boolean))
									|| (!((Boolean) table.getHideValue()).booleanValue())) {
								variables.addElement(elem);
							}
						} else if ((vartype != null) && (elem.getKStructType() instanceof KStructTypeUnit)
								|| (elem.getKStructType() instanceof KStructTypeStruct)) {
							getSubNode(elem, vartype, variables);
						}
					}
				}
				copyAndClear(originScope, variables, result, scope, program, targetVarName);
				boolean isProjectAllowed = /* !KvtSystem.isIMMHandling || */"_global".equalsIgnoreCase(scope.getKey())
						|| "_system".equalsIgnoreCase(scope.getKey());
				for (int i = 0; i < ((KStructProject) scope).programs.getChildCount(); i++) {
					KStructProgram prog = (KStructProgram) ((KStructProject) scope).programs.getChild(i);
					if (isProjectAllowed || prog.equals(originScope)) {
						for (int x = 0; x < ((KStructProgram) prog).variables.getChildCount(); x++) {
							KStructVar elem = (KStructVar) ((KStructProgram) prog).variables.getChild(x);
							if (elem.isPublic() || (elem.containsAttribute(KvtCAttributeKey.COMBOBOX_VALUE))) {
								KStructVarWrapper w = d.variable.createKStructVarWrapper(elem);
								String key = elem.getKey();
								if (!(variables.containsElement(key))) {
									if (checkType(w, varType, isReference)) {
										variables.addElement(elem);
									}
									if ((vartype != null) && (elem.getKStructType() instanceof KStructTypeUnit)
											|| (elem.getKStructType() instanceof KStructTypeStruct)) {
										getSubNode(elem, vartype, variables);
									}
								}
							}
						}
					}
					copyAndClear(originScope, variables, result, prog, program, targetVarName);
				}
			}
		}
		copyAndClear(originScope, variables, result, scope, program, targetVarName);
		if (result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				list.addData(result.elementAt(i));
			}
		}
		return list;
	}

	private static void getSubNode(KStructVar elem, String type, HashVector variables) {
		KStructType varType = elem.getKStructType();
		String componentList = (String) varType.getAttribute(KvtCAttributeKey.EXPORTCOMPONENT);
		if (componentList != null) {
			StringTokenizer tok = new StringTokenizer(componentList, ";");
			while (tok.hasMoreElements()) {
				String var = (String) tok.nextElement();
				int p = var.indexOf(":");
				if (p > 0) {
					var = var.substring(p + 1);
				}
				KStructNodeVector children = null;
				if (varType instanceof KStructTypeStruct) {
					children = ((KStructTypeStruct) varType).components;
				} else if (varType instanceof KStructTypeUnit) {
					children = ((KStructTypeUnit) varType).components;
				}
				if (children != null) {
					for (int i = 0; i < children.getChildCount(); i++) {
						KStructNode node = children.getChild(i);
						if (node.getKey().equals(var) && checkType(((KStructVar) node).getKStructType(), type)) {
							variables.addElement(new SubComponent(elem, (KStructVar) node));
						}
					}

				}
			}
		}
	}

	private static boolean checkType(KStructType actType, String typ) {
		if ("ANY".equalsIgnoreCase(typ)) {
			return true;
		}
		if ((actType != null) && (typ != null)) {
			if (typ.equals(actType.getKey())) {
				return true;
			}
			if (actType instanceof KStructTypeUnit) {
				return checkType(((KStructTypeUnit) actType).getBaseUnit(), typ);
			}
		}
		return false;
	}

	/**
	 * check if actual value matches formal value
	 * 
	 * @param actualValue
	 * @return true if value matches type
	 */
	protected static boolean checkType(Object actualValue, KStructType type, boolean isReference) {
		KStructType formalParameter = KvtStatementAdministrator.skipAlias(type);
		if ((actualValue instanceof KStructVarWrapper) && (((KStructVarWrapper) actualValue).getKStructType() == null)) {
			Object val = ((KStructVarWrapper) actualValue).getActualValue();
			if (val == null) {
				val = ((KStructVarWrapper) actualValue).readActualValue(null);
			}
			if (val != null) {
				actualValue = val;
			}
		}

		if (formalParameter == null) {
			return false;
		}
		if (formalParameter.getKey().equalsIgnoreCase("ANY")) {
			return true;
		}

		if ((formalParameter instanceof KStructTypeBool) && (actualValue instanceof Boolean)) {
			return true;
		}
		if (isReference) {
			if (checkIntType(formalParameter, actualValue)) {
				return true;
			}
		} else {
			if ((formalParameter instanceof KStructTypeSInt) && ((actualValue instanceof Byte))) {
				return true;
			}
			if ((formalParameter instanceof KStructTypeInt) && ((actualValue instanceof Short) || (actualValue instanceof Byte))) {
				return true;
			}
			if ((formalParameter instanceof KStructTypeDWord) && ((actualValue instanceof DWord) || (actualValue instanceof DWord))) {
				return true;
			}
			if ((formalParameter instanceof KStructTypeDInt)
					&& ((actualValue instanceof Integer) || (actualValue instanceof Short) || (actualValue instanceof Byte))) {
				return true;
			}
			if ((formalParameter instanceof KStructTypeLInt)
					&& ((actualValue instanceof Long) || (actualValue instanceof Integer) || (actualValue instanceof Short) || (actualValue instanceof Byte))) {
				return true;
			}
		}
		if ((formalParameter instanceof KStructTypeReal) && (actualValue instanceof Float)) {
			return true;
		}
		if ((formalParameter instanceof KStructTypeString) && (actualValue instanceof String)) {
			return true;
		}
		if ((formalParameter instanceof KStructTypeEnum) && (actualValue instanceof KStructConstEnum)) {
			KStructConst c = (KStructConst) actualValue;
			if (c.getParent() instanceof KStructType) {
				KStructType t = KvtStatementAdministrator.skipAlias((KStructType) c.getParent());
				return (formalParameter.equals(t));
			}
		}
		if ((formalParameter instanceof KStructTypeSubrange)
				&& ((actualValue instanceof Integer) || (!isReference && (actualValue instanceof Short) || (actualValue instanceof Byte)))) {
			return true;
		}
		KStructType t = null;
		if (actualValue instanceof KStructVarWrapper) {
			t = ((KStructVarWrapper) actualValue).getKStructType();
		} else if (actualValue instanceof KStructType) {
			t = (KStructType) actualValue;
		} else if (actualValue instanceof KStructConst) {
			t = ((KStructConst) actualValue).getKStructType();
		} else if (actualValue instanceof KStructRoutine) {
			t = ((KStructRoutine) actualValue).getReturnType();
		} else if (actualValue instanceof KStructTypeRoutine) {
			t = ((KStructTypeRoutine) actualValue).getReturnType();
		}
		t = KvtStatementAdministrator.skipAlias(t);
		if (t instanceof KStructTypeArray) {
			t = KvtStatementAdministrator.skipAlias(((KStructTypeArray) t).getArrayElementKStructType());
		}
		if (t == null) {
			return false;
		}

		if ((formalParameter instanceof KStructTypeBool) && (t instanceof KStructTypeBool)) {
			return true;
		}
		if (isReference) {
			if (checkIntType(formalParameter, t)) {
				return true;
			}
		} else {
			if ((formalParameter instanceof KStructTypeDInt) && ((t instanceof KStructTypeDInt))) {
				return true;
			}
			if ((formalParameter instanceof KStructTypeLInt) && ((t instanceof KStructTypeLInt))) {
				return true;
			}
		}
		if ((formalParameter instanceof KStructTypeReal) && (t instanceof KStructTypeReal)) {
			return true;
		}
		if ((formalParameter instanceof KStructTypeString) && (t instanceof KStructTypeString)) {
			return true;
		}
		if ((formalParameter instanceof KStructTypeEnum) && (t instanceof KStructTypeEnum) && type.equals(t)) {
			return true;
		}
		if ((formalParameter instanceof KStructTypeSubrange) && (t instanceof KStructTypeSubrange) && type.equals(t)) {
			return true;
		}
		if (formalParameter.equals(t)) {
			return true;
		}
		if ((formalParameter instanceof KStructTypeUnit) && (t instanceof KStructTypeUnit)) {
			KStructTypeUnit unit = ((KStructTypeUnit) t).getBaseUnit();

			while (unit != null) {
				if (unit.equals(formalParameter)) {
					return true;
				}
				unit = unit.getBaseUnit();
			}
		}
		return false;
	}

	protected static boolean checkIntType(KStructType formalType, Object value) {
		if (formalType instanceof KStructTypeSInt) {
			return value instanceof Byte;
		}
		if (formalType instanceof KStructTypeInt) {
			return value instanceof Short;
		}
		if (formalType instanceof KStructTypeDInt) {
			return value instanceof Integer;
		}
		if (formalType instanceof KStructTypeLInt) {
			return value instanceof Long;
		}
		return false;
	}

	private static void copyAndClear(KStructNode originScope, HashVector source, HashVector target, KStructNode scope, KStructProgram program,
			String targetVarName) {
		if (source.size() > 0) {
			source.sort();
			if (source != null) {
				// Image icon = null;
				String text = "   ";
				// if (scope instanceof KStructSystem) {
				// // target.addElement(new UnselectableElement("[SYSTEM]"));
				// icon = KvtVarMonitorController.SYSTEM_ICON;
				// text = "S|";
				// } else if (scope instanceof KStructGlobal) {
				// // target.addElement(new UnselectableElement("[GLOBAL]"));
				// icon = KvtVarMonitorController.GLOBAL_ICON;
				// text = "G|";
				// } else if (scope instanceof KStructProject) {
				// // target.addElement(new UnselectableElement("[PROJECT]"));
				// icon = KvtVarMonitorController.PROJECT_ICON;
				// text = "P|";
				// } else if (scope instanceof KStructProgram) {
				// // target.addElement(new
				// // UnselectableElement("["+scope.getKey()+"]"));
				// icon = KvtVarMonitorController.LOCAL_ICON;
				// text = "L|";
				// }
				int l = source.size();
				for (int i = 0; i < l; i++) {
					// if (icon != null) {
					Object obj = source.elementAt(i);
					if ((obj instanceof KStructVarArray)
							&& ("true".equalsIgnoreCase((String) ((KStructVarArray) obj).getAttribute(KvtCAttributeKey.EXPAND_FIELDS)))) {
						KStructTypeArray arr = (KStructTypeArray) skipMapToType(((KStructVarArray) obj).getKStructType());
						int lb = arr.getLowerBound().intValue();
						int ub = arr.getUpperBound().intValue();
						for (int x = lb; x <= ub; x++) {
							addTargetNode(target, originScope, obj,/* icon, */"[" + x + "]", text, program, targetVarName);
						}
					} else {
						addTargetNode(target, originScope, obj, /* icon, */null, text, program, targetVarName);
					}
					// } else {
					// target.addElement(source.elementAt(i));
					// }
				}
				source.removeAllElements();
			}
		}
	}

	/**
	 * @param _target
	 * @param _originScope
	 * @param _obj
	 * @param _icon
	 * @param _arraytext
	 * @param iconToText
	 * @param _program
	 * @param _targetVarName
	 */
	private static void addTargetNode(HashVector _target, KStructNode _originScope, Object _obj, /*
																								 * Image
																								 * _icon
																								 * ,
																								 */String _arraytext, String _iconToText,
			KStructProgram _program, String _targetVarName) {
		// if ((_obj instanceof KStructVar) && ((KStructVar)
		// _obj).containsAttribute(KvtCAttributeKey.COMBOBOX_VALUE)) {
		// if ((_program != null) && (_program.equals(((KStructVar)
		// _obj).getParent())) && (_targetVarName != null)
		// && _targetVarName.equalsIgnoreCase((String) ((KStructVar)
		// _obj).getAttribute(KvtCAttributeKey.COMBOBOX_VALUE))) {
		// _target.addElement(new ActiveIconElement(_originScope,
		// EDIT_IN_SCOPE_ICON, _obj, _arraytext, "V|"));
		// }
		// } else {
		_target.addElement(new DataElement(_originScope, _obj, _arraytext, _iconToText));
		// }
	}

	/**
	 * @return
	 */
	private static Enumeration<KStructProject> getLoadedProjectList() {
		Vector<KStructProject> projects = new Vector<KStructProject>();

		KTcDfl d = KvtSystemCommunicator.getTcDfl();
		if (d != null) {

			synchronized (d.getLockObject()) {
				KvtProject[] prjList = KvtProjectAdministrator.getAllProjects();
				KStructProject global = null;
				KStructProject system = null;
				for (int i = 0; i < prjList.length; i++) {
					if (prjList[i].getProjectState() == KvtProject.SUCCESSFULLY_LOADED) {
						if (prjList[i].isGlobalProject()) {
							global = (KStructProject) prjList[i].getStructProject();
						} else if (prjList[i].isSystemProject()) {
							system = (KStructProject) prjList[i].getStructProject();
						} else {
							projects.addElement((KStructProject) prjList[i].getStructProject());
						}
					}
				}
				if (global != null) {
					projects.addElement(global);
				}
				if (system != null) {
					projects.addElement(system);
				}
			}
		}
		return projects.elements();
	}

	public static DataModel createMapToModel(KStructVarWrapper variable, KStructVarWrapper scopeVariable, String modelMaskVar) {
		KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
		if (dfl == null)
			return null;
		DataModel data = new DataModel(variable);
		// search all variables with the same type at correct scope
		KStructType mapToType = getKStructType(variable);// .getKStructType();
		KStructProgram program = null;
		mapToType = skipMapToType(mapToType);
		KStructNode scope = null;
		KStructVar root = variable.getRootVariable();
		if (root != null) {
			scope = root.getParent();
			if (scope instanceof KStructProgram) {
				program = (KStructProgram) scope;
			}

		}
		String projectName = "_system";
		KvtProject proj = KvtProjectAdministrator.getProject(projectName);
		if ((proj != null) && (proj.getStructProject() != null)) {
			scope = proj.getStructProject();
		}
		// if (KvtSystem.isIMMHandling) {
		// KvtProgram prg = getCurrentRunningProgram();
		// if (prg != null) {
		// scope = prg.getStructProgram();
		// }
		// }
		if (scopeVariable != null) {
			KMapTarget target = scopeVariable.readMapTarget();
			if (target instanceof KMapTargetInternal) {
				KStructVarWrapper wrappi = KvtSystemCommunicator.getTcDfl().variable.createKStructVarWrapper(((KMapTargetInternal) target)
						.getVariableComponentPath());
				if (wrappi == null) {
					wrappi = KvtSystemCommunicator.getTcDfl().variable.createKStructVarWrapper(((KMapTargetInternal) target).getTcFullPath());
				}
				if (wrappi != null) {
					scopeVariable = wrappi;
				}
			}
			KStructVar rt = scopeVariable.getRootVariable();
			if (rt != null) {
				scope = rt.getParent();
			}
		}
		if (scope != null && mapToType != null) {
			data.setContentType(mapToType);
			if (scopeVariable == null) {
				long t1 = System.currentTimeMillis();
				data = getScopelessVariableList(scope, data, mapToType, true, program, variable.getKey());
				long t2 = System.currentTimeMillis() - t1;
				// System.out.println("getScopelessVariableList took " + t2 +
				// " ms!");
			} else {
				// data = KvtVarTreeNode.getVariableList(scope, data, mapToType,
				// true, null);
			}
			// delete the own struct var from the list of possible map to's
			KStructVar myVar = variable.getKStructVar();
			DataModel dataFiltered = new DataModel(variable);
			dataFiltered.setContentType(mapToType);
			if (modelMaskVar != null) {
				if (modelMaskVar.startsWith("INSTANCE.")) {
					modelMaskVar = modelMaskVar.substring(8);
				}
				if (!modelMaskVar.startsWith(".")) {
					modelMaskVar = "." + modelMaskVar;
				}
			}
			boolean add = true;
			for (int i = 0; i < data.getSize(); i++) {
				add = true;
				Object elem = data.getElementAt(i);
				// if (elem instanceof ActiveIconElement) {
				// KStructVar kstructvar = (KStructVar) ((ActiveIconElement)
				// elem).getValue();
				// KStructVarWrapper wrp = (KStructVarWrapper)
				// varCache.get(kstructvar.getPath());
				// ((ActiveIconElement) elem).setWrapper(variable);
				// ((ActiveIconElement) elem).showUserValue(showUserValue);
				// if ((wrp != null) && (variable != null)) {
				// if (connector == null) {
				// connector = new Hashtable();
				// }
				// connector.put(wrp, variable);
				// if (variable.isMapTo()) {
				// connector.put(variable, wrp);
				// }
				// }
				// }
				if (elem instanceof DataElement) {
					elem = ((DataElement) elem).getValue();
				}
				if (elem instanceof KStructVar) {
					KvtHideTable table = KvtHideTable.checkVariable((KStructVar) elem);
					if (table != null) {
						if (table.getHideValue() instanceof Boolean) {
							add = !((Boolean) table.getHideValue()).booleanValue();
						} else {
							table.displayElement(((KStructVar) elem).getKey());
						}
					}
					if (add && modelMaskVar != null) {
						KStructVarWrapper wrappi = dfl.variable.createKStructVarWrapper(((KStructVar) elem).getPath() + modelMaskVar);
						if (wrappi != null) {
							Object val = wrappi.readActualValue(null);
							if (val instanceof Boolean) {
								add = ((Boolean) val).booleanValue();
							}
						}
					}
				}
				if (add && !elem.equals(myVar)) {
					dataFiltered.addData(data.getElementAt(i));
				}
			}
			data = dataFiltered;
		}

		// add the map target if it is not a member of the variable list
		KMapTarget mapTarget = variable.readMapTarget();
		if (mapTarget instanceof KMapTargetExternal) {
			// add the path of the external map target to the list
			data.addData(((KMapTargetExternal) mapTarget).getName(), 0);
			// data.addData(KvtTranslation.getTranslationText(PARAMETERMASK_VAR_LABEL),
			// 1);
		} else if (mapTarget instanceof KMapTargetInternal) {
			Object[] mapTargetComponentPath = ((KMapTargetInternal) mapTarget).getVariableComponentPath();
			if (mapTargetComponentPath != null && mapTargetComponentPath.length > 0) {
				if (mapTargetComponentPath.length == 1) {
					String mapTargetFullPath = dfl.structure.getFullPath(mapTargetComponentPath);
					if (mapTargetFullPath != null) {
						boolean found = false;
						for (int i = 0; i < data.getSize(); i++) {
							Object elem = data.getElementAt(i);
							if (elem instanceof DataElement) {
								elem = ((DataElement) elem).getValue();
							}
							if (elem instanceof KStructVar) {
								if (mapTargetFullPath.equals(((KStructVar) elem).getPath())) {
									found = true;
									break;
								}
							}
						}
						// no internal variable was found which is identical
						// with the map target
						// -> add the map target to the list
						if (!found) {
							data.addData(mapTargetFullPath, 0);
							// data.addData(KvtTranslation.getTranslationText(PARAMETERMASK_VAR_LABEL),
							// 1);
						}
					}
				} else {
					// map target is a member of a struct, array, or unit and is
					// not found in the variable list
					// -> add the map target path to the list
					String structPath = dfl.structure.convertPath(mapTargetComponentPath);
					if (structPath == null) {
						structPath = "";
					}

					data.addData(structPath, 0);

					// data.addData(structPath, 0);
					// data.addData(KvtTranslation.getTranslationText(PARAMETERMASK_VAR_LABEL),
					// 1);
				}
			} else {
				// no map target component path exist (mapping end user variable
				// to none end user variable)
				// -> add the variable path of TcStructualNode to the list
				String tcFullPath = ((KMapTargetInternal) mapTarget).getTcFullPath();
				if (tcFullPath == null) {
					tcFullPath = "";
				}
				data.addData(tcFullPath, 0);
				// data.addData(tcFullPath, 0);
				// data.addData(KvtTranslation.getTranslationText(PARAMETERMASK_VAR_LABEL),
				// 1);
			}
		}
		return data;
	}

	/**
	 * @param _mapToType
	 * @return
	 */
	private static KStructType skipMapToType(KStructType _mapToType) {
		if (_mapToType instanceof KStructTypeMapTo) {
			return skipMapToType(((KStructTypeMapTo) _mapToType).getKStructType());
		}
		return _mapToType;
	}

	/**
	 * @param _variable
	 * @return
	 */
	private static KStructType getKStructType(KStructVarWrapper _variable) {
		KStructType vartype = KvtStatementAdministrator.skipAlias(_variable.getKStructType());
		KTcDfl dfl = KvtSystemCommunicator.getTcDfl();
		if (vartype == null && dfl != null) {
			KMapTarget target = _variable.readMapTarget();
			if (target instanceof KMapTargetInternal) {
				KStructVarWrapper wrapper = dfl.variable.createKStructVarWrapper(((KMapTargetInternal) target).getVariableComponentPath());
				if (wrapper == null) {
					wrapper = dfl.variable.createKStructVarWrapper(((KMapTargetInternal) target).getTcFullPath());
				}
				if (wrapper != null) {
					vartype = wrapper.getKStructType();
				}
			}
		}
		return vartype;
	}

	/**
	 * @param _i
	 * @return
	 */
	private Object getData(int _i) {
		if (mList != null && !mList.isEmpty() && _i >= 0)
			return mList.elementAt(_i);
		return null;
	}

	// ****************************
	// NESTED CLASSES
	// ****************************

	private static class KEnumWrapper {
		protected KStructConstEnum	m_enum;
		protected boolean			m_invalid;

		public KEnumWrapper(KStructConstEnum constEnum, boolean isInvalid) {
			m_enum = constEnum;
			m_invalid = isInvalid;
		}

		public KEnumWrapper(KStructConstEnum constEnum) {
			this(constEnum, false);
		}

		@Override
		public String toString() {
			return m_enum.getKey();
		}

		public Object getInitValue() {
			return m_enum.getInitValue();
		}

		public KStructConstEnum getKStructConstEnum() {
			return m_enum;
		}

		public boolean equalsNumber(Number o) {
			if (o != null) {
				return o.intValue() == ((Number) m_enum.getInitValue()).intValue();
			}
			return false;
		}

		public int intValue() {
			Object obj = getInitValue();
			if (obj instanceof Number) {
				return ((Number) obj).intValue();
			}
			return 0;
		}

	}

	public static class DataElement {
		public Object		value;
		// public Image image;
		public KStructNode	scope;
		public int			arrayIndex;
		public String		arrayName;
		private String		text;
		private String		shortText;

		public DataElement(KStructNode scope, /* Image image, */Object value, String arrayPath, String iconTxt) {
			// this.image = image;
			this.value = value;
			this.scope = scope;
			this.text = iconTxt;
			arrayIndex = -1;
			if (arrayPath != null) {
				try {
					int start = arrayPath.indexOf("[") + 1;
					int end = arrayPath.indexOf("]");
					arrayIndex = Integer.parseInt(arrayPath.substring(start, end));
					if (value instanceof KStructVar) {
						arrayName = ((KStructVar) value).getKey() + "[" + arrayIndex + "]";
					}
				} catch (Exception e) {
					// nothing to do
				}
			}

			if (value instanceof SubComponent) {
				shortText = value.toString();
			} else {
				this.shortText = getString();
				if (scope instanceof KStructSystem) {
					int index = shortText.lastIndexOf('.');
					if (index > 0) {
						index = shortText.lastIndexOf('.', index - 1);
						if (index > 0) {
							shortText = shortText.substring(index + 1);
							return;
						}
					}
				}
				int index = shortText.lastIndexOf('.');
				if (index > 0) {
					shortText = shortText.substring(index + 1);
				}
			}
		}

		public String getVariableKey() {
			return getString();
		}

		public String getStringRepresentation() {
			return text;
		}

		public int getArrayIndex() {
			return arrayIndex;
		}

		public String getArrayName() {
			return arrayName;
		}

		// public Image getIcon() {
		// return image;
		// }

		public Object getValue() {
			return value;
		}

		private String getString() {
			if ((scope != null) && (value instanceof KStructVar)) {
				KStructNode node = ((KStructVar) value).getParent();
				if (node == scope) {
					return addArrayString(value.toString());
				}
				if (node instanceof KStructProgram) {
					KStructNode prj = node.getParent();
					if (prj == scope) {
						return addArrayString(node.toString() + "." + value.toString());
					}
					if ((prj instanceof KStructGlobal) || (prj instanceof KStructSystem)) {
						return addArrayString(value.toString());
					}
					return addArrayString(prj.toString() + "." + node.toString() + "." + value.toString());
				}
				if ((node instanceof KStructGlobal) || (node instanceof KStructSystem)) {
					return addArrayString(value.toString());
				}
				return addArrayString(node.toString() + "." + value.toString());
			}
			return addArrayString(value.toString());
		}

		@Override
		public String toString() {
			// return KvtTranslation.getSourceTranslationText(shortText);
			return shortText;
		}

		public String getShortText() {
			return shortText;
		}

		private boolean isArray() {
			return (value instanceof KStructVarArray);
		}

		private String addArrayString(String text) {
			if (isArray()) {
				if (arrayIndex >= 0) {
					return text + "[" + arrayIndex + "]";
				}
				return text + "[n]";
			}
			return text;
		}

		public boolean isActiveElement() {
			return false;
		}

		public Object getBackground() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	private static class SubComponent {
		private KStructVar	par, sub;

		public SubComponent(KStructVar parent, KStructVar component) {
			par = parent;
			sub = component;
		}

		@Override
		public String toString() {
			return par.getKey() + "." + sub.getKey();
		}

		public String getPath() {
			return par.getPath() + "." + sub.getKey();
		}

		public boolean equals(Object parent, Object component) {
			return par.equals(parent) && sub.equals(component);
		}

	}

	private static class HashVector {
		private Hashtable	ht	= new Hashtable();
		private Vector		vt	= new Vector();

		public HashVector() {
			super();
		}

		public int size() {
			return vt.size();
		}

		public Object elementAt(int index) {
			return vt.elementAt(index);
		}

		private boolean containsElement(String key) {
			return ht.contains(key);
		}

		public void addElement(Object obj) {
			String key = obj.toString();
			if (!ht.containsKey(key)) {
				ht.put(key, obj);
				vt.addElement(obj);
			}

			// vt.addElement(obj);
			// if (obj instanceof KStructVar) {
			// String varKey = ((KStructVar) obj).getKey();
			// if (varKey != null) {
			// ht.put(varKey, obj);
			// }
			// }

		}

		public void removeAllElements() {
			vt.removeAllElements();
			ht.clear();
		}

		public void sort() {
			int r = vt.size();
			Object[] elems = new Object[r];
			vt.copyInto(elems);
			int gap;
			int counter;
			int pointer;
			Object temp;
			gap = r / 2;
			while (gap > 0) {
				for (counter = gap; counter < r; counter++) {
					pointer = counter - gap;
					while (pointer >= 0) {
						String var1 = (elems[pointer]).toString().toLowerCase();
						String var2 = (elems[gap + pointer]).toString().toLowerCase();
						if (var1.compareTo(var2) > 0) {
							temp = elems[pointer];
							elems[pointer] = elems[gap + pointer];
							elems[gap + pointer] = temp;
							pointer = pointer - gap;
						} else {
							pointer = -1;
						}
					}
				}
				gap = gap / 2;
			}
			vt.removeAllElements();
			for (int i = 0; i < elems.length; i++) {
				vt.addElement(elems[i]);
			}
		}
	}

	/**
	 * @return
	 * 
	 */
	public List<Object> getData() {
		return mList;
	}
}
