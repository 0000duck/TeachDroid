package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcAddConstNodeIn implements XDR {
	public SysRpcTcNodeInfo nodeInfo;
	public SysRpcTcConstInfo constInfo;

	public SysRpcTcAddConstNodeIn () {
		nodeInfo = new SysRpcTcNodeInfo();
		constInfo = new SysRpcTcConstInfo();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		nodeInfo.write(out);
		constInfo.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nodeInfo.read(in);
		constInfo.read(in);
	}
}