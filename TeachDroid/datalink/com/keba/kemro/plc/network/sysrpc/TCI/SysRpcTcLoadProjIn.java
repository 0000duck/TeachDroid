package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcLoadProjIn implements XDR {
	public int projScopeHnd;

	public SysRpcTcLoadProjIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(projScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		projScopeHnd = in.readInt();
	}
}