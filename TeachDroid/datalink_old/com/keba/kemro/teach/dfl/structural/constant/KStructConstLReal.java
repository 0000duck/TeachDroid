/**
 * 
 */
package com.keba.kemro.teach.dfl.structural.constant;

import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.dfl.structural.type.KStructType;
import com.keba.kemro.teach.network.TcStructuralConstNode;
import com.keba.kemro.teach.network.TcValue;

/**
 * @author ltz
 * 
 */
public class KStructConstLReal extends KStructConst {

	private static final Double	defaultValue	= new Double(0);

	KStructConstLReal(String key, KStructType type, int visibility, KTcDfl dfl) {
		super(key, type, visibility, dfl);
		setLoaded(true);
		setAllowsChildren(false);
	}

	/**
	 * @see com.keba.kemro.teach.dfl.structural.constant.KStructConst#getInitValue()
	 */
	public Object getInitValue() {
		if (m_initValue == null) {
			if (ortsStructuralNode == null) {
				m_initValue = defaultValue;
			} else {
				TcValue value = ((TcStructuralConstNode) this.getTcStructuralNode()).getValue();
				m_initValue = new Double(Double.longBitsToDouble(value.int64Value));
			}
		}
		return m_initValue;
	}
}
