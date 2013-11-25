package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetDirEntryInfoListIn implements XDR {
	public String[] dirEntryPaths;  //variable length with max length of TCI.rpcChunkLen
	public int dirEntryPaths_count; //countains the number of elements

	public SysRpcTcGetDirEntryInfoListIn () {
		dirEntryPaths = new String[TCI.rpcChunkLen];
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