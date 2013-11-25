package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcStartProgIn implements XDR {
	public int progScopeHnd;

	public SysRpcTcStartProgIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(progScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		progScopeHnd = in.readInt();
	}
}