package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcDeleteDirEntryExtIn implements XDR {
	public String dirEntryPath;
	public boolean forced;

	public RpcTcDeleteDirEntryExtIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(dirEntryPath);
		out.writeBool(forced);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		dirEntryPath = in.readString();
		forced = in.readBool();
	}
}