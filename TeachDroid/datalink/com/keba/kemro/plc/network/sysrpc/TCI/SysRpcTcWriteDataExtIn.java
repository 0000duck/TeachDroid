package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcWriteDataExtIn implements XDR {
	public int scopeHnd;
	public boolean strict;
	public boolean clean;

	public SysRpcTcWriteDataExtIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(scopeHnd);
		out.writeBool(strict);
		out.writeBool(clean);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		scopeHnd = in.readInt();
		strict = in.readBool();
		clean = in.readBool();
	}
}