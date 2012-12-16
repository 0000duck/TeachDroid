package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetNextNodeChunkOut implements XDR {
	public int iterHnd;
	public int nrOfHnd;
	public int[] nodeHnd;  //variable length with max length of rpcChunkLen
	public int nodeHnd_count; //countains the number of elements
	public boolean retVal;

	public RpcTcGetNextNodeChunkOut () {
		nodeHnd = new int[rpcChunkLen.value];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(iterHnd);
		out.writeInt(nrOfHnd);
		out.writeInt(nodeHnd_count);
		for (int for_i = 0; for_i < nodeHnd_count; for_i++) {
			out.writeInt(nodeHnd[for_i]);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		iterHnd = in.readInt();
		nrOfHnd = in.readInt();
		nodeHnd_count = in.readInt();
		for (int for_i = 0; for_i < nodeHnd_count; for_i++) {
			nodeHnd[for_i] = in.readInt();
		}
		retVal = in.readBool();
	}
}