package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcAddDirEntryIn implements XDR {
	public String dirPath;
	public String dirEntryName;
	public SysRpcTcDirEntryKind kind;

	public SysRpcTcAddDirEntryIn () {
		kind = new SysRpcTcDirEntryKind();
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