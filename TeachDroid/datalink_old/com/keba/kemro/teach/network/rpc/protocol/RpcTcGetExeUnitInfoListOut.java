package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetExeUnitInfoListOut implements XDR {
	public int nrOfExeUnitHnd;
	public RpcTcExeUnitInfo[] info;  //variable length with max length of rpcChunkLen
	public int info_count; //countains the number of elements
	public boolean retVal;

	public RpcTcGetExeUnitInfoListOut () {
		info = new RpcTcExeUnitInfo[rpcChunkLen.value];
		for (int for_i = 0; for_i < rpcChunkLen.value; for_i++) {
			info[for_i] = new RpcTcExeUnitInfo();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(nrOfExeUnitHnd);
		out.writeInt(info_count);
		for (int for_i = 0; for_i < info_count; for_i++) {
			info[for_i].write(out);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nrOfExeUnitHnd = in.readInt();
		info_count = in.readInt();
		for (int for_i = 0; for_i < info_count; for_i++) {
			info[for_i].read(in);
		}
		retVal = in.readBool();
	}
}