package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcSetWatchPointVarOut implements XDR {
	public int wpVarHnd;
	public boolean retVal;

	public RpcTcSetWatchPointVarOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(wpVarHnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		wpVarHnd = in.readInt();
		retVal = in.readBool();
	}
}