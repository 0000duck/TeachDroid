package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcSetWatchPointVarIn implements XDR {
	public int routineScopeHnd;
	public int lineNr;
	public int exeUnitHnd;
	public SysRpcTcInstancePath instPath;

	public SysRpcTcSetWatchPointVarIn () {
		instPath = new SysRpcTcInstancePath();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineScopeHnd);
		out.writeInt(lineNr);
		out.writeInt(exeUnitHnd);
		instPath.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineScopeHnd = in.readInt();
		lineNr = in.readInt();
		exeUnitHnd = in.readInt();
		instPath.read(in);
	}
}