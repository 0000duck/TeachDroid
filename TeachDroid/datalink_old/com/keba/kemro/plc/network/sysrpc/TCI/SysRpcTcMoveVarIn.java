package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcMoveVarIn implements XDR {
	public int varHnd;
	public int newScopeHnd;

	public SysRpcTcMoveVarIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varHnd);
		out.writeInt(newScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varHnd = in.readInt();
		newScopeHnd = in.readInt();
	}
}