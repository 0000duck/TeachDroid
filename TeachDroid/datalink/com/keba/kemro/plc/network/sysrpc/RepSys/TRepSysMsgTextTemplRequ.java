package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysMsgTextTemplRequ implements XDR {
	public int compId;
	public int msgId;
	public TRepSysKLinkTextFormat format;

	public TRepSysMsgTextTemplRequ () {
		format = new TRepSysKLinkTextFormat();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(compId);
		out.writeInt(msgId);
		format.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		compId = in.readInt();
		msgId = in.readInt();
		format.read(in);
	}
}