package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcDeleteDirEntryIn implements XDR {
	public String dirEntryPath;

	public SysRpcTcDeleteDirEntryIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(dirEntryPath);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		dirEntryPath = in.readString();
	}
}