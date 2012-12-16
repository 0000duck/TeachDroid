package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcAddRoutineNodeIn implements XDR {
	public SysRpcTcNodeInfo nodeInfo;
	public SysRpcTcRoutineInfo routInfo;

	public SysRpcTcAddRoutineNodeIn () {
		nodeInfo = new SysRpcTcNodeInfo();
		routInfo = new SysRpcTcRoutineInfo();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		nodeInfo.write(out);
		routInfo.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nodeInfo.read(in);
		routInfo.read(in);
	}
}