package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcBuildIn implements XDR {
	public String dirEntryPath;

	public RpcTcBuildIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(dirEntryPath);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		dirEntryPath = in.readString();
	}
}