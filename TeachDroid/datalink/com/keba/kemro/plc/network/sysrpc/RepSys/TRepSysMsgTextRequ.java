package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysMsgTextRequ implements XDR {
	public TRepSysNr msgData;
	public TRepSysMsgParamList param;
	public TRepSysKLinkTextFormat format;

	public TRepSysMsgTextRequ () {
		msgData = new TRepSysNr();
		param = new TRepSysMsgParamList();
		format = new TRepSysKLinkTextFormat();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		msgData.write(out);
		param.write(out);
		format.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		msgData.read(in);
		param.read(in);
		format.read(in);
	}
}