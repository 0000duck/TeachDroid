package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetNextSymbolChunkIn implements XDR {
	public int editHnd;
	public int nonTermHnd;
	public int iterHnd;

	public RpcTcGetNextSymbolChunkIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
		out.writeInt(nonTermHnd);
		out.writeInt(iterHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
		nonTermHnd = in.readInt();
		iterHnd = in.readInt();
	}
}