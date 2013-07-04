package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcDeleteStatementIn implements XDR {
	public int routineHnd;
	public RpcTcPos begPos;
	public RpcTcPos endPos;

	public RpcTcDeleteStatementIn () {
		begPos = new RpcTcPos();
		endPos = new RpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineHnd);
		begPos.write(out);
		endPos.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineHnd = in.readInt();
		begPos.read(in);
		endPos.read(in);
	}
}