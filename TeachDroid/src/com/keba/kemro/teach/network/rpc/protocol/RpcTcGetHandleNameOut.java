package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetHandleNameOut implements XDR {
	public String name;
	public boolean retVal;

	public RpcTcGetHandleNameOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(name);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		name = in.readString();
		retVal = in.readBool();
	}
}