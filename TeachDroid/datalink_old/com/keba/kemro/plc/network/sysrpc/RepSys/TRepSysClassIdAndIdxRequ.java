package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysClassIdAndIdxRequ implements XDR {
	public int classId;
	public int msgIdx;

	public TRepSysClassIdAndIdxRequ () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(classId);
		out.writeInt(msgIdx);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		classId = in.readInt();
		msgIdx = in.readInt();
	}
}