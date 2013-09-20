package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcStopProgIn implements XDR {
	public int exeUnitHnd;

	public RpcTcStopProgIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
	}
}