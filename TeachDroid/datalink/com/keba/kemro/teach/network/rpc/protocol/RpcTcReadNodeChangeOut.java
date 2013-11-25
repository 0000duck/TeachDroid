package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcReadNodeChangeOut implements XDR {
	public RpcTcNodeChange[] changes;  //variable length with max length of rpcChunkLen
	public int changes_count; //countains the number of elements
	public int nrChanges;
	public boolean retVal;

	public RpcTcReadNodeChangeOut () {
		changes = new RpcTcNodeChange[rpcChunkLen.value];
		for (int for_i = 0; for_i < rpcChunkLen.value; for_i++) {
			changes[for_i] = new RpcTcNodeChange();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(changes_count);
		for (int for_i = 0; for_i < changes_count; for_i++) {
			changes[for_i].write(out);
		}
		out.writeInt(nrChanges);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		changes_count = in.readInt();
		for (int for_i = 0; for_i < changes_count; for_i++) {
			changes[for_i].read(in);
		}
		nrChanges = in.readInt();
		retVal = in.readBool();
	}
}