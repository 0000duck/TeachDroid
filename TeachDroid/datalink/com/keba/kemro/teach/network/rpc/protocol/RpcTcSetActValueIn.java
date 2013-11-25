package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcSetActValueIn implements XDR {
	public int exeUnitHnd;
	public RpcTcVarAccess varAccess;
	public RpcTcValue value;

	public RpcTcSetActValueIn () {
		varAccess = new RpcTcVarAccess();
		value = new RpcTcValue();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		varAccess.write(out);
		value.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		varAccess.read(in);
		value.read(in);
	}
}