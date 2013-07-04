package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcExecuteMethodIn implements XDR {
	public String methodName;
	public int instanceExeUnitHnd;
	public RpcTcVarAccess instanceVarAccess;
	public int paramExeUnitHnd;
	public RpcTcVarAccess paramVarAccess;

	public RpcTcExecuteMethodIn () {
		instanceVarAccess = new RpcTcVarAccess();
		paramVarAccess = new RpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(methodName);
		out.writeInt(instanceExeUnitHnd);
		instanceVarAccess.write(out);
		out.writeInt(paramExeUnitHnd);
		paramVarAccess.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		methodName = in.readString();
		instanceExeUnitHnd = in.readInt();
		instanceVarAccess.read(in);
		paramExeUnitHnd = in.readInt();
		paramVarAccess.read(in);
	}
}