package com.keba.kemro.serviceclient.alarm.sysrpc;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jsdr.sdr.SdrConnection;
import com.keba.kemro.plc.network.sysrpc.RepSys.RepSys;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysClassIdMultiResp;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysClassMaskRequ;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysEntryTypeResp;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysKLinkTextFormat;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysMsgNrStructRequ;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysMsgTextRequ;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysMsgTextResp;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysMsgTextTemplRequ;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysRespErr;
import com.keba.kemro.serviceclient.alarm.KMessage;
import com.keba.kemro.serviceclient.alarm.KMessageClient;

public class KSysRpcMessageClient extends KMessageClient {
	protected RepSys m_client;

	protected KSysRpcMessageClient(String hostName, int connectionTimeOut) {
		super(hostName, connectionTimeOut);
	}

	@Override
	public boolean connect() {
		SdrConnection connection = new SdrConnection();
		connection.ip = m_hostName;
		connection.station = -1;
		connection.timeOut = m_connectionTimeOut;
		try {
			m_client = new RepSys(m_hostName);
			return true;
		} catch (Exception e) {
		}
		m_client = null;
		return false;
	}

	@Override
	public void disconnect() {
		RepSys c = m_client;
		if (c != null) {
			try {
				c.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void close() {
		RepSys c = m_client;
		if (c != null) {
			try {
				c.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void createdBuffers() {
		RepSys c = m_client;
		if (c != null) {
			try {
				TRepSysClassIdMultiResp repSysClassIdMultiResp = new TRepSysClassIdMultiResp();
				TRepSysEntryTypeResp repSysEntryTypeResp = new TRepSysEntryTypeResp();
				TRepSysClassMaskRequ repSysClassMaskRequ = new TRepSysClassMaskRequ();
				repSysClassIdMultiResp = c.GetAllClassIds_1(repSysClassIdMultiResp);
				for (int i = 0; i < repSysClassIdMultiResp.classCnt; i++) {
					int classId = repSysClassIdMultiResp.classIdArr[i];
					repSysClassMaskRequ.classMask = classId;
					repSysEntryTypeResp = c.GetClassName_1(repSysClassMaskRequ, repSysEntryTypeResp);
					String className = repSysEntryTypeResp.value.mEntryName.toString();
					if (className != null) {
						addBuffer(className, classId, true, true);
						addBuffer(className, classId, true, false);
					}
				}
			} catch (RPCException sdre) {
			} catch (IOException ioe) {
			}
		}
	}

	protected void addBuffer(String bufferName, int classId, boolean activate, boolean isReportBuffer) {
		m_buffers.addElement(new KSysRpcMessageBuffer(this, bufferName, classId, activate, isReportBuffer, isReportBuffer ? 0 : m_maxNrEntries));
	}

	@Override
	protected String getMessageTextFromControl(KMessage msg) {
		RepSys c = m_client;
		if (c != null) {
			try {
				TRepSysMsgTextRequ in = new TRepSysMsgTextRequ();
				TRepSysMsgTextResp out = new TRepSysMsgTextResp();
				in.format.value = TRepSysKLinkTextFormat.eRepSysKLinkAscii;
				in.msgData = ((KSysRpcMessage) msg).m_msg.mNr;
				in.param = ((KSysRpcMessage) msg).m_msg.mParam;
				out = c.GetMsgTextPlain_1(in, out);
				StringBuffer buf = new StringBuffer();
				for (int i = 0; i <  out.text_count; i++){
				   buf.append((char)out.text[i]);
				}
				return buf.toString();
			} catch (RPCException sdre) {
			} catch (IOException ioe) {
			}
		}
		return null;
	}
	
	@Override
	protected String getMessageTemplateFromControl(KMessage msg) {
		RepSys c = m_client;
		if (c != null) {
			try {
				TRepSysMsgTextTemplRequ in = new TRepSysMsgTextTemplRequ();
				TRepSysMsgTextResp out = new TRepSysMsgTextResp();
				in.format.value = TRepSysKLinkTextFormat.eRepSysKLinkAscii;
				in.msgId = ((KSysRpcMessage) msg).m_Nr;
				in.compId = ((KSysRpcMessage) msg).m_CompNr;
				out = c.GetMsgTextTempl_1(in, out);
				StringBuffer buf = new StringBuffer();
				for (int i = 0; i <  out.text_count; i++){
				   buf.append((char)out.text[i]);
				}
				return buf.toString();
			} catch (RPCException sdre) {
			} catch (IOException ioe) {
			}
		}
		return null;
	}

	@Override
	protected boolean quitMessage(KMessage msg) {
		RepSys client = m_client;
		if (client != null) {
			try {
				TRepSysMsgNrStructRequ arg = new TRepSysMsgNrStructRequ();
				TRepSysRespErr retVal = new TRepSysRespErr();
				arg.msgNr = ((KSysRpcMessage) msg).m_msg.mNr;
				retVal = client.QuitMsg_1(arg, retVal);
				return retVal.value == TRepSysRespErr.eRepSysRespOk;
			} catch (RPCException sdre) {
			} catch (IOException ioe) {
			}
		}
		return false;
	}

	@Override
	protected boolean checkMsgChange() {
		try {
			RepSys client = m_client;
   			int oldCnt = m_msgChgCnt;
   			m_msgChgCnt = client.GetNrOfNewBufEntries_1(0xFFFFFFFF);
   			if (m_msgChgCnt == oldCnt) {
   				if (m_activeInfoLog) {
	   	   			oldCnt = m_logChgCnt;
	   	   			m_logChgCnt = client.GetNrOfNewBufEntries_1(0xFFFFFFFF);
	 				return m_logChgCnt != oldCnt;
   				}
   			} else {
   				return true;
   			}
  			
   	   	} catch (Exception e) {
   	   	}
   	   	return false;
	}
}
