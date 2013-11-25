package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetMultExeUnitChunkIn implements XDR {
	public int[] exeUnitHnd;  //variable length with max length of rpcChunkLen
	public int exeUnitHnd_count; //countains the number of elements
	public RpcTcExeUnitKind kind;
	public int iterHnd;

	public RpcTcGetMultExeUnitChunkIn () {
		exeUnitHnd = new int[rpcChunkLen.value];
		kind = new RpcTcExeUnitKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd_count);
		for (int for_i = 0; for_i < exeUnitHnd_count; for_i++) {
			out.writeInt(exeUnitHnd[for_i]);
		}
		kind.write(out);
		out.writeInt(iterHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd_count = in.readInt();
		for (int for_i = 0; for_i < exeUnitHnd_count; for_i++) {
			exeUnitHnd[for_i] = in.readInt();
		}
		kind.read(in);
		iterHnd = in.readInt();
	}
}