package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcSetClientTypeIn implements XDR {
	public RpcTcClientType type;
	public boolean forceController;

	public RpcTcSetClientTypeIn () {
		type = new RpcTcClientType();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		type.write(out);
		out.writeBool(forceController);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		type.read(in);
		forceController = in.readBool();
	}
}