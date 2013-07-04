package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetConstInfoIn implements XDR {
	public int constScopeHnd;

	public RpcTcGetConstInfoIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(constScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		constScopeHnd = in.readInt();
	}
}