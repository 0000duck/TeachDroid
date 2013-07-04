package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetNodeInfoListIn implements XDR {
	public int nrOfScopHnd;
	public int[] scopeHnd;  //variable length with max length of TCI.rpcChunkLen
	public int scopeHnd_count; //countains the number of elements

	public SysRpcTcGetNodeInfoListIn () {
		scopeHnd = new int[TCI.rpcChunkLen];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(nrOfScopHnd);
		out.writeInt(scopeHnd_count);
		for (int for_i = 0; for_i < scopeHnd_count; for_i++) {
			out.writeInt(scopeHnd[for_i]);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nrOfScopHnd = in.readInt();
		scopeHnd_count = in.readInt();
		for (int for_i = 0; for_i < scopeHnd_count; for_i++) {
			scopeHnd[for_i] = in.readInt();
		}
	}
}