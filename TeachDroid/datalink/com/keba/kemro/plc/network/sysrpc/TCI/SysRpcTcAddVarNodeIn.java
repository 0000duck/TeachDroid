package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcAddVarNodeIn implements XDR {
	public SysRpcTcNodeInfo nodeInfo;
	public SysRpcTcVarInfo varInfo;

	public SysRpcTcAddVarNodeIn () {
		nodeInfo = new SysRpcTcNodeInfo();
		varInfo = new SysRpcTcVarInfo();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		nodeInfo.write(out);
		varInfo.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nodeInfo.read(in);
		varInfo.read(in);
	}
}