package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetFirstDirEntryChunkIn implements XDR {
	public String dirPath;
	public SysRpcTcDirEntryKind kind;

	public SysRpcTcGetFirstDirEntryChunkIn () {
		kind = new SysRpcTcDirEntryKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(dirPath);
		kind.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		dirPath = in.readString();
		kind.read(in);
	}
}