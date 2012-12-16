package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetNodeTextRangeIn implements XDR {
	public int editHnd;
	public int nodeHnd;

	public RpcTcGetNodeTextRangeIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
		out.writeInt(nodeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
		nodeHnd = in.readInt();
	}
}