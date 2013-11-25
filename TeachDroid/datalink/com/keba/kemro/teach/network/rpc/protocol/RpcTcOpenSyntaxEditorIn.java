package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcOpenSyntaxEditorIn implements XDR {
	public int scopeHnd;
	public RpcTcPart part;

	public RpcTcOpenSyntaxEditorIn () {
		part = new RpcTcPart();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(scopeHnd);
		part.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		scopeHnd = in.readInt();
		part.read(in);
	}
}