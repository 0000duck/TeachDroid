package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcOpenTeachControlIn implements XDR {
	public RpcTcClientType clType;

	public RpcTcOpenTeachControlIn () {
		clType = new RpcTcClientType();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		clType.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		clType.read(in);
	}
}