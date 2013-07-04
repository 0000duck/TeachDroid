package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcOpenNodeIn implements XDR {
	public String nodePath;

	public SysRpcTcOpenNodeIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(nodePath);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nodePath = in.readString();
	}
}