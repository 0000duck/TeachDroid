package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcOpenVarAccessListIn implements XDR {
	public String[] varAccessPaths;  //variable length with max length of rpcChunkLen
	public int varAccessPaths_count; //countains the number of elements

	public RpcTcOpenVarAccessListIn () {
		varAccessPaths = new String[rpcChunkLen.value];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varAccessPaths_count);
		for (int for_i = 0; for_i < varAccessPaths_count; for_i++) {
			out.writeString(varAccessPaths[for_i]);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccessPaths_count = in.readInt();
		for (int for_i = 0; for_i < varAccessPaths_count; for_i++) {
			varAccessPaths[for_i] = in.readString();
		}
	}
}