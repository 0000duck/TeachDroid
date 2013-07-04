package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetActValueOut implements XDR {
	public RpcTcValue value;
	public boolean retVal;

	public RpcTcGetActValueOut () {
		value = new RpcTcValue();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		value.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		value.read(in);
		retVal = in.readBool();
	}
}