package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysMsgNrStructRequ implements XDR {
	public TRepSysNr msgNr;

	public TRepSysMsgNrStructRequ () {
		msgNr = new TRepSysNr();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		msgNr.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		msgNr.read(in);
	}
}