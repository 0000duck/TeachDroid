package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcCreateInstanceIn implements XDR {
	public int typeHnd;

	public RpcTcCreateInstanceIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(typeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		typeHnd = in.readInt();
	}
}