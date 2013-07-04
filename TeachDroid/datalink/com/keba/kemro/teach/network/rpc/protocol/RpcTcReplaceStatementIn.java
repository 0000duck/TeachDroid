package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcReplaceStatementIn implements XDR {
	public int routineHnd;
	public RpcTcPos begPos;
	public RpcTcPos endPos;
	public String stmtText;

	public RpcTcReplaceStatementIn () {
		begPos = new RpcTcPos();
		endPos = new RpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineHnd);
		begPos.write(out);
		endPos.write(out);
		out.writeString(stmtText);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineHnd = in.readInt();
		begPos.read(in);
		endPos.read(in);
		stmtText = in.readString();
	}
}