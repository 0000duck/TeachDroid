package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetNextExeUnitIn implements XDR {
	public int exeUnitHnd;
	public RpcTcExeUnitKind kind;
	public int iterHnd;

	public RpcTcGetNextExeUnitIn () {
		kind = new RpcTcExeUnitKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		kind.write(out);
		out.writeInt(iterHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		kind.read(in);
		iterHnd = in.readInt();
	}
}