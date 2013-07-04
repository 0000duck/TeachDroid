package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetMultExeUnitChunkOut implements XDR {
	public int iterHnd;
	public int nrOfExeUnitHnd;
	public int[] upperExeUnitHnd;  //variable length with max length of rpcChunkLen
	public int upperExeUnitHnd_count; //countains the number of elements
	public int[] exeUnitHnd;  //variable length with max length of rpcChunkLen
	public int exeUnitHnd_count; //countains the number of elements
	public boolean retVal;

	public RpcTcGetMultExeUnitChunkOut () {
		upperExeUnitHnd = new int[rpcChunkLen.value];
		exeUnitHnd = new int[rpcChunkLen.value];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(iterHnd);
		out.writeInt(nrOfExeUnitHnd);
		out.writeInt(upperExeUnitHnd_count);
		for (int for_i = 0; for_i < upperExeUnitHnd_count; for_i++) {
			out.writeInt(upperExeUnitHnd[for_i]);
		}
		out.writeInt(exeUnitHnd_count);
		for (int for_i = 0; for_i < exeUnitHnd_count; for_i++) {
			out.writeInt(exeUnitHnd[for_i]);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		iterHnd = in.readInt();
		nrOfExeUnitHnd = in.readInt();
		upperExeUnitHnd_count = in.readInt();
		for (int for_i = 0; for_i < upperExeUnitHnd_count; for_i++) {
			upperExeUnitHnd[for_i] = in.readInt();
		}
		exeUnitHnd_count = in.readInt();
		for (int for_i = 0; for_i < exeUnitHnd_count; for_i++) {
			exeUnitHnd[for_i] = in.readInt();
		}
		retVal = in.readBool();
	}
}