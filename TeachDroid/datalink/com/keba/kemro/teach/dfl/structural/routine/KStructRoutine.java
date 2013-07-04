package com.keba.kemro.teach.dfl.structural.routine;

import java.util.Enumeration;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.type.*;
import com.keba.kemro.teach.network.*;

/**
 * Strukturbaumelement für Routinen
 */

public class KStructRoutine extends KStructNode {
	/**
	 * Liste mit Parametervariablen
	 */
	public KStructNodeVector parameters = new KStructNodeVector(this);
	/**
	 * Liste mit Routinenlokalen Variablen
	 */
	public KStructNodeVector variables = new KStructNodeVector(this);
	private KStructType returnType = null;
	private Integer routineType = NAMED_ROUTINE;

	/**
	 * Identifier für AT Routine
	 */
	public final static Integer AT_ROUTINE = new Integer(TcStructuralRoutineNode.AT_ROUTINE);
	/**
	 * Identifier für 'normale' Routine
	 */
	public final static Integer NAMED_ROUTINE = new Integer(TcStructuralRoutineNode.NAMED_ROUTINE);
	/**
	 * Identifier für unbenannte Routine
	 */
	public final static Integer UNNAMED_ROUTINE = new Integer(TcStructuralRoutineNode.UNNAMED_ROUTINE);

	/**
	 * Legt einen neue Routine mit den übergebenen Eigenschaften an
	 * 
	 * @param key
	 *            Name
	 * @param visibility
	 *            Sichtbarkeit
	 * @param routineType
	 *            Typ der Routine
	 */
	KStructRoutine(String key, int visibility, int routineType, KTcDfl dfl) {
		super(key, dfl);
		setVisibility(visibility);
		if (routineType == TcStructuralRoutineNode.AT_ROUTINE) {
			this.routineType = AT_ROUTINE;
		} else if (routineType == TcStructuralRoutineNode.NAMED_ROUTINE) {
			this.routineType = NAMED_ROUTINE;
		} else if (routineType == TcStructuralRoutineNode.UNNAMED_ROUTINE) {
			this.routineType = UNNAMED_ROUTINE;
		}
		this.setAllowsChildren(true);
	}

	/**
	 * Liefert true wenn es sich um eine At Routine handelt
	 * 
	 * @return true wenn At Routine
	 */
	public boolean isAtRoutine() {
		return routineType.equals(AT_ROUTINE);
	}

	/**
	 * Liefert true wenn die Routine als ABSTRACT gekennzeichnet ist und
	 * überschrieben werden muss.
	 * 
	 * @return isAbstract
	 */
	public boolean isAbstract() {
		return (ortsStructuralNode instanceof TcStructuralRoutineNode) && ((TcStructuralRoutineNode) ortsStructuralNode).isAbstract();
	}

	/**
	 * Liefert true wenn es sich um eine 'normale' Routine handelt
	 * 
	 * @return true wenn 'normale' Routine
	 */
	public boolean isNamedRoutine() {
		return routineType.equals(NAMED_ROUTINE);
	}

	/**
	 * Liefert true wenn es sich um eine unbenannte Routine handelt
	 * 
	 * @return true bei unbenannten Routinen
	 */
	public boolean isUnnamedRoutine() {
		return routineType.equals(UNNAMED_ROUTINE);
	}

	/**
	 * Lädt alle Kinder nach
	 * 
	 */
	protected void loadChildren() {
		if (!isLoaded() && this.getAllowsChildren()) {
			setLoaded(true);
			dfl.structure.variableFactory.loadRoutineVariables(this);
		}
	}

	/**
	 * Liefert den Return Typ falls definiert
	 * 
	 * @return Der Return Typ der Routine
	 */
	public KStructType getReturnType() {
		if (isAtRoutine()) {
			return null;
		}
		if ((returnType == null) && (ortsStructuralNode != null) && (((TcStructuralRoutineNode) getTcStructuralNode()).getReturnType() != null)) {
			returnType = dfl.structure.getKStructType(((TcStructuralRoutineNode) getTcStructuralNode()).getReturnType());
			if (returnType == null) {
				returnType = dfl.structure.typeFactory.createKStructType(((TcStructuralRoutineNode) getTcStructuralNode()).getReturnType(), dfl);
			}

		}
		return returnType;
	}

	protected void setTcStructuralNode(TcStructuralNode n) {
		super.setTcStructuralNode(n);
	}

	public TcStructuralRoutineNode getTeachPendant() {
		String key = m_nativeKey + "_TEACH";
		TcStructuralNode program = getTcStructuralNode().getDeclarationNode();
		TcStructuralRoutineNode routnode = null;
		Enumeration e = dfl.client.structure.getNodes(program, TcStructuralNode.ROUTINE);
		while ((routnode == null) && e.hasMoreElements()) {
			TcStructuralRoutineNode r = (TcStructuralRoutineNode) e.nextElement();
			if (key.equals(r.getName())) {
				return r;
			}
		}
		return null;
	}
}
