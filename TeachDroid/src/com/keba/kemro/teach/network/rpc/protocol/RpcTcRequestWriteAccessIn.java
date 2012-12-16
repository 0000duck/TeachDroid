package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcRequestWriteAccessIn implements XDR {
	public boolean request;
	public boolean retVal;

	public RpcTcRequestWriteAccessIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeBool(request);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		request = in.readBool();
		retVal = in.readBool();
	}
}