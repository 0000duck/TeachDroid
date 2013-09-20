package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcLoadGlobalIn implements XDR {
	public int globalScopeHnd;

	public SysRpcTcLoadGlobalIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(globalScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		globalScopeHnd = in.readInt();
	}
}