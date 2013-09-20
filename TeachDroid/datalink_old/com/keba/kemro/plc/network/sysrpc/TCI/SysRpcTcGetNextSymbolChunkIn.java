package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetNextSymbolChunkIn implements XDR {
	public int editHnd;
	public int nonTermHnd;
	public int iterHnd;

	public SysRpcTcGetNextSymbolChunkIn () {
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