package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetVarInfoListIn implements XDR {
	public int nrOfVarScopeHnd;
	public int[] varScopeHnd;  //variable length with max length of rpcChunkLen
	public int varScopeHnd_count; //countains the number of elements

	public RpcTcGetVarInfoListIn () {
		varScopeHnd = new int[rpcChunkLen.value];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(nrOfVarScopeHnd);
		out.writeInt(varScopeHnd_count);
		for (int for_i = 0; for_i < varScopeHnd_count; for_i++) {
			out.writeInt(varScopeHnd[for_i]);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nrOfVarScopeHnd = in.readInt();
		varScopeHnd_count = in.readInt();
		for (int for_i = 0; for_i < varScopeHnd_count; for_i++) {
			varScopeHnd[for_i] = in.readInt();
		}
	}
}