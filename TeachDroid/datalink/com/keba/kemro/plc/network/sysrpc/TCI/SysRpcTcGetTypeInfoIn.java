package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetTypeInfoIn implements XDR {
	public int typeScopeHnd;

	public SysRpcTcGetTypeInfoIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(typeScopeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		typeScopeHnd = in.readInt();
	}
}