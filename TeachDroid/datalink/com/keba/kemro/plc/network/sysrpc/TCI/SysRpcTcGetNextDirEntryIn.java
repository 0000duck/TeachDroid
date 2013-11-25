package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetNextDirEntryIn implements XDR {
	public String dirPath;
	public SysRpcTcDirEntryKind kind;
	public int iterHnd;

	public SysRpcTcGetNextDirEntryIn () {
		kind = new SysRpcTcDirEntryKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(dirPath);
		kind.write(out);
		out.writeInt(iterHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		dirPath = in.readString();
		kind.read(in);
		iterHnd = in.readInt();
	}
}