package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetRoutineInfoIn implements XDR {
	public int routineScopeHnd;

	public RpcTcGetRoutineInfoIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineScopeHnd = in.readInt();
	}
}