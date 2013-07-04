package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcOpenTeachControlIn implements XDR {
	public SysRpcTcClientType clType;

	public SysRpcTcOpenTeachControlIn () {
		clType = new SysRpcTcClientType();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		clType.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		clType.read(in);
	}
}