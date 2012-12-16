package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcOpenVarAccessListIn implements XDR {
	public String[] varAccessPaths;  //variable length with max length of TCI.rpcChunkLen
	public int varAccessPaths_count; //countains the number of elements

	public SysRpcTcOpenVarAccessListIn () {
		varAccessPaths = new String[TCI.rpcChunkLen];
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