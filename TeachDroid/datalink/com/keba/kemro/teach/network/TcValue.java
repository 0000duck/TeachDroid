package com.keba.kemro.teach.network;

/**
 * Value enthält den Wert einer Variablen
 * 
 * @see TcStructuralVarNode
 */
public class TcValue {
	private static final String EMPTY_STRING = "";
	public boolean boolValue;
	public long int64Value;
	public int int32Value;
	public short int16Value;
	public byte int8Value;
	public float floatValue;
	public String stringValue = EMPTY_STRING;
	/** true für eine gültigen Variablenwert */
	public boolean isValid;

	/**
	 * Since TcValue is a generic wrapper object for any one kind of TC values,
	 * it is necessary to know, which of the members of TcValue holds the
	 * information. Instead of manually interpreting the node kind of the
	 * corresponding {@link TcStructuralTypeNode}, one could use this method
	 * 
	 * @param _nodeKind
	 *            An integer representing the node's type according to the
	 *            following marshalling schema: <code>
	 * <ul>
	 * 	<li>TcStructuralTypeNode.BOOL_TYPE => {@link java.lang.Boolean}</li>
	 * 	<li>TcStructuralTypeNode.[SINT_TYPE|BYTE_TYPE] => {@link java.lang.Byte}</li>
	 * 	<li>TcStructuralTypeNode.[INT_TYPE|WORD_TYPE] => {@link java.lang.Short}</li>
	 * 	<li>TcStructuralTypeNode.[DINT_TYPE|SUBRANGE_TYPE|ENUM_TYPE] => {@link java.lang.Integer}</li>
	 * 	<li>TcStructuralTypeNode.[LINT_TYPE|LWORD_TYPE] => {@link java.lang.Long}</li>
	 * 	<li>TcStructuralTypeNode.DWORD_TYPE => {@link java.lang.DWord}</li>
	 * 	<li>TcStructuralTypeNode.REAL_TYPE => {@link java.lang.Float}</li>
	 * 	<li>TcStructuralTypeNode.STRING_TYPE => {@link java.lang.String}</li>
	 * 	
	 * </ul>
	 * </code>
	 * @return a {@link java.lang.Object} which is a runtime instance of
	 *         Boolean, Byte,.... according to the marshalling table, null if no
	 *         type match was found or if the TcValue's isValid flag is false
	 */
	public Object getValue(int _nodeKind) {
		int kind = _nodeKind;
		Object actualValue = null;
		if (!isValid)
			return null;

		if (kind == TcStructuralTypeNode.BOOL_TYPE) {
			actualValue = boolValue ? Boolean.TRUE : Boolean.FALSE;

		} else if ((kind == TcStructuralTypeNode.SINT_TYPE) || (kind == TcStructuralTypeNode.BYTE_TYPE)) {
			actualValue = new Byte(int8Value);

		} else if ((kind == TcStructuralTypeNode.INT_TYPE) || (kind == TcStructuralTypeNode.WORD_TYPE)) {
			actualValue = new Short(int16Value);

//		} 
//		else if ((kind == TcStructuralTypeNode.DWORD_TYPE)) {
//			actualValue = new DWord(int32Value);

		} else if ((kind == TcStructuralTypeNode.DINT_TYPE) || (kind == TcStructuralTypeNode.DWORD_TYPE) || (kind == TcStructuralTypeNode.ENUM_TYPE) || (kind == TcStructuralTypeNode.SUBRANGE_TYPE)) {
			actualValue = new Integer(int32Value);

		} else if ((kind == TcStructuralTypeNode.LINT_TYPE) || (kind == TcStructuralTypeNode.LWORD_TYPE)) {
			actualValue = new Long(int64Value);

		} else if (kind == TcStructuralTypeNode.REAL_TYPE) {
			// floatComparison
			actualValue = new Float(floatValue);

		} else if (kind == TcStructuralTypeNode.STRING_TYPE) {
			actualValue = stringValue;

		}else if (kind==TcStructuralTypeNode.LREAL_TYPE) {
			actualValue= new Double(Double.longBitsToDouble(int64Value));
		}
		else {
			actualValue = null;
		}
		return actualValue;
	}

	public TcValue() {
	}
}