package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetAttributesIn implements XDR {
	public int[] scopeHnd;  //variable length with max length of rpcChunkLen
	public int scopeHnd_count; //countains the number of elements

	public RpcTcGetAttributesIn () {
		scopeHnd = new int[rpcChunkLen.value];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(scopeHnd_count);
		for (int for_i = 0; for_i < scopeHnd_count; for_i++) {
			out.writeInt(scopeHnd[for_i]);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		scopeHnd_count = in.readInt();
		for (int for_i = 0; for_i < scopeHnd_count; for_i++) {
			scopeHnd[for_i] = in.readInt();
		}
	}
}