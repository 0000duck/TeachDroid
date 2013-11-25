package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcLoadGlobalIn implements XDR {
	public int globalScopeHnd;

	public RpcTcLoadGlobalIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(globalScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		globalScopeHnd = in.readInt();
	}
}