package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetVarInfoIn implements XDR {
	public int varScopeHnd;

	public RpcTcGetVarInfoIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varScopeHnd = in.readInt();
	}
}