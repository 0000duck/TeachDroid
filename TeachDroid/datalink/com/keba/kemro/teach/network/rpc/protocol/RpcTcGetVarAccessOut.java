package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetVarAccessOut implements XDR {
	public RpcTcVarAccess varAccess;
	public boolean retVal;

	public RpcTcGetVarAccessOut () {
		varAccess = new RpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		varAccess.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccess.read(in);
		retVal = in.readBool();
	}
}