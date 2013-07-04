package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetRoutineInfoListIn implements XDR {
	public int nrOfRoutineScopeHnd;
	public int[] routineScopeHnd;  //variable length with max length of TCI.rpcChunkLen
	public int routineScopeHnd_count; //countains the number of elements

	public SysRpcTcGetRoutineInfoListIn () {
		routineScopeHnd = new int[TCI.rpcChunkLen];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(nrOfRoutineScopeHnd);
		out.writeInt(routineScopeHnd_count);
		for (int for_i = 0; for_i < routineScopeHnd_count; for_i++) {
			out.writeInt(routineScopeHnd[for_i]);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nrOfRoutineScopeHnd = in.readInt();
		routineScopeHnd_count = in.readInt();
		for (int for_i = 0; for_i < routineScopeHnd_count; for_i++) {
			routineScopeHnd[for_i] = in.readInt();
		}
	}
}