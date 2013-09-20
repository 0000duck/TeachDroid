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
package com.keba.kemro.teach.dfl.structural.var;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.routine.*;
import com.keba.kemro.teach.dfl.structural.type.*;
import com.keba.kemro.teach.network.*;

/**
 * Strukturknoten für eine Variable.
 */
public class KStructVar extends KStructNode {
	/** Konstante für Variablenkennzeichnung */
	public final static byte VAR = TcStructuralVarNode.VAR_KIND;
	/** Constant Variable */
	public final static byte CONST_VAR = TcStructuralVarNode.CONST_VAR_KIND;
	/** Konstante für Parameterkennzeichnung */
	public final static byte PARAM = TcStructuralVarNode.PARAM_KIND;
	/** Konstante für eine konstante Parameterkennzeichnung */
	public final static byte CONST_PARAM = TcStructuralVarNode.CONST_PARAM_KIND;
	/** Konstante für Wert-Parameterkennzeichnung */
	public final static byte VALUE_PARAM = TcStructuralVarNode.VALUE_PARAM_KIND;
	/** DOCUMENT ME! */
	private final static int BIT_MASK_IS_COMPONENT = KStructNode.BIT_MASK_LAST * 2;
	/** DOCUMENT ME! */
	private final static int BIT_MASK_IS_MAP_TO = KStructNode.BIT_MASK_LAST * 4;
	/** DOCUMENT ME! */
	private final static int BIT_MASK_IS_SAVE = KStructNode.BIT_MASK_LAST * 8;
	/** DOCUMENT ME! */
	private final static int BIT_MASK_IS_READONLY = KStructNode.BIT_MASK_LAST * 16;
	/** DOCUMENT ME! */
	private final static int BIT_MASK_IS_EXPORT = KStructNode.BIT_MASK_LAST * 32;

	/** Kennzeichnung der Variable */
	protected byte kind;
	/** Strukturtyp der Variable */
	protected KStructType type;

	KStructVar(String key, int visibility, byte kind, boolean isMapTo, boolean isComponent, KStructType type, KTcDfl dfl) {
		super(key, dfl);
		this.setAllowsChildren(false);
		this.setLoaded(true);
		this.setVisibility(visibility);
		this.kind = kind;
		this.setMapTo(isMapTo);
		this.setComponent(isComponent);
		this.type = type;
	}

	protected void setTcStructuralNode(TcStructuralNode n) {
		super.setTcStructuralNode(n);
		if (n instanceof TcStructuralVarNode) {
			setReadOnly(((TcStructuralVarNode) n).isReadOnly());
			setExportVariable(((TcStructuralVarNode) n).isExportVariable());
		}
	}

	/**
	 * Liefert den Strukturtyp
	 * 
	 * @return Strukturtyp
	 */
	public KStructType getKStructType() {
		return type;
	}

	public boolean isConstVariable() {
		return kind == CONST_VAR;
	}

	/**
	 * Prüft die Variable auf eine Parameterkennzeichnung.
	 * 
	 * @return wahr, wenn die Variable ein Parameter ist.
	 */
	public boolean isParameter() {
		boolean isParam = (getParent() instanceof KStructRoutine) || (getParent() instanceof KStructTypeRoutine);
		if (!isParam) {
			return false;
		}
		return ((kind == CONST_PARAM) || (kind == VALUE_PARAM) || (kind == PARAM));
	}

	/**
	 * Prüft die Variable auf eine konstante Parameterkennzeichnung.
	 * 
	 * @return wahr, wenn die Variable ein konstanter Parameter ist.
	 */
	public boolean isConstParameter() {
		return kind == CONST_PARAM;
	}

	/**
	 * Prüft die Variable auf eine Wert-Parameterkennzeichnung.
	 * 
	 * @return wahr, wenn die Variable ein Wert-Parameter ist.
	 */
	public boolean isValueParameter() {
		return kind == VALUE_PARAM;
	}

	/**
	 * Prüft die Variable, ob sie eine MapTo-Variable ist.
	 * 
	 * @return Wahrheitswert
	 */
	public boolean isMapTo() {
		return isBitSet(BIT_MASK_IS_MAP_TO);
	}

	/**
	 * Liefert wahr, wenn es sich um eine SAVE-Variable handelt.
	 * 
	 * @return Wahrheitswert
	 */
	public boolean isSave() {
		return isBitSet(BIT_MASK_IS_SAVE);
	}

	/**
	 * Setzt die Variable als SAVE.
	 * 
	 * @param b
	 *            Wahrheitswert
	 */
	protected void setSave(boolean b) {
		setBit(BIT_MASK_IS_SAVE, b);
	}

	/**
	 * Returns true if the variable is save project.
	 * 
	 * @return true if the variable is save project
	 */
	public boolean isReadOnly() {
		return isBitSet(BIT_MASK_IS_READONLY);
	}

	/**
	 * Setzt die Variable als SAVE.
	 * 
	 * @param b
	 *            Wahrheitswert
	 */
	protected void setReadOnly(boolean b) {
		setBit(BIT_MASK_IS_READONLY, b);
	}

	public boolean isInLibSystem() {
		String path = getDirEntryPath();
		if (path != null) {
			return path.startsWith("\\..\\..\\") || path.startsWith("/../../");
		}
		return false;
	}

	public boolean isExportVariable() {
		return isBitSet(BIT_MASK_IS_EXPORT);
	}

	/**
	 * Setzt die Variable als Export.
	 * 
	 * @param b
	 *            Wahrheitswert
	 */
	protected void setExportVariable(boolean b) {
		setBit(BIT_MASK_IS_EXPORT, b);
	}

	/**
	 * Liefert wahr, wenn die Variable eine Komponente ist.
	 * 
	 * @return Wahrheitswert
	 */
	public boolean isComponent() {
		return isBitSet(BIT_MASK_IS_COMPONENT);
	}

	/**
	 * Returns true if the variable is a optional parameter.
	 * 
	 * @return true for a optional parameter
	 */
	public boolean isOptional() {
		return (getTcStructuralNode() instanceof TcStructuralVarNode) && ((TcStructuralVarNode) getTcStructuralNode()).isOptional();
	}

	/**
	 * Returns true if the variable is referenced.
	 * 
	 * @return true if the variable is referenced
	 */
	public boolean isReferenced() {
		return (getTcStructuralNode() instanceof TcStructuralVarNode) && ((TcStructuralVarNode) getTcStructuralNode()).isReferenced();
	}

	/**
	 * Setzt eine Variable als MapTo-Variable.
	 * 
	 * @param b
	 *            Wahrheitswert
	 */
	protected void setMapTo(boolean b) {
		setBit(BIT_MASK_IS_MAP_TO, b);
	}

	/**
	 * Setzt die Variable als Komponenten-Variable.
	 * 
	 * @param b
	 *            Wahrheitswert
	 */
	protected void setComponent(boolean b) {
		setBit(BIT_MASK_IS_COMPONENT, b);
	}

	/**
	 * Liefert die Art des Knotens
	 * 
	 * @return byte
	 */
	public byte getKind() {
		return kind;
	}

}
