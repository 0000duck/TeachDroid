package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcSetDebugModeIn implements XDR {
	public int exeUnitHnd;
	public SysRpcTcExeFlags exeFlags;

	public SysRpcTcSetDebugModeIn () {
		exeFlags = new SysRpcTcExeFlags();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		exeFlags.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		exeFlags.read(in);
	}
}