package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcCreateInstanceOut implements XDR {
	public int tempVarHnd;
	public boolean retVal;

	public RpcTcCreateInstanceOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(tempVarHnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		tempVarHnd = in.readInt();
		retVal = in.readBool();
	}
}