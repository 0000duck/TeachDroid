package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcStartProgIn implements XDR {
	public int progScopeHnd;

	public RpcTcStartProgIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(progScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		progScopeHnd = in.readInt();
	}
}