package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcOpenVarAccessOut implements XDR {
	public RpcTcVarAccess varAccess;
	public RpcTcTypeKind type;
	public boolean retVal;

	public RpcTcOpenVarAccessOut () {
		varAccess = new RpcTcVarAccess();
		type = new RpcTcTypeKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		varAccess.write(out);
		type.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccess.read(in);
		type.read(in);
		retVal = in.readBool();
	}
}