package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcRenameDirEntryIn implements XDR {
	public String dirEntryPath;
	public String newDirEntryName;
	public RpcTcDirEntryKind kind;

	public RpcTcRenameDirEntryIn () {
		kind = new RpcTcDirEntryKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(dirEntryPath);
		out.writeString(newDirEntryName);
		kind.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		dirEntryPath = in.readString();
		newDirEntryName = in.readString();
		kind.read(in);
	}
}