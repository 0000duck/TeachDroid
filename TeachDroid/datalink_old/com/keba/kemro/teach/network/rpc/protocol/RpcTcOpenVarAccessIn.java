package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcOpenVarAccessIn implements XDR {
	public String varAccessPath;

	public RpcTcOpenVarAccessIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(varAccessPath);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccessPath = in.readString();
	}
}