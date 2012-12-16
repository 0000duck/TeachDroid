package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetHandleIn implements XDR {
	public String name;

	public SysRpcTcGetHandleIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(name);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		name = in.readString();
	}
}