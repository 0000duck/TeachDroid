package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetDirEntryInfoListIn implements XDR {
	public String[] dirEntryPaths;  //variable length with max length of rpcChunkLen
	public int dirEntryPaths_count; //countains the number of elements

	public RpcTcGetDirEntryInfoListIn () {
		dirEntryPaths = new String[rpcChunkLen.value];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(dirEntryPaths_count);
		for (int for_i = 0; for_i < dirEntryPaths_count; for_i++) {
			out.writeString(dirEntryPaths[for_i]);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		dirEntryPaths_count = in.readInt();
		for (int for_i = 0; for_i < dirEntryPaths_count; for_i++) {
			dirEntryPaths[for_i] = in.readString();
		}
	}
}