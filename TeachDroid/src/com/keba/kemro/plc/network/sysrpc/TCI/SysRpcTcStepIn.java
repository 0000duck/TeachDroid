package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcStepIn implements XDR {
	public int exeUnitHnd;
	public SysRpcTcExeStepState stepCmd;

	public SysRpcTcStepIn () {
		stepCmd = new SysRpcTcExeStepState();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		stepCmd.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		stepCmd.read(in);
	}
}