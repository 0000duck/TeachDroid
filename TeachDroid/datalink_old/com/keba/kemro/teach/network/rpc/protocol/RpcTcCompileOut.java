package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcCompileOut implements XDR {
	public boolean retVal;

	public RpcTcCompileOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		retVal = in.readBool();
	}
}