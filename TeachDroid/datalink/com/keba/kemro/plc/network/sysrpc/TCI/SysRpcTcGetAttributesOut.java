package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetAttributesOut implements XDR {
	public String[] attributes;  //variable length with max length of TCI.rpcChunkLen
	public int attributes_count; //countains the number of elements
	public boolean retVal;

	public SysRpcTcGetAttributesOut () {
		attributes = new String[TCI.rpcChunkLen];
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