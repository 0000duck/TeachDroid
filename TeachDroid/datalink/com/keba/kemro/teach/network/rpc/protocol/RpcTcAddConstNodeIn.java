package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcAddConstNodeIn implements XDR {
	public RpcTcNodeInfo nodeInfo;
	public RpcTcConstInfo constInfo;

	public RpcTcAddConstNodeIn () {
		nodeInfo = new RpcTcNodeInfo();
		constInfo = new RpcTcConstInfo();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		nodeInfo.write(out);
		constInfo.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nodeInfo.read(in);
		constInfo.read(in);
	}
}