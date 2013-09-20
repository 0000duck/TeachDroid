package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysClassMaskRequ implements XDR {
	public int classMask;

	public TRepSysClassMaskRequ () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(classMask);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		classMask = in.readInt();
	}
}