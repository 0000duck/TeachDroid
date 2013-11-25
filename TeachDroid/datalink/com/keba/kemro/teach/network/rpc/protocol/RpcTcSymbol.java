package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcSymbol implements XDR {
	public int hnd;
	public RpcTcSymbolKind kind;
	public String text;

	public RpcTcSymbol () {
		kind = new RpcTcSymbolKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(hnd);
		kind.write(out);
		out.writeString(text);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		hnd = in.readInt();
		kind.read(in);
		text = in.readString();
	}
}