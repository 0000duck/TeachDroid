package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcSetClientTypeIn implements XDR {
	public SysRpcTcClientType type;
	public boolean forceController;

	public SysRpcTcSetClientTypeIn () {
		type = new SysRpcTcClientType();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		type.write(out);
		out.writeBool(forceController);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		type.read(in);
		forceController = in.readBool();
	}
}