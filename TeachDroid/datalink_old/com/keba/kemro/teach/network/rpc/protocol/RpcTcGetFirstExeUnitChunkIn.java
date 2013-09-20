package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetFirstExeUnitChunkIn implements XDR {
	public int exeUnitHnd;
	public RpcTcExeUnitKind kind;

	public RpcTcGetFirstExeUnitChunkIn () {
		kind = new RpcTcExeUnitKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		kind.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		kind.read(in);
	}
}