package com.keba.kemro.teach.network.sysrpc;

import com.keba.kemro.teach.network.*;
import com.keba.kemro.plc.network.sysrpc.TCI.*;

public class TcSysRpcStructuralVarNode extends TcSysRpcStructuralNode implements TcStructuralVarNode {
	private static final SysRpcTcGetVarInfoIn SysRpcTcGetVarInfoIn = new SysRpcTcGetVarInfoIn();
	private static final SysRpcTcGetVarInfoOut SysRpcTcGetVarInfoOut = new SysRpcTcGetVarInfoOut();
	private static final int BIT_MASK_IS_LOADED = TcSysRpcStructuralNode.BIT_MASK_LAST * 2;
	private static final int BIT_MASK_IS_PUBLIC = TcSysRpcStructuralNode.BIT_MASK_LAST * 4;
	private static final int BIT_MASK_IS_SAVE = TcSysRpcStructuralNode.BIT_MASK_LAST * 8;
	protected static final int BIT_MASK_IS_OPTIONAL = TcSysRpcStructuralNode.BIT_MASK_LAST * 16;
	protected static final int BIT_MASK_IS_READONLY = TcSysRpcStructuralNode.BIT_MASK_LAST * 32;
	private byte varKind;
	private TcSysRpcStructuralTypeNode type;
	// @ltz - TESTING for memory dumps
	private int m_tcMemOffset = -1;
	private int m_tcVarMemSize = -1;

	TcSysRpcStructuralVarNode(int handle, TcSysRpcClient client) {
		super(handle, client);
	}

	/**
	 * Liefert die Variablenart (VAR, PARAM, CONST_PARAM oder VALUE_PARAM)
	 * zurück.
	 * 
	 * @return Variablenart
	 */
	public byte getVarKind() {
		if (isVarInfoLoaded() || loadVarInfo()) {
			return varKind;
		}
		return -1;
	}

	/**
	 * Liefert den Typ der Variable zurück.
	 * 
	 * @return Variablentyp
	 */
	public TcStructuralTypeNode getType() {
		if (isVarInfoLoaded() || loadVarInfo()) {
			return type;
		}
		return null;
	}

	/**
	 * Gibt die Sichtbarkeit an.
	 * 
	 * @return true für eine Pulic - Variable
	 */
	public boolean isPublic() {
		if (isVarInfoLoaded() || loadVarInfo()) {
			return isBitSet(BIT_MASK_IS_PUBLIC);
		}
		return false;
	}

	private void setPublic(boolean b) {
		setBit(BIT_MASK_IS_PUBLIC, b);
	}

	/**
	 * Liefert true für eine Save-Variable zurück.
	 * 
	 * @return true für eine Save-Variable
	 */
	public boolean isSave() {
		if (isVarInfoLoaded() || loadVarInfo()) {
			return isBitSet(BIT_MASK_IS_SAVE);
		}
		return false;
	}

	private void setSave(boolean b) {
		setBit(BIT_MASK_IS_SAVE, b);
	}

	/**
	 * Returns true if the variable is a optional parameter.
	 * 
	 * @return true for a optional parameter
	 */
	public boolean isOptional() {
		if (isVarInfoLoaded() || loadVarInfo()) {
			return isBitSet(BIT_MASK_IS_OPTIONAL);
		}
		return false;
	}

	private void setOptional(boolean b) {
		setBit(BIT_MASK_IS_OPTIONAL, b);
	}

	/**
	 * Returns true if the variable is a optional parameter.
	 * 
	 * @return true for a optional parameter
	 */
	public boolean isReadOnly() {
		if (isVarInfoLoaded() || loadVarInfo()) {
			return isBitSet(BIT_MASK_IS_READONLY);
		}
		return false;
	}

	private void setReadOnly(boolean b) {
		setBit(BIT_MASK_IS_READONLY, b);
	}

	/**
	 * Returns true if the variable is referenced.
	 * 
	 * @return true if the variable is referenced
	 */
	public boolean isReferenced() {
		try {
			synchronized (SysRpcTcGetVarInfoIn) {
				SysRpcTcGetVarInfoIn.varScopeHnd = getHandle();
				client.client.SysRpcTcGetVarInfo_1(SysRpcTcGetVarInfoIn, SysRpcTcGetVarInfoOut);
				if (SysRpcTcGetVarInfoOut.retVal) {
					return (SysRpcTcGetVarInfoOut.info.attr & SysRpcTcVarAttr.rpcVarAttrIsReferenced) == SysRpcTcVarAttr.rpcVarAttrIsReferenced;
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralVarNode - loadInfo: ");
		}
		return false;
	}

	private boolean isVarInfoLoaded() {
		return isBitSet(BIT_MASK_IS_LOADED);
	}

	private void setVarInfoLoaded(boolean b) {
		setBit(BIT_MASK_IS_LOADED, b);
	}

	private boolean loadVarInfo() {
		try {
			synchronized (SysRpcTcGetVarInfoIn) {
				SysRpcTcGetVarInfoIn.varScopeHnd = getHandle();
				client.client.SysRpcTcGetVarInfo_1(SysRpcTcGetVarInfoIn, SysRpcTcGetVarInfoOut);
				if (SysRpcTcGetVarInfoOut.retVal) {
					setVarInfo(SysRpcTcGetVarInfoOut.info);
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralVarNode - loadInfo: ");
		}
		return false;
	}

	/**
	 * Setzt die Variableninformation.
	 * 
	 * @param info
	 *            Variableninformation
	 */
	void setVarInfo(SysRpcTcVarInfo info) {
		setVarInfoLoaded(true);
		setSave(info.isSave);
		setReadOnly((info.attr & SysRpcTcVarAttr.rpcVarAttrIsReadonly) == SysRpcTcVarAttr.rpcVarAttrIsReadonly);
		varKind = (byte) info.kind.value;
		setOptional((info.attr & SysRpcTcVarAttr.rpcVarAttrIsOptional) == SysRpcTcVarAttr.rpcVarAttrIsOptional);
		TcStructuralNode osn;
		if (info.typeHnd != 0) {
			osn = client.cache.getFromCache(info.typeHnd);
			if ((osn != null) && (osn instanceof TcSysRpcStructuralTypeNode)) {
				type = (TcSysRpcStructuralTypeNode) osn;
			} else {
				type = new TcSysRpcStructuralTypeNode(info.typeHnd, client);
				client.cache.putToCache(type);
			}
		}
		setPublic(!info.isPrivate);

		// @ltz - TESTING memdump
		setTCMemSze(info.dataSize);
		if (info.incCnt < 0)
			setTCMemOffset(~info.incCnt);
	}

	public int getTCMemOffset() {
		return m_tcMemOffset;
	}

	public void setTCMemOffset(int tcMemOffset) {
		m_tcMemOffset = tcMemOffset;
	}

	public int getTCMemSize() {
		return m_tcVarMemSize;
	}

	public void setTCMemSze(int tcMemSze) {
		m_tcVarMemSize = tcMemSze;
	}
}
