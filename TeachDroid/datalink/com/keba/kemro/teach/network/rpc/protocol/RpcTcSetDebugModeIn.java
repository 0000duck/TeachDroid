package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcSetDebugModeIn implements XDR {
	public int exeUnitHnd;
	public RpcTcExeFlags exeFlags;

	public RpcTcSetDebugModeIn () {
		exeFlags = new RpcTcExeFlags();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		exeFlags.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		exeFlags.read(in);
	}
}