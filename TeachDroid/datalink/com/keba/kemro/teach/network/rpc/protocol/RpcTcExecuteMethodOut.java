package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcExecuteMethodOut implements XDR {
	public boolean methodRetVal;
	public boolean retVal;

	public RpcTcExecuteMethodOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeBool(methodRetVal);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		methodRetVal = in.readBool();
		retVal = in.readBool();
	}
}