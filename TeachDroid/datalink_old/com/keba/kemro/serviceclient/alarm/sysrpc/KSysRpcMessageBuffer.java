package com.keba.kemro.serviceclient.alarm.sysrpc;

import com.keba.kemro.plc.network.sysrpc.RepSys.RepSys;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysClassIdAndIdxRequ;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysClassMaskAndIdxRequ;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysMsgEntryResp;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysRespErr;
import com.keba.kemro.serviceclient.alarm.KMessage;
import com.keba.kemro.serviceclient.alarm.KMessageBuffer;

public class KSysRpcMessageBuffer extends KMessageBuffer {
	protected int m_lastMessageIter = 0;

	protected KSysRpcMessageBuffer(KSysRpcMessageClient service, String bufferName, int classId, boolean activate, boolean isReportBuffer, int maxNrEntries) {
		super(service, bufferName, classId, activate, isReportBuffer, maxNrEntries);
	}

	@Override
	protected KMessage getNextMessage() {
		RepSys client = ((KSysRpcMessageClient) m_service).m_client;
		if (client != null) {
			try {
				TRepSysMsgEntryResp out = new TRepSysMsgEntryResp();
				if (isReportBuffer()) {
					TRepSysClassIdAndIdxRequ in = new TRepSysClassIdAndIdxRequ();
					in.classId = getClassId();
					in.msgIdx = m_lastMessageIter;
					if (m_lastMessageIter <= 0) {
						out = client.GetFirstMsgOfBuf_1(in, out);
					} else {
						out = client.GetNextMsgOfBuf_1(in, out);
					}
				} else {
					TRepSysClassMaskAndIdxRequ in = new TRepSysClassMaskAndIdxRequ();
					in.classMask = getClassId();
					in.msgIdx = m_lastMessageIter;
					if (m_lastMessageIter <= 0) {
						out = client.GetFirstMsgOfProt_1(in, out);
					} else {
						out = client.GetNextMsgOfProt_1(in, out);
					}
				}
				if (out.errorCode.value == TRepSysRespErr.eRepSysRespOk) {
					m_lastMessageIter = out.value.mIndex;
					return new KSysRpcMessage(out.value, this);
				}
			} catch (Exception e) {
			}
		}
		return null;
	}

	@Override
	protected void resetIteration() {
		m_lastMessageIter = 0;
	}

	@Override
	protected void doRefresh() {
		if (((KSysRpcMessageClient) m_service).m_client != null) {
			super.doRefresh();
		}
	}

	@Override
	protected int getLastMessageId() {
		RepSys client = ((KSysRpcMessageClient) m_service).m_client;
		if (client != null) {
			try {
				TRepSysMsgEntryResp out = new TRepSysMsgEntryResp();
				if (isReportBuffer()) {
					TRepSysClassIdAndIdxRequ in = new TRepSysClassIdAndIdxRequ();
					in.classId = getClassId();
					in.msgIdx = 0;
					if (m_lastMessageIter <= 0) {
						out = client.GetLastMsgOfBuf_1(in, out);
						if (out.errorCode.value == TRepSysRespErr.eRepSysRespOk) {
							return out.value.mNr.mMsgId *1000  + out.value.mNr.mInstId;
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return 0;
	}

}
