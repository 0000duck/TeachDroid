package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetHandleOut implements XDR {
	public int hnd;
	public boolean retVal;

	public RpcTcGetHandleOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(hnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		hnd = in.readInt();
		retVal = in.readBool();
	}
}