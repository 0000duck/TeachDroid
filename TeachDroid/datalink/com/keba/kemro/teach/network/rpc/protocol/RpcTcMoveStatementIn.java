package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcMoveStatementIn implements XDR {
	public int editHnd;
	public RpcTcPos pos;
	public RpcTcSymbolKind nonTerm;
	public RpcTcPos toPos;

	public RpcTcMoveStatementIn () {
		pos = new RpcTcPos();
		nonTerm = new RpcTcSymbolKind();
		toPos = new RpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
		pos.write(out);
		nonTerm.write(out);
		toPos.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
		pos.read(in);
		nonTerm.read(in);
		toPos.read(in);
	}
}