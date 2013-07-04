package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcReadChangedVarGroupValuesOut implements XDR {
	public int[] varId;  //variable length with max length of rpcChunkLen
	public int varId_count; //countains the number of elements
	public RpcTcValue[] value;  //variable length with max length of rpcChunkLen
	public int value_count; //countains the number of elements
	public boolean retVal;

	public RpcTcReadChangedVarGroupValuesOut () {
		varId = new int[rpcChunkLen.value];
		value = new RpcTcValue[rpcChunkLen.value];
		for (int for_i = 0; for_i < rpcChunkLen.value; for_i++) {
			value[for_i] = new RpcTcValue();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varId_count);
		for (int for_i = 0; for_i < varId_count; for_i++) {
			out.writeInt(varId[for_i]);
		}
		out.writeInt(value_count);
		for (int for_i = 0; for_i < value_count; for_i++) {
			value[for_i].write(out);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varId_count = in.readInt();
		for (int for_i = 0; for_i < varId_count; for_i++) {
			varId[for_i] = in.readInt();
		}
		value_count = in.readInt();
		for (int for_i = 0; for_i < value_count; for_i++) {
			value[for_i].read(in);
		}
		retVal = in.readBool();
	}
}