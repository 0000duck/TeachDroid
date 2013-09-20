package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetVarInfoIn implements XDR {
	public int varScopeHnd;

	public SysRpcTcGetVarInfoIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varScopeHnd = in.readInt();
	}
}