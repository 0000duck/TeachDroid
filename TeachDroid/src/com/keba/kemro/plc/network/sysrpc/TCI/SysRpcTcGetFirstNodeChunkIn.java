package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetFirstNodeChunkIn implements XDR {
	public int scopeHnd;
	public SysRpcTcNodeKind kind;

	public SysRpcTcGetFirstNodeChunkIn () {
		kind = new SysRpcTcNodeKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(scopeHnd);
		kind.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		scopeHnd = in.readInt();
		kind.read(in);
	}
}