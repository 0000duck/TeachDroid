package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetCodePointRoutinesOut implements XDR {
	public int chgCnt;
	public int[] routineScopeHnd;  //variable length with max length of rpcChunkLen
	public int routineScopeHnd_count; //countains the number of elements
	public boolean retVal;

	public RpcTcGetCodePointRoutinesOut () {
		routineScopeHnd = new int[rpcChunkLen.value];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(chgCnt);
		out.writeInt(routineScopeHnd_count);
		for (int for_i = 0; for_i < routineScopeHnd_count; for_i++) {
			out.writeInt(routineScopeHnd[for_i]);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		chgCnt = in.readInt();
		routineScopeHnd_count = in.readInt();
		for (int for_i = 0; for_i < routineScopeHnd_count; for_i++) {
			routineScopeHnd[for_i] = in.readInt();
		}
		retVal = in.readBool();
	}
}