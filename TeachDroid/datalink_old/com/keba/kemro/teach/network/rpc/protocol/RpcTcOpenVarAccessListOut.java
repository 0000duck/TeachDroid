package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcOpenVarAccessListOut implements XDR {
	public RpcTcVarAccess[] varAccess;  //variable length with max length of rpcChunkLen
	public int varAccess_count; //countains the number of elements
	public RpcTcVarAccessInfo[] info;  //variable length with max length of rpcChunkLen
	public int info_count; //countains the number of elements
	public boolean retVal;

	public RpcTcOpenVarAccessListOut () {
		varAccess = new RpcTcVarAccess[rpcChunkLen.value];
		for (int for_i = 0; for_i < rpcChunkLen.value; for_i++) {
			varAccess[for_i] = new RpcTcVarAccess();
		}
		info = new RpcTcVarAccessInfo[rpcChunkLen.value];
		for (int for_i = 0; for_i < rpcChunkLen.value; for_i++) {
			info[for_i] = new RpcTcVarAccessInfo();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varAccess_count);
		for (int for_i = 0; for_i < varAccess_count; for_i++) {
			varAccess[for_i].write(out);
		}
		out.writeInt(info_count);
		for (int for_i = 0; for_i < info_count; for_i++) {
			info[for_i].write(out);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccess_count = in.readInt();
		for (int for_i = 0; for_i < varAccess_count; for_i++) {
			varAccess[for_i].read(in);
		}
		info_count = in.readInt();
		for (int for_i = 0; for_i < info_count; for_i++) {
			info[for_i].read(in);
		}
		retVal = in.readBool();
	}
}