package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcCreateInstanceIn implements XDR {
	public int typeHnd;

	public SysRpcTcCreateInstanceIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(typeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		typeHnd = in.readInt();
	}
}