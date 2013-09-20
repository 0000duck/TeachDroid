package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcAddVarNodeIn implements XDR {
	public RpcTcNodeInfo nodeInfo;
	public RpcTcVarInfo varInfo;

	public RpcTcAddVarNodeIn () {
		nodeInfo = new RpcTcNodeInfo();
		varInfo = new RpcTcVarInfo();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		nodeInfo.write(out);
		varInfo.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nodeInfo.read(in);
		varInfo.read(in);
	}
}