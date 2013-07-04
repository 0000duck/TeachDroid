package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcRenameDirEntryIn implements XDR {
	public String dirEntryPath;
	public String newDirEntryName;
	public SysRpcTcDirEntryKind kind;

	public SysRpcTcRenameDirEntryIn () {
		kind = new SysRpcTcDirEntryKind();
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