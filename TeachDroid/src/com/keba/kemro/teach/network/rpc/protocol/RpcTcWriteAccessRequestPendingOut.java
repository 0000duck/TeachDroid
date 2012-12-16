package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcWriteAccessRequestPendingOut implements XDR {
	public boolean requestPending;
	public boolean retVal;

	public RpcTcWriteAccessRequestPendingOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeBool(requestPending);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		requestPending = in.readBool();
		retVal = in.readBool();
	}
}