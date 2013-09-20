package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcDestroyIn implements XDR {
	public int scopeHnd;

	public SysRpcTcDestroyIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(scopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		scopeHnd = in.readInt();
	}
}