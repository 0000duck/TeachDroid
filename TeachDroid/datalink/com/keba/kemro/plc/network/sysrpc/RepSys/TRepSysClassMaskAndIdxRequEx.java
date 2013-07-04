package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysClassMaskAndIdxRequEx implements XDR {
	public int classMask;
	public int msgIdx;
	public int mMaxMsgs;

	public TRepSysClassMaskAndIdxRequEx () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(classMask);
		out.writeInt(msgIdx);
		out.writeInt(mMaxMsgs);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		classMask = in.readInt();
		msgIdx = in.readInt();
		mMaxMsgs = in.readInt();
	}
}