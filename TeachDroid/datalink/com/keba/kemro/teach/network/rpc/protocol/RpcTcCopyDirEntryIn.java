package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcCopyDirEntryIn implements XDR {
	public String srcDirEntryPath;
	public String destDirPath;
	public String destdirEntryName;
	public RpcTcDirEntryKind kind;

	public RpcTcCopyDirEntryIn () {
		kind = new RpcTcDirEntryKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(srcDirEntryPath);
		out.writeString(destDirPath);
		out.writeString(destdirEntryName);
		kind.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		srcDirEntryPath = in.readString();
		destDirPath = in.readString();
		destdirEntryName = in.readString();
		kind.read(in);
	}
}