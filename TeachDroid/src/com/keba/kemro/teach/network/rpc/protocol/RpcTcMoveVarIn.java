package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcMoveVarIn implements XDR {
	public int varHnd;
	public int newScopeHnd;

	public RpcTcMoveVarIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varHnd);
		out.writeInt(newScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varHnd = in.readInt();
		newScopeHnd = in.readInt();
	}
}