package com.keba.kemro.serviceclient.alarm.sysrpc;

import com.keba.kemro.plc.network.sysrpc.RepSysMsgUtil;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysMsgEntry;
import com.keba.kemro.serviceclient.alarm.KMessage;
import com.keba.kemro.serviceclient.alarm.KMessageBuffer;

public class KSysRpcMessage extends KMessage {
	protected TRepSysMsgEntry m_msg;
	protected Object[] param;
	protected boolean paramsChecked;

	protected KSysRpcMessage(TRepSysMsgEntry msg, KMessageBuffer buffer) {
		super(buffer, msg.mNr.mClassId, msg.mNr.mCompId, msg.mNr.mInstId, msg.mNr.mMsgId);
		m_msg = msg;
	}

	@Override
	public int getParamCount() {
		return m_msg.mParam.mNrParams;
	}

	@Override
	public int getParamKind(int idx) {
		if ((0 <= idx) && (idx < m_msg.mParam.mParam_count)) {
			return m_msg.mParam.mParam[idx].mKind.value;
		}
		return 0;
	}

	@Override
	public Object getParamValue(int idx) {
		if (!paramsChecked) {
			paramsChecked = true;
			param = RepSysMsgUtil.createParams(m_msg.mParam.mParam_count, m_msg.mParam.mParam, m_msg.mParam.mString);
		}
		if (param != null) {
			return param[idx];
		}
		return null;
	}

	@Override
	public int getState() {
		return m_msg.mFlag.value;
	}

	@Override
	public long getTimeMSec() {
		return ((long) m_msg.mTime.sec) * 1000 + (m_msg.mTime.usec / 1000);
	}

	@Override
	protected long getTimeUSec() {
		return ((long) m_msg.mTime.sec) * 1000000 + m_msg.mTime.usec;
	}

	@Override
	protected boolean update(KMessage msg) {
		boolean changed = false;
		if (m_msg.mTime.sec != ((KSysRpcMessage) msg).m_msg.mTime.sec) {
			changed = true;
		}
		if (m_msg.mTime.usec != ((KSysRpcMessage) msg).m_msg.mTime.usec) {
			changed = true;
		}
		if (m_msg.mFlag.value != ((KSysRpcMessage) msg).m_msg.mFlag.value) {
			changed = true;
		}
		m_msg = ((KSysRpcMessage) msg).m_msg;

		return changed;

	}

	@Override
	public int getMsgIndex() {
		return m_msg.mNr.mMsgId *1000  + m_msg.mNr.mInstId;
	}

}
