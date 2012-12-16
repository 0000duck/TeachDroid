package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetConstInfoListIn implements XDR {
	public int nrOfConstScopeHnd;
	public int[] constScopeHnd;  //variable length with max length of TCI.rpcChunkLen
	public int constScopeHnd_count; //countains the number of elements

	public SysRpcTcGetConstInfoListIn () {
		constScopeHnd = new int[TCI.rpcChunkLen];
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