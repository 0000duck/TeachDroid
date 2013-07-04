package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetConstInfoIn implements XDR {
	public int constScopeHnd;

	public SysRpcTcGetConstInfoIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(constScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		constScopeHnd = in.readInt();
	}
}