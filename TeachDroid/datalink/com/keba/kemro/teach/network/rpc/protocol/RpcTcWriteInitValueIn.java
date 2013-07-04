package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcWriteInitValueIn implements XDR {
	public int varHnd;

	public RpcTcWriteInitValueIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varHnd = in.readInt();
	}
}