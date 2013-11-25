package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetNextNodeOut implements XDR {
	public int iterHnd;
	public int nodeHnd;
	public boolean retVal;

	public RpcTcGetNextNodeOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(iterHnd);
		out.writeInt(nodeHnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		iterHnd = in.readInt();
		nodeHnd = in.readInt();
		retVal = in.readBool();
	}
}