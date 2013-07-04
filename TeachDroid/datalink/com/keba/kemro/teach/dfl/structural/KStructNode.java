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

import java.io.*;
import java.util.*;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.util.*;
import com.keba.kemro.teach.network.*;

/**
 * Die Klasse beschreibt eine abstrakte Basisklasse für alle speziellen
 * Strukturknoten, die den Projektaufbau und Programmdeklarationen beschreiben.
 */
public abstract class KStructNode {
	/** DOCUMENT ME! */
	private final static int		BIT_MASK_ATTRIBUTES_CHANGED		= 1;
	/** DOCUMENT ME! */
	private final static int		BIT_MASK_IS_LOADED				= 2;
	/** DOCUMENT ME! */
	private final static int		BIT_MASK_ALLOWS_CHILDREN		= 2 * 2;
	/** DOCUMENT ME! */
	private final static int		BIT_MASK_IS_ATTRIBUTES_LOADED	= 2 * 2 * 2;
	/** DOCUMENT ME! */
	private final static int		BIT_MASK_IS_PUBLIC				= 2 * 2 * 2 * 2;
	/** DOCUMENT ME! */
	private final static int		BIT_MASK_IS_PRIVATE				= 2 * 2 * 2 * 2 * 2;
	/** DOCUMENT ME! */
	private final static int		BIT_MASK_IS_GLOBAL				= 2 * 2 * 2 * 2 * 2 * 2;
	/**
	 * höchster Wert in der Bitmaske. In abgeleiteten Klasenn ist dieser Wert
	 * für einen neuen Wert mit 2 zu multiplizieren
	 */
	protected final static int		BIT_MASK_LAST					= BIT_MASK_IS_GLOBAL;
	/**
	 * Indicates that the attribute key has no value
	 */
	protected final static Object	EMPTY_ATTRIBUTE					= new Object();
	/** Konstante für eine leere Zeichenkette */
	public final static String		EMPTY_STRING					= "";
	/** Konstante für einen globalen Strukturknoten */
	public final static int			GLOBAL							= 0;
	/** Konstante für einen öffentlichen Strukturknoten */
	public final static int			PUBLIC							= 1;
	/** Konstante für einen privaten Strukturknoten */
	public final static int			PRIVATE							= 2;
	/** Konstante für Ganzzahlwert 0 */
	public final static Integer		ZERO							= new Integer(0);
	/** Konstante für das Trennzeichen in einer Datei */
	public static String			m_separator						= File.separator;
	/**
	 * Der eindeutige Schlüssel des Elementes in seinem Scope. Name des
	 * Elementes in der Datei.
	 */
	protected String				m_nativeKey;
	/** Bitset um Flags zu speichern. Speicheroptimierung. */
	private int						bitSet;
	/** contains the source code attributes */
	private Hashtable				m_attributes;

	/** Referenz auf den übergeordneten Strukturknoten */
	protected KStructNode			parent;
	/** Referenz auf den zugehörigen TeachControl-Strukturknoten */
	protected TcStructuralNode		ortsStructuralNode;
	/** speichert den Dateipfad des Strukturknotens */
	protected String				storedDirEntryPath;

	public KTcDfl					dfl;

	/**
	 * Erzeugt einen Strukturknoten
	 * 
	 * @param key
	 *            Name des Strukturknotens in der Datei
	 */
	protected KStructNode(String key, KTcDfl dfl) {
		m_nativeKey = key;
		this.dfl = dfl;
	}

	/**
	 * Liefert den scopeeindeutigen Schlüssel des Elementes.
	 * 
	 * @return scopeeindeutiger Schlüssel
	 */
	public String getKey() {
		return m_nativeKey;
	}

	public boolean isUserNode() {
		return (ortsStructuralNode != null) && ortsStructuralNode.isUserNode();
	}

	/**
	 * Testet ob Element ein übergeordneter Strukturknoten von p ist.
	 * 
	 * @return ist übergeordneter Strukturknoten
	 */
	public boolean isAboveOf(KStructNode p) {
		while (p != null) {
			if (equals(p)) {
				return true;
			}
			p = p.getParent();
		}
		return false;
	}

	/**
	 * Liefert den boolschen Wert des Flags.
	 * 
	 * @param bitMask
	 *            Das Flag
	 * 
	 * @return TRUE wenn Bit gesetzt sonst FALSE
	 */
	protected final boolean isBitSet(int bitMask) {
		return (bitSet & bitMask) == bitMask;
	}

	/**
	 * Setzten der Flags auf einen bestimmten boolschen Wert
	 * 
	 * @param bitMask
	 *            Das Flag das gesetzt werden soll.
	 * @param b
	 *            Der Wert des Flags.
	 */
	protected final void setBit(int bitMask, boolean b) {
		bitSet = (b ? (bitSet | bitMask) : (bitSet & ~bitMask));
	}

	/**
	 * Erlaubt dem Strukturknoten untergeordnete Strukturknoten zu halten.
	 * 
	 * @param b
	 */
	protected void setAllowsChildren(boolean b) {
		setBit(BIT_MASK_ALLOWS_CHILDREN, b);
	}

	/**
	 * Setzt dem Strukturknoten einen übergeordneten Knoten
	 * 
	 * @param parent
	 *            übergeordneter Knoten
	 */
	protected void setParent(KStructNode parent) {
		this.parent = parent;
	}

	/**
	 * Setzt den TeachControl-Strukturknoten
	 * 
	 * @param n
	 *            TeachControl-Strukturknoten
	 */
	protected void setTcStructuralNode(TcStructuralNode n) {
		ortsStructuralNode = n;
	}

	/**
	 * Wird gesetzt, wenn der Knoten geladen ist.
	 * 
	 * @param b
	 */
	protected void setLoaded(boolean b) {
		setBit(BIT_MASK_IS_LOADED, b);
	}

	/**
	 * Setzt den Dateipfad des Strukturknotens in dem er deklariert ist.
	 * 
	 * @param newDirEntryPath
	 *            Datei-Pfad
	 */
	public void setDefaultDirEntryPath(String newDirEntryPath) {
		storedDirEntryPath = newDirEntryPath;
	}

	/**
	 * Liefert den zugehörigen Projektknoten
	 * 
	 * @return Projektknoten
	 */
	public KStructProject getKStructProject() {
		KStructNode par;

		par = getParent();
		while (par != null) {
			if (par instanceof KStructProject) {
				return (KStructProject) par;
			}
			par = par.getParent();
		}
		return null;
	}

	public String getDeclarationPath() {
		TcStructuralNode program = getTcStructuralNode().getDeclarationNode();
		String accesspath = null;
		TcStructuralNode n = program;
		while ((n != null) && (n.getParent() != null)) {
			if (accesspath == null) {
				accesspath = n.getName();
			} else {
				accesspath = n.getName() + "." + accesspath;
			}
			n = n.getParent();
		}
		return accesspath;
	}

	/**
	 * Gibt an, ob der Knoten untergeordnete Knoten erlaubt.
	 * 
	 * @return Wahrheitswert
	 */
	public boolean getAllowsChildren() {
		return isBitSet(BIT_MASK_ALLOWS_CHILDREN);
	}

	/**
	 * Liefert den übergeordneten Strukturknoten
	 * 
	 * @return übergeordneten Strukturknoten
	 */
	public KStructNode getParent() {
		return parent;
	}

	/**
	 * Liefert den TeachControl-Strukturknoten
	 * 
	 * @return TeachControl-Strukturknoten
	 */
	public TcStructuralNode getTcStructuralNode() {
		return ortsStructuralNode;
	}

	/**
	 * Gibt an, ob der Knoten geladen ist
	 * 
	 * @return Wahrheitswert
	 */
	protected boolean isLoaded() {
		return isBitSet(BIT_MASK_IS_LOADED);
	}

	/**
	 * Liefert den eindeutigen Schlüssel (Name)
	 * 
	 * @return eindeutiger Schlüssel (Name)
	 */
	public String toString() {
		return m_nativeKey;
	}

	/**
	 * Liefert den Dateipfad in dem der Strukturknoten deklariert ist.
	 * 
	 * @return Dateipfad als Zeichenkette
	 */
	public String getDirEntryPath() {
		if (storedDirEntryPath != null) {
			return storedDirEntryPath;
		}
		KStructProject project = getKStructProject();
		if (ortsStructuralNode.getDeclarationNode() != null) {
			storedDirEntryPath = project.getDirEntryPath(ortsStructuralNode.getDeclarationNode());
		} else {
			storedDirEntryPath = "";
		}
		return storedDirEntryPath;
	}

	/**
	 * Liefert den Pfad des Knotens im Strukturbaum.
	 * 
	 * @return Zeichenkettenfeld
	 */
	public String getPath() {
		return dfl.structure.convertPath(getKeyPathToRoot(this, 0));
	}

	/**
	 * Liefert die Sichtbarkeit des Knotens.
	 * 
	 * @return Sichtbarkeitskonstante
	 */
	public int getVisibility() {
		if (isBitSet(BIT_MASK_IS_GLOBAL)) {
			return GLOBAL;
		}
		if (isBitSet(BIT_MASK_IS_PUBLIC)) {
			return PUBLIC;
		}
		return PRIVATE;
	}

	/**
	 * @return true if it is a global node
	 */
	public boolean isGlobal() {
		return isBitSet(BIT_MASK_IS_GLOBAL);
	}

	/**
	 * @return true if it is a private node
	 */
	public boolean isPrivate() {
		return isBitSet(BIT_MASK_IS_PRIVATE);
	}

	/**
	 * Gibt an, ob der Knoten PUBLIC deklariert ist.
	 * 
	 * @return The public value
	 */
	public boolean isPublic() {
		return isBitSet(BIT_MASK_IS_PUBLIC);
	}

	/**
	 * Lädt die untergeordneten Knoten. Muss in den abgeleiteten Klassen
	 * implementiert werden.
	 */
	protected void loadChildren() {
		setLoaded(true);
	}

	/**
	 * Initialisiert die Sichtbarkeit des Knotens.
	 * 
	 * @param visibility
	 *            Sichtbarkeitskonstante
	 */
	protected void setVisibility(int visibility) {
		if (visibility == GLOBAL) {
			setBit(BIT_MASK_IS_GLOBAL, true);
			setBit(BIT_MASK_IS_PRIVATE, false);
			setBit(BIT_MASK_IS_PUBLIC, false);
		} else if (visibility == PUBLIC) {
			setBit(BIT_MASK_IS_GLOBAL, false);
			setBit(BIT_MASK_IS_PRIVATE, false);
			setBit(BIT_MASK_IS_PUBLIC, true);
		} else {
			setBit(BIT_MASK_IS_GLOBAL, false);
			setBit(BIT_MASK_IS_PRIVATE, true);
			setBit(BIT_MASK_IS_PUBLIC, false);
		}
	}

	/**
	 * Liefert den Pfad zum Wurzelknoten (rekursive Methode).
	 * 
	 * @param aNode
	 *            Ausgangsknoten
	 * @param depth
	 *            gibt die Pfadtiefe an u. wird pro Aufruf um eins erhöht.
	 * 
	 * @return Feld von Strukturknoten, die den Pfad beschreiben
	 */
	protected KStructNode[] getPathToRoot(KStructNode aNode, int depth) {
		KStructNode[] retNodes;

		if (aNode == null) {
			if (depth == 0) {
				return null;
			}
			retNodes = new KStructNode[depth];
		} else {
			depth++;
			retNodes = getPathToRoot(aNode.getParent(), depth);
			retNodes[retNodes.length - depth] = aNode;
		}
		return retNodes;
	}

	/**
	 * Liefert den Pfad zum Wurzelknoten (rekursive Methode).
	 * 
	 * @param aNode
	 *            Ausgangsknoten
	 * @param depth
	 *            gibt die Pfadtiefe an u. wird pro Aufruf um eins erhöht.
	 * 
	 * @return Feld von Strukturknoten, die den Pfad beschreiben
	 */
	private Object[] getKeyPathToRoot(KStructNode aNode, int depth) {
		Object[] retNodes;

		if ((aNode instanceof KStructRoot) || (aNode == null)) {
			if (depth == 0) {
				return null;
			}
			retNodes = new Object[depth];
		} else {
			depth++;
			retNodes = getKeyPathToRoot(aNode.getParent(), depth);
			retNodes[retNodes.length - depth] = aNode;
		}
		return retNodes;
	}

	/**
	 * @return true if the attributes are already loaded.
	 */
	public boolean hasAttributesLoaded() {
		return isBitSet(BIT_MASK_IS_ATTRIBUTES_LOADED);
	}

	/**
	 * Sets the loaded flag.
	 * 
	 * @param b
	 *            true if loaded
	 */
	public void setAttributesLoaded(boolean b) {
		setBit(BIT_MASK_IS_ATTRIBUTES_LOADED, b);
	}

	/**
	 * @return true if the attributes have changed.
	 */
	public boolean hasAttributesChanged() {
		return isBitSet(BIT_MASK_ATTRIBUTES_CHANGED);
	}

	/**
	 * Sets the changed flag.
	 * 
	 * @param b
	 *            true if changed
	 */
	public void setAttributesChanged(boolean b) {
		setBit(BIT_MASK_ATTRIBUTES_CHANGED, b);
	}

	/**
	 * Retruns true if a attribute with the given key exists.
	 * 
	 * @param key
	 *            attribute key
	 * @return true if a attribute with the given key exists.
	 */
	public boolean containsAttribute(String key) {
		loadAttributes();
		if ((m_attributes != null) && m_attributes.containsKey(key)) {
			AttributeValue av = (AttributeValue) m_attributes.get(key);
			return av.state != AttributeValue.REMOVED;
		}
		return false;
	}

	public boolean isAttributeSet(String key) {
		loadAttributes();
		if (m_attributes != null) {
			String val = (String) getAttribute(key);
			return "TRUE".equalsIgnoreCase(val);
		}
		return false;
	}

	/**
	 * Returns the attribute value for the given key. If the attribute key
	 * hasn't a value then null is returned.
	 * 
	 * @param key
	 *            attribute key
	 * @return attribute value
	 */
	public Object getAttribute(String key) {
		loadAttributes();
		if ((m_attributes != null) && m_attributes.containsKey(key) && !((AttributeValue) m_attributes.get(key)).value.equals(EMPTY_ATTRIBUTE) && ((AttributeValue) m_attributes.get(key)).state != AttributeValue.REMOVED) {
			return ((AttributeValue) m_attributes.get(key)).value;
		}
		return null;
	}

	/**
	 * Returns all attribute keys.
	 * 
	 * @return enumeration of keys or null when there aren't any key declared
	 */
	public Enumeration getAttributes() {
		loadAttributes();
		if (m_attributes != null) {
			return m_attributes.keys();
		}
		return null;
	}

	/**
	 * Removes the attribute.
	 * 
	 * @param key
	 *            attribute key
	 */
	public void removeAttribute(String key) {
		loadAttributes();
		if (m_attributes == null) {
			m_attributes = new Hashtable();
		}
		AttributeValue av = (AttributeValue) m_attributes.get(key);
		if (av != null) {
			av.state = AttributeValue.REMOVED;
		}
		setAttributesChanged(true);
	}

	/**
	 * Sets a attribute value
	 * 
	 * @param key
	 *            attribute key
	 * @param value
	 *            attribute value
	 */
	public void setAttribute(String key, Object value) {
		loadAttributes();
		if (m_attributes == null) {
			m_attributes = new Hashtable();
		}
		AttributeValue av = new AttributeValue();
		av.value = value;
		av.state = AttributeValue.CHANGED;
		if (value == null) {
			av.value = EMPTY_ATTRIBUTE;
		}
		m_attributes.put(key, av);
		setAttributesChanged(true);
	}

	protected synchronized void loadAttributes() {
		if (!hasAttributesLoaded()) {
			setAttributesLoaded(true);
			if (getTcStructuralNode().hasAttributes()) {
				String attrib;
				//@ltz parent can be null, however
				if (parent != null)
					attrib = parent.dfl.client.structure.getAttributes(getTcStructuralNode());
				else
					attrib = dfl.client.structure.getAttributes(getTcStructuralNode());
				if ((attrib != null) && (0 < attrib.length())) {
					m_attributes = new Hashtable();
					setAttributes(m_attributes, attrib);
				}
			}
		}
	}

	protected synchronized void setAttributes(Hashtable table, String attributes) {
		// parse attributes
		int beg = 0;
		int end = attributes.indexOf("|");
		if (end < beg) {
			end = attributes.length();
		}
		while (beg < attributes.length()) {
			int equal = attributes.indexOf("=", beg);
			if ((equal < beg) || (end < equal)) {
				AttributeValue av = new AttributeValue();
				av.value = EMPTY_ATTRIBUTE;
				table.put(attributes.substring(beg, end).trim(), av);
			} else {
				String aKey = attributes.substring(beg, equal).trim();
				String sValue = attributes.substring(equal + 1, end).trim();
				Object value = EMPTY_ATTRIBUTE;
				if (sValue.charAt(0) == '"') {
					// string value
					value = sValue.substring(1, sValue.length() - 1);
				} else if (sValue.equals("TRUE")) {
					value = Boolean.TRUE;
				} else if (sValue.equals("FALSE")) {
					value = Boolean.FALSE;
				} else if (0 < sValue.indexOf('.')) {
					// float value
					try {
						value = Float.valueOf(sValue);
					} catch (NumberFormatException e) {
						KDflLogger.error(getClass(), "setAttributes: no valid float value");
					}
				} else {
					// integer value
					try {
						value = Integer.valueOf(sValue);
					} catch (NumberFormatException e) {
						KDflLogger.error(getClass(), "setAttributes: no valid int value");
					}
				}
				AttributeValue av = new AttributeValue();
				av.value = value;
				table.put(aKey, av);
			}
			beg = end + 1;
			end = attributes.indexOf("|", beg);
			if (end < beg) {
				end = attributes.length();
			}
		}
	}

	/**
	 * Saves the attributes.
	 */
	public void saveAttributes() {
		if (hasAttributesChanged() && (m_attributes != null)) {
			Hashtable temp = m_attributes;
			m_attributes = null;
			setAttributesLoaded(false);
			loadAttributes();
			Enumeration e = temp.keys();
			while (e.hasMoreElements()) {
				String aKey = (String) e.nextElement();
				AttributeValue av = (AttributeValue) temp.get(aKey);
				if (av.state == AttributeValue.CHANGED) {
					setAttribute(aKey, av.value);
				} else if (av.state == AttributeValue.REMOVED) {
					removeAttribute(aKey);
				}
			}
			// write back
			if (m_attributes != null) {
				e = m_attributes.keys();
				String attributes = "";
				while (e.hasMoreElements()) {
					String aKey = (String) e.nextElement();
					AttributeValue av = (AttributeValue) m_attributes.get(aKey);
					if (av.state != AttributeValue.REMOVED) {
						if (0 < attributes.length()) {
							attributes += " | ";
						}
						attributes += aKey;
						if (av.value instanceof Boolean) {
							attributes += "=" + (((Boolean) av.value).booleanValue() ? "TRUE" : "FALSE");
						} else if (av.value instanceof Number) {
							attributes += "=" + av.value.toString();
						} else if (av.value instanceof String) {
							attributes += "=\"" + ((String) av.value) + "\"";
						}
					}
				}
				parent.dfl.client.structure.setAttirbutes(getTcStructuralNode(), attributes);
			}
		}
	}

	private static class AttributeValue {
		private static final int	CHANGED	= 1;
		private static final int	REMOVED	= 2;
		Object						value;
		int							state;
	}
}
