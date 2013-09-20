package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcExecuteMethodExtIn implements XDR {
	public int routineHnd;
	public RpcTcVarAccess instanceVarAccess;
	public RpcTcMethodParam[] params;  //variable length with max length of rpcMaxParams
	public int params_count; //countains the number of elements

	public RpcTcExecuteMethodExtIn () {
		instanceVarAccess = new RpcTcVarAccess();
		params = new RpcTcMethodParam[rpcMaxParams.value];
		for (int for_i = 0; for_i < rpcMaxParams.value; for_i++) {
			params[for_i] = new RpcTcMethodParam();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineHnd);
		instanceVarAccess.write(out);
		out.writeInt(params_count);
		for (int for_i = 0; for_i < params_count; for_i++) {
			params[for_i].write(out);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineHnd = in.readInt();
		instanceVarAccess.read(in);
		params_count = in.readInt();
		for (int for_i = 0; for_i < params_count; for_i++) {
			params[for_i].read(in);
		}
	}
}