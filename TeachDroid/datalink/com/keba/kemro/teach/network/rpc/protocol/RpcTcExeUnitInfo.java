package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcExeUnitInfo implements XDR {
	public RpcTcExeUnitKind kind;
	public boolean isMainFlow;
	public String callPath;
	public int scopeHnd;
	public int priority;

	public RpcTcExeUnitInfo () {
		kind = new RpcTcExeUnitKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		kind.write(out);
		out.writeBool(isMainFlow);
		out.writeString(callPath);
		out.writeInt(scopeHnd);
		out.writeInt(priority);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		kind.read(in);
		isMainFlow = in.readBool();
		callPath = in.readString();
		scopeHnd = in.readInt();
		priority = in.readInt();
	}
}