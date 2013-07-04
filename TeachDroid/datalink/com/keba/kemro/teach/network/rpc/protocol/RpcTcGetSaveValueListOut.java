package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetSaveValueListOut implements XDR {
	public RpcTcValue[] value;  //variable length with max length of rpcChunkLen
	public int value_count; //countains the number of elements
	public boolean retVal;

	public RpcTcGetSaveValueListOut () {
		value = new RpcTcValue[rpcChunkLen.value];
		for (int for_i = 0; for_i < rpcChunkLen.value; for_i++) {
			value[for_i] = new RpcTcValue();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value_count);
		for (int for_i = 0; for_i < value_count; for_i++) {
			value[for_i].write(out);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		value_count = in.readInt();
		for (int for_i = 0; for_i < value_count; for_i++) {
			value[for_i].read(in);
		}
		retVal = in.readBool();
	}
}