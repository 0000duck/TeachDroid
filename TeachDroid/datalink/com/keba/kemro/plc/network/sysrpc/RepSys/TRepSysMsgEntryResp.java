package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysMsgEntryResp implements XDR {
	public TRepSysRespErr errorCode;
	public TRepSysMsgEntry value;

	public TRepSysMsgEntryResp () {
		errorCode = new TRepSysRespErr();
		value = new TRepSysMsgEntry();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		errorCode.write(out);
		value.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		errorCode.read(in);
		value.read(in);
	}
}