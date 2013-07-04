package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcAddDirEntryIn implements XDR {
	public String dirPath;
	public String dirEntryName;
	public RpcTcDirEntryKind kind;

	public RpcTcAddDirEntryIn () {
		kind = new RpcTcDirEntryKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(dirPath);
		out.writeString(dirEntryName);
		kind.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		dirPath = in.readString();
		dirEntryName = in.readString();
		kind.read(in);
	}
}