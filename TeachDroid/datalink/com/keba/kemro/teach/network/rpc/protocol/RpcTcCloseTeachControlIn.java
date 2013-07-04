package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcCloseTeachControlIn implements XDR {
	public int clientHnd;

	public RpcTcCloseTeachControlIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(clientHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		clientHnd = in.readInt();
	}
}