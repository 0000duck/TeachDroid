package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcAddVarsToGroupIn implements XDR {
	public int varGroupHnd;
	public int[] exeUnitHnd;  //variable length with max length of rpcChunkLen
	public int exeUnitHnd_count; //countains the number of elements
	public RpcTcVarAccess[] varAccess;  //variable length with max length of rpcChunkLen
	public int varAccess_count; //countains the number of elements

	public RpcTcAddVarsToGroupIn () {
		exeUnitHnd = new int[rpcChunkLen.value];
		varAccess = new RpcTcVarAccess[rpcChunkLen.value];
		for (int for_i = 0; for_i < rpcChunkLen.value; for_i++) {
			varAccess[for_i] = new RpcTcVarAccess();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varGroupHnd);
		out.writeInt(exeUnitHnd_count);
		for (int for_i = 0; for_i < exeUnitHnd_count; for_i++) {
			out.writeInt(exeUnitHnd[for_i]);
		}
		out.writeInt(varAccess_count);
		for (int for_i = 0; for_i < varAccess_count; for_i++) {
			varAccess[for_i].write(out);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varGroupHnd = in.readInt();
		exeUnitHnd_count = in.readInt();
		for (int for_i = 0; for_i < exeUnitHnd_count; for_i++) {
			exeUnitHnd[for_i] = in.readInt();
		}
		varAccess_count = in.readInt();
		for (int for_i = 0; for_i < varAccess_count; for_i++) {
			varAccess[for_i].read(in);
		}
	}
}