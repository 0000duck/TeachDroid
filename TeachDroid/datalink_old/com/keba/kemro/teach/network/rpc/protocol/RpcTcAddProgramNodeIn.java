package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcAddProgramNodeIn implements XDR {
	public RpcTcNodeInfo nodeInfo;

	public RpcTcAddProgramNodeIn () {
		nodeInfo = new RpcTcNodeInfo();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		nodeInfo.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nodeInfo.read(in);
	}
}