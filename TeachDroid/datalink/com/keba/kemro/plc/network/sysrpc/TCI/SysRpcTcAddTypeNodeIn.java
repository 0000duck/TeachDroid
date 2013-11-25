package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcAddTypeNodeIn implements XDR {
	public SysRpcTcNodeInfo nodeInfo;
	public SysRpcTcTypeInfo typeInfo;

	public SysRpcTcAddTypeNodeIn () {
		nodeInfo = new SysRpcTcNodeInfo();
		typeInfo = new SysRpcTcTypeInfo();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		nodeInfo.write(out);
		typeInfo.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nodeInfo.read(in);
		typeInfo.read(in);
	}
}