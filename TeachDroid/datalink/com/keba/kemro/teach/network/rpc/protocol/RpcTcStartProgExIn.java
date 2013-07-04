package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcStartProgExIn implements XDR {
	public int progScopeHnd;
	public int startFlags;

	public RpcTcStartProgExIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(progScopeHnd);
		out.writeInt(startFlags);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		progScopeHnd = in.readInt();
		startFlags = in.readInt();
	}
}