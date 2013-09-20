package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetDirEntryInfoListOut implements XDR {
	public RpcTcDirEntryInfo[] infos;  //variable length with max length of rpcChunkLen
	public int infos_count; //countains the number of elements
	public boolean retVal;

	public RpcTcGetDirEntryInfoListOut () {
		infos = new RpcTcDirEntryInfo[rpcChunkLen.value];
		for (int for_i = 0; for_i < rpcChunkLen.value; for_i++) {
			infos[for_i] = new RpcTcDirEntryInfo();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(infos_count);
		for (int for_i = 0; for_i < infos_count; for_i++) {
			infos[for_i].write(out);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		infos_count = in.readInt();
		for (int for_i = 0; for_i < infos_count; for_i++) {
			infos[for_i].read(in);
		}
		retVal = in.readBool();
	}
}