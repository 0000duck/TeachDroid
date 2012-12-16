package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcInsertTerminalSymbolIn implements XDR {
	public int editHnd;
	public RpcTcPos pos;
	public int kind;

	public RpcTcInsertTerminalSymbolIn () {
		pos = new RpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
		pos.write(out);
		out.writeInt(kind);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
		pos.read(in);
		kind = in.readInt();
	}
}