package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcSetCodePointIn implements XDR {
	public int routineScopeHnd;
	public int lineNr;
	public int exeUnitHnd;
	public RpcTcInstancePath instancePath;
	public RpcTcCodePointKind kind;

	public RpcTcSetCodePointIn () {
		instancePath = new RpcTcInstancePath();
		kind = new RpcTcCodePointKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineScopeHnd);
		out.writeInt(lineNr);
		out.writeInt(exeUnitHnd);
		instancePath.write(out);
		kind.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineScopeHnd = in.readInt();
		lineNr = in.readInt();
		exeUnitHnd = in.readInt();
		instancePath.read(in);
		kind.read(in);
	}
}