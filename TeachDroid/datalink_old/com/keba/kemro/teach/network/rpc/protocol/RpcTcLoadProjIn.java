package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcLoadProjIn implements XDR {
	public int projScopeHnd;

	public RpcTcLoadProjIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(projScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		projScopeHnd = in.readInt();
	}
}