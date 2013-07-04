package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetNextNodeChunkIn implements XDR {
	public int scopeHnd;
	public SysRpcTcNodeKind kind;
	public int iterHnd;

	public SysRpcTcGetNextNodeChunkIn () {
		kind = new SysRpcTcNodeKind();
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