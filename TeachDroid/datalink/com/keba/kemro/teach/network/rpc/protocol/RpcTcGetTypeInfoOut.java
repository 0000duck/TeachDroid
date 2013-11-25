package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetTypeInfoOut implements XDR {
	public RpcTcTypeInfo info;
	public boolean retVal;

	public RpcTcGetTypeInfoOut () {
		info = new RpcTcTypeInfo();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		info.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		info.read(in);
		retVal = in.readBool();
	}
}