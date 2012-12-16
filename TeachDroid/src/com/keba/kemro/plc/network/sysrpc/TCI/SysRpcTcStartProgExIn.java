package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcStartProgExIn implements XDR {
	public int progScopeHnd;
	public int startFlags;

	public SysRpcTcStartProgExIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(progScopeHnd);
		out.writeInt(startFlags);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		progScopeHnd = in.readInt();
		startFlags = in.readInt();
	}
}