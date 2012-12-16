package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcSetMapTargetIn implements XDR {
	public int exeUnitHnd;
	public RpcTcVarAccess varAccess;
	public RpcTcMapTarget target;

	public RpcTcSetMapTargetIn () {
		varAccess = new RpcTcVarAccess();
		target = new RpcTcMapTarget();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		varAccess.write(out);
		target.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		varAccess.read(in);
		target.read(in);
	}
}