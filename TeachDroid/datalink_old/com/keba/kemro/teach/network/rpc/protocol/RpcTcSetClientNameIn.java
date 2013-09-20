package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcSetClientNameIn implements XDR {
	public String name;

	public RpcTcSetClientNameIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(name);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		name = in.readString();
	}
}