package com.keba.kemro.teach.network.base;

import com.keba.kemro.teach.network.*;

public abstract class TcStructuralAbstractNode implements TcStructuralNode {
	private static final int BIT_MASK_IS_LOADED = 1;
	private static final int BIT_MASK_IS_MARKED = 2;
	private static final int BIT_MASK_IS_USER_NODE = 4;
	private static final int BIT_MASK_HAS_USER_ATTRIBUTE = 8;
	private static final int BIT_MASK_IS_ABSTRACT = 16;
	private static final int BIT_MASK_IS_EXPORT = 32;
	protected static final int BIT_MASK_LAST = 32;

	protected int handle;
	protected String name;
	protected TcStructuralAbstractNode dirEntryNode;
	protected TcStructuralAbstractNode parent;
	protected int bitSet;
	protected byte nodeKind;

	/**
	 * Konstruktor.
	 * 
	 * @param handle
	 *            ist die Identifikation des Strukturbaumknotens im
	 *            TeachControl.
	 */
	protected TcStructuralAbstractNode(int handle) {
		this.handle = handle;
	}

	/**
	 * Liefert den TeachControl - Handle zur�ck.
	 * 
	 * @return TeachControl - Handle
	 */
	protected int getHandle() {
		return handle;
	}

	/**
	 * Setzt das Bit in der Bit-Feld.
	 * 
	 * @param bitMask
	 *            Bit-Maske
	 * @param b
	 *            true f�r ein gesetztes Bit
	 */
	protected final void setBit(int bitMask, boolean b) {
		bitSet = (b ? (bitSet | bitMask) : (bitSet & ~bitMask));
	}

	/**
	 * Liefert den Wert des Bits.
	 * 
	 * @param bitMask
	 *            Bit-Maske
	 * 
	 * @return true f�r das gesetzte Bit
	 */
	protected final boolean isBitSet(int bitMask) {
		return (bitSet & bitMask) == bitMask;
	}

	protected boolean isLoaded() {
		return isBitSet(BIT_MASK_IS_LOADED);
	}

	protected void setLoaded(boolean b) {
		setBit(BIT_MASK_IS_LOADED, b);
	}

	/**
	 * Liefert true zur�ck, wenn das Objekt markiert ist.
	 * 
	 * @return true f�r markiert
	 */
	protected boolean isMarked() {
		return isBitSet(BIT_MASK_IS_MARKED);
	}

	/**
	 * Markiert das Objekt.
	 * 
	 * @param b
	 *            true f�r das Markieren
	 */
	protected void setMarked(boolean b) {
		setBit(BIT_MASK_IS_MARKED, b);
	}

	public boolean isUserNode() {
		if (isLoaded() || loadInfo()) {
			return isBitSet(BIT_MASK_IS_USER_NODE);
		}
		return false;

	}

	/**
	 * Markiert das Objekt.
	 * 
	 * @param b
	 *            true f�r das Markieren
	 */
	protected void setUserNode(boolean b) {
		setBit(BIT_MASK_IS_USER_NODE, b);
	}

	/**
	 * Markiert das Objekt.
	 * 
	 * @param b
	 *            true f�r das Markieren
	 */
	protected void setAbstract(boolean b) {
		setBit(BIT_MASK_IS_ABSTRACT, b);
	}

	public boolean isAbstract() {
		if (isLoaded() || loadInfo()) {
			return isBitSet(BIT_MASK_IS_ABSTRACT);
		}
		return false;
	}

	protected void setExportVariable(boolean b) {
		setBit(BIT_MASK_IS_EXPORT, b);
	}

	public boolean isExportVariable() {
		if (isLoaded() || loadInfo()) {
			return isBitSet(BIT_MASK_IS_EXPORT);
		}
		return false;
	}

	public boolean hasAttributes() {
		if (isLoaded() || loadInfo()) {
			return isBitSet(BIT_MASK_HAS_USER_ATTRIBUTE);
		}
		return false;

	}

	protected void setHasAttributes(boolean b) {
		setBit(BIT_MASK_HAS_USER_ATTRIBUTE, b);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return handle;
	}

	/**
	 * Liefert den Name, z.B. Typname, Variablenname zur�ck.
	 * 
	 * @return Name
	 */
	public String getName() {
		if (isLoaded() || loadInfo()) {
			return name;
		}
		return null;
	}

	/**
	 * Liefert die Art (PROJECT, PROGRAM, ...) des Strukturbaumknotens zur�ck.
	 * 
	 * @return Art
	 */
	public byte getKind() {
		if (isLoaded() || loadInfo()) {
			return nodeKind;
		}
		return UNKOWN;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof TcStructuralAbstractNode)) {
			return false;
		}
		return getHandle() == ((TcStructuralAbstractNode) o).getHandle();
	}

	/**
	 * Liefert den Strukturbaumknoten der Deklarationsdatei zur�ck.
	 * 
	 * @return Strukturbaumknoten
	 */
	public TcStructuralNode getDeclarationNode() {
		if (isLoaded() || loadInfo()) {
			return dirEntryNode;
		}
		return null;
	}

	/**
	 * Liefert den �bergeordneten Knoten im Strukturbaum zur�ck.
	 * 
	 * @return DOCUMENT ME!
	 */
	public TcStructuralNode getParent() {
		if (isLoaded() || loadInfo()) {
			return parent;
		}
		return null;
	}

	protected abstract boolean loadInfo();

}
