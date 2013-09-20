package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcRenameNodeIn implements XDR {
	public int scopeHnd;
	public String newName;

	public SysRpcTcRenameNodeIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(scopeHnd);
		out.writeString(newName);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		scopeHnd = in.readInt();
		newName = in.readString();
	}
}