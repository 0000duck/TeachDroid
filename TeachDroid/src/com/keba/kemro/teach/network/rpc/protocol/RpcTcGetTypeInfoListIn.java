package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetTypeInfoListIn implements XDR {
	public int nrOfTypeScopeHnd;
	public int[] typeScopeHnd;  //variable length with max length of rpcChunkLen
	public int typeScopeHnd_count; //countains the number of elements

	public RpcTcGetTypeInfoListIn () {
		typeScopeHnd = new int[rpcChunkLen.value];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(nrOfTypeScopeHnd);
		out.writeInt(typeScopeHnd_count);
		for (int for_i = 0; for_i < typeScopeHnd_count; for_i++) {
			out.writeInt(typeScopeHnd[for_i]);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nrOfTypeScopeHnd = in.readInt();
		typeScopeHnd_count = in.readInt();
		for (int for_i = 0; for_i < typeScopeHnd_count; for_i++) {
			typeScopeHnd[for_i] = in.readInt();
		}
	}
}