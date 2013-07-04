package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcAddTypeNodeIn implements XDR {
	public RpcTcNodeInfo nodeInfo;
	public RpcTcTypeInfo typeInfo;

	public RpcTcAddTypeNodeIn () {
		nodeInfo = new RpcTcNodeInfo();
		typeInfo = new RpcTcTypeInfo();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		nodeInfo.write(out);
		typeInfo.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nodeInfo.read(in);
		typeInfo.read(in);
	}
}