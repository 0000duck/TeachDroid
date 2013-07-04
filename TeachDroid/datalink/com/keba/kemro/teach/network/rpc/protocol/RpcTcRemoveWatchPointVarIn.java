package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcRemoveWatchPointVarIn implements XDR {
	public int routineScopeHnd;
	public int lineNr;
	public int wpVarHnd;

	public RpcTcRemoveWatchPointVarIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineScopeHnd);
		out.writeInt(lineNr);
		out.writeInt(wpVarHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineScopeHnd = in.readInt();
		lineNr = in.readInt();
		wpVarHnd = in.readInt();
	}
}