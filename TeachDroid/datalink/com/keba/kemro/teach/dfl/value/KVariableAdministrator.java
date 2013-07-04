package com.keba.kemro.teach.dfl.value;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.type.*;
import com.keba.kemro.teach.dfl.structural.var.*;
import com.keba.kemro.teach.network.*;

public class KVariableAdministrator {
	private KTcDfl dfl;
	KVariableServer server;

	protected KVariableAdministrator(KTcDfl dfl) {
		this.dfl = dfl;
		KVariableGroup.setDFL(dfl);
	}

	protected void init() {
		server = new KVariableServer(dfl);
	}

	protected void stop() {
		server.stop();
	}

	public KVariableGroup createVariableGroup(String sGroupName) {
		return new KVariableGroup(sGroupName);
	}

	public KMapToVariableGroup createMapToVariableGroup(String sGroupName) {
		return new KMapToVariableGroup(sGroupName);
	}

	public KRoutineVariableGroup createRoutineVariableGroup(String sGroupName) {
		return new KRoutineVariableGroup(sGroupName);
	}

	public KMapToRoutineVariableGroup createMapToRoutineVariableGroup(String sGroupName) {
		return new KMapToRoutineVariableGroup(sGroupName);
	}

	/**
	 * Erzeugt für eine Strukturvariable einen neuen Variablenwrapper
	 * 
	 * @param rootVariable
	 *            Strukturvariable
	 * 
	 * @return erzeugter Variablenwrapper
	 */
	public KStructVarWrapper createKStructVarWrapper(KStructVar rootVariable) {
		KStructType t = KStructVarWrapper.skipMapTo(rootVariable.getKStructType());
		KStructVarWrapper wrapper = new KStructVarWrapper(rootVariable, -1, t, dfl,false);
		wrapper.m_rootPath = new Object[1];
		wrapper.m_rootPath[0] = rootVariable;
		return wrapper;
	}

	/**
	 * Erzeugt für eine Variable einen neuen Variablenwrapper. Die Variable wird
	 * durch ihren Pfad spezifiziert.
	 * 
	 * @param fullPath
	 *            Zeichenkette, die den Variablenpfad bezeichnet
	 * 
	 * @return erzeugter Variablenwrapper
	 */
	public KStructVarWrapper createKStructVarWrapper(String fullPath) {
		if (fullPath != null) {
			TcAccessHandle handle = dfl.client.structure.getVarAccessHandle(fullPath);
			if (handle == null) {
				if (fullPath.startsWith("_system.")) {
					fullPath = fullPath.substring(8);
					handle = dfl.client.structure.getVarAccessHandle(fullPath);
				} else {
					fullPath = "_system." + fullPath;
					handle = dfl.client.structure.getVarAccessHandle(fullPath);
				}
			}
			if (handle != null) {
				return createKStructVarWrapper(fullPath, handle);
			}
		}
		return null;
	}

	/**
	 * Erzeugt für eine Variable einen neuen Variablenwrapper. Die Variable wird
	 * durch ihren Pfad spezifiziert. Der Pfad wird durch ein Objektfeld
	 * repräsentiert, wobei die einzelnen Objekte Strukturvariablen sind.
	 * 
	 * @param path
	 *            Feld von Strukturvariablen
	 * 
	 * @return erzeugter Variablenwrapper
	 */
	public KStructVarWrapper createKStructVarWrapper(Object[] path) {
		if ((path == null) || (path.length == 0) || !(path[0] instanceof KStructVar)) {
			return null;
		}
		String fullPath = dfl.structure.getFullPath(path);
		TcAccessHandle handle = null;
		if (fullPath != null) {
			handle = dfl.client.structure.getVarAccessHandle(fullPath);
		}
		if (handle == null) {
			// workaround for routine local variables
			Object[] tcPath = convertKStructNodePath(path);
			handle = dfl.client.structure.getVarAccessHandle(tcPath);
		}
		if (handle != null) {
			return createKStructVarWrapper(fullPath, handle);
		}
		return null;
	}

	private KStructVarWrapper createKStructVarWrapper(String fullPath, TcAccessHandle handle) {
		KStructVarWrapper wrapper = null;

		int kind = handle.getTypeKind();
		if ((kind <= TcStructuralTypeNode.ANY_TYPE) || (kind >= TcStructuralTypeNode.BYTE_TYPE)) {
			if (fullPath.endsWith("]")) {
				int beg = fullPath.lastIndexOf("[");
				int index = Integer.parseInt(fullPath.substring(beg + 1, fullPath.length() - 1));
				wrapper = new KStructVarWrapper(null, index, kind, dfl, true);
				wrapper.m_rootPathString = fullPath;
			} else {
				wrapper = new KStructVarWrapper(null, -1, kind, dfl, false);
				int beg = fullPath.lastIndexOf(".");
				wrapper.key = (0 <= beg) ? fullPath.substring(beg + 1) : fullPath;
				wrapper.m_rootPathString = fullPath;
			}
			wrapper.m_accessHandle = handle;
		}
		return wrapper;
	}

	/**
	 * Converts the KStructNodePath into a TcStructualNodePath
	 * 
	 * @param kStructNodePath
	 *            KStructNodePath which has to be converted
	 * 
	 * @return TcStructualNodePath of the KStructNodePath
	 */
	private Object[] convertKStructNodePath(Object[] kStructNodePath) {
		if (kStructNodePath == null || 0 == kStructNodePath.length) {
			return null;
		}

		Object[] p = null;
		int offset = 0;
		if (((KStructVar) kStructNodePath[0]).getParent() instanceof KStructProgram) {
			p = new Object[kStructNodePath.length + 1];
			p[0] = ((KStructVar) kStructNodePath[0]).getParent().getTcStructuralNode();
			offset = 1;
		} else {
			p = new Object[kStructNodePath.length];
		}
		for (int i = 0; i < kStructNodePath.length; i++) {
			if (kStructNodePath[i] instanceof Integer) {
				p[i + offset] = kStructNodePath[i];
			} else {
				p[i + offset] = ((KStructNode) kStructNodePath[i]).getTcStructuralNode();
			}
		}
		return p;
	}

	public KMapTargetExternal createMapTargetExternal(String name) {
		return new KMapTargetExternal(name, dfl);
	}

	public KMapTargetInternal createMapTargetInternal(String fullPath) {
		return new KMapTargetInternal(fullPath, dfl);
	}

	public KMapTargetInternalRoutine createMapTargetInternalRoutine(String fullPath, String routineFullPath) {
		return new KMapTargetInternalRoutine(fullPath, routineFullPath, dfl);
	}

}
