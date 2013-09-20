package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetMultExeUnitChunkIn implements XDR {
	public int[] exeUnitHnd;  //variable length with max length of TCI.rpcChunkLen
	public int exeUnitHnd_count; //countains the number of elements
	public SysRpcTcExeUnitKind kind;
	public int iterHnd;

	public SysRpcTcGetMultExeUnitChunkIn () {
		exeUnitHnd = new int[TCI.rpcChunkLen];
		kind = new SysRpcTcExeUnitKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd_count);
		for (int for_i = 0; for_i < exeUnitHnd_count; for_i++) {
			out.writeInt(exeUnitHnd[for_i]);
		}
		kind.write(out);
		out.writeInt(iterHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd_count = in.readInt();
		for (int for_i = 0; for_i < exeUnitHnd_count; for_i++) {
			exeUnitHnd[for_i] = in.readInt();
		}
		kind.read(in);
		iterHnd = in.readInt();
	}
}