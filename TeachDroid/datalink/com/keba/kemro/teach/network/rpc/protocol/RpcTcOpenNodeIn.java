package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcOpenNodeIn implements XDR {
	public String nodePath;

	public RpcTcOpenNodeIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(nodePath);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nodePath = in.readString();
	}
}