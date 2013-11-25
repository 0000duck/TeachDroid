package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcOpenVarAccessIn implements XDR {
	public String varAccessPath;

	public SysRpcTcOpenVarAccessIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(varAccessPath);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccessPath = in.readString();
	}
}