package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcExecuteMethodExtOut implements XDR {
	public RpcTcValue methodRetVal;
	public boolean retVal;

	public RpcTcExecuteMethodExtOut () {
		methodRetVal = new RpcTcValue();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		methodRetVal.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		methodRetVal.read(in);
		retVal = in.readBool();
	}
}