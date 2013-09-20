package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetTypeInfoIn implements XDR {
	public int typeScopeHnd;

	public RpcTcGetTypeInfoIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(typeScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		typeScopeHnd = in.readInt();
	}
}