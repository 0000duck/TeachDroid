package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetNextLineChunkOut implements XDR {
	public int iterHnd;
	public String[] lines;  //variable length with max length of TCI.rpcChunkLen
	public int lines_count; //countains the number of elements
	public boolean retVal;

	public SysRpcTcGetNextLineChunkOut () {
		lines = new String[TCI.rpcChunkLen];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(iterHnd);
		out.writeInt(lines_count);
		for (int for_i = 0; for_i < lines_count; for_i++) {
			out.writeString(lines[for_i]);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		iterHnd = in.readInt();
		lines_count = in.readInt();
		for (int for_i = 0; for_i < lines_count; for_i++) {
			lines[for_i] = in.readString();
		}
		retVal = in.readBool();
	}
}