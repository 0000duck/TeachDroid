package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcDeleteDirEntryExtIn implements XDR {
	public String dirEntryPath;
	public boolean forced;

	public SysRpcTcDeleteDirEntryExtIn () {
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