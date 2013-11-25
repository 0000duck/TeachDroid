package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetHandleNameIn implements XDR {
	public int hnd;

	public RpcTcGetHandleNameIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(hnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		hnd = in.readInt();
	}
}