package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetMapTargetIn implements XDR {
	public int exeUnitHnd;
	public RpcTcVarAccess varAccess;

	public RpcTcGetMapTargetIn () {
		varAccess = new RpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		varAccess.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		varAccess.read(in);
	}
}