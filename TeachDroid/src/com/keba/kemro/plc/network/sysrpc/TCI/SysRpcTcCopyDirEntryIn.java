package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcCopyDirEntryIn implements XDR {
	public String srcDirEntryPath;
	public String destDirPath;
	public String destdirEntryName;
	public SysRpcTcDirEntryKind kind;

	public SysRpcTcCopyDirEntryIn () {
		kind = new SysRpcTcDirEntryKind();
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