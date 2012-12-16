package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetNextNodeIn implements XDR {
	public int scopeHnd;
	public RpcTcNodeKind kind;
	public int iterHnd;

	public RpcTcGetNextNodeIn () {
		kind = new RpcTcNodeKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(scopeHnd);
		kind.write(out);
		out.writeInt(iterHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		scopeHnd = in.readInt();
		kind.read(in);
		iterHnd = in.readInt();
	}
}