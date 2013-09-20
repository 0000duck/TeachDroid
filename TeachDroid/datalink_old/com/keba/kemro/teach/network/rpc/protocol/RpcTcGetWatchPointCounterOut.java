package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetWatchPointCounterOut implements XDR {
	public int counter;
	public boolean retVal;

	public RpcTcGetWatchPointCounterOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(counter);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		counter = in.readInt();
		retVal = in.readBool();
	}
}