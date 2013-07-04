package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetAttributesOut implements XDR {
	public String[] attributes;  //variable length with max length of rpcChunkLen
	public int attributes_count; //countains the number of elements
	public boolean retVal;

	public RpcTcGetAttributesOut () {
		attributes = new String[rpcChunkLen.value];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(attributes_count);
		for (int for_i = 0; for_i < attributes_count; for_i++) {
			out.writeString(attributes[for_i]);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		attributes_count = in.readInt();
		for (int for_i = 0; for_i < attributes_count; for_i++) {
			attributes[for_i] = in.readString();
		}
		retVal = in.readBool();
	}
}