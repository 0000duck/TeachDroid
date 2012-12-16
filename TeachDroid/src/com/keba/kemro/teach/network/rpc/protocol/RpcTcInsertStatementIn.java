package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcInsertStatementIn implements XDR {
	public int routineHnd;
	public RpcTcPos pos;
	public String stmtText;

	public RpcTcInsertStatementIn () {
		pos = new RpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineHnd);
		pos.write(out);
		out.writeString(stmtText);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineHnd = in.readInt();
		pos.read(in);
		stmtText = in.readString();
	}
}