package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetExeUnitStatusListOut implements XDR {
	public int nrOfExeUnitHnd;
	public RpcTcExeUnitStatus[] status;  //variable length with max length of rpcChunkLen
	public int status_count; //countains the number of elements
	public boolean retVal;

	public RpcTcGetExeUnitStatusListOut () {
		status = new RpcTcExeUnitStatus[rpcChunkLen.value];
		for (int for_i = 0; for_i < rpcChunkLen.value; for_i++) {
			status[for_i] = new RpcTcExeUnitStatus();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(nrOfExeUnitHnd);
		out.writeInt(status_count);
		for (int for_i = 0; for_i < status_count; for_i++) {
			status[for_i].write(out);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nrOfExeUnitHnd = in.readInt();
		status_count = in.readInt();
		for (int for_i = 0; for_i < status_count; for_i++) {
			status[for_i].read(in);
		}
		retVal = in.readBool();
	}
}