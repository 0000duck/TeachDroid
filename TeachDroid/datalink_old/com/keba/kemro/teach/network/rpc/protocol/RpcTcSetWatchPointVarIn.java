package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcSetWatchPointVarIn implements XDR {
	public int routineScopeHnd;
	public int lineNr;
	public int exeUnitHnd;
	public RpcTcInstancePath instPath;

	public RpcTcSetWatchPointVarIn () {
		instPath = new RpcTcInstancePath();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineScopeHnd);
		out.writeInt(lineNr);
		out.writeInt(exeUnitHnd);
		instPath.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineScopeHnd = in.readInt();
		lineNr = in.readInt();
		exeUnitHnd = in.readInt();
		instPath.read(in);
	}
}