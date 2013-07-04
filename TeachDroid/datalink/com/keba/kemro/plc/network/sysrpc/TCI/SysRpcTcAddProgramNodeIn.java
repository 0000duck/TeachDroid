package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcAddProgramNodeIn implements XDR {
	public SysRpcTcNodeInfo nodeInfo;

	public SysRpcTcAddProgramNodeIn () {
		nodeInfo = new SysRpcTcNodeInfo();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		nodeInfo.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nodeInfo.read(in);
	}
}