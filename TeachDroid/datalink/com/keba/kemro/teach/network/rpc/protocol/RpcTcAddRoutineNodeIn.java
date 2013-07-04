package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcAddRoutineNodeIn implements XDR {
	public RpcTcNodeInfo nodeInfo;
	public RpcTcRoutineInfo routInfo;

	public RpcTcAddRoutineNodeIn () {
		nodeInfo = new RpcTcNodeInfo();
		routInfo = new RpcTcRoutineInfo();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		nodeInfo.write(out);
		routInfo.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nodeInfo.read(in);
		routInfo.read(in);
	}
}