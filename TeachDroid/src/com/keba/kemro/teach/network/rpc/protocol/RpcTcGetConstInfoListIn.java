package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetConstInfoListIn implements XDR {
	public int nrOfConstScopeHnd;
	public int[] constScopeHnd;  //variable length with max length of rpcChunkLen
	public int constScopeHnd_count; //countains the number of elements

	public RpcTcGetConstInfoListIn () {
		constScopeHnd = new int[rpcChunkLen.value];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(nrOfConstScopeHnd);
		out.writeInt(constScopeHnd_count);
		for (int for_i = 0; for_i < constScopeHnd_count; for_i++) {
			out.writeInt(constScopeHnd[for_i]);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nrOfConstScopeHnd = in.readInt();
		constScopeHnd_count = in.readInt();
		for (int for_i = 0; for_i < constScopeHnd_count; for_i++) {
			constScopeHnd[for_i] = in.readInt();
		}
	}
}