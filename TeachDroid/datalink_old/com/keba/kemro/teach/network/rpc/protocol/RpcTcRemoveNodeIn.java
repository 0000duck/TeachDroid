package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcRemoveNodeIn implements XDR {
	public int scopeHnd;

	public RpcTcRemoveNodeIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(scopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		scopeHnd = in.readInt();
	}
}