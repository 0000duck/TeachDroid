package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcOpenNodeOut implements XDR {
	public int nodeHnd;
	public RpcTcNodeKind kind;
	public boolean retVal;

	public RpcTcOpenNodeOut () {
		kind = new RpcTcNodeKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(nodeHnd);
		kind.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nodeHnd = in.readInt();
		kind.read(in);
		retVal = in.readBool();
	}
}