package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetWatchPointCounterIn implements XDR {
	public int routineScopeHnd;
	public int lineNr;

	public SysRpcTcGetWatchPointCounterIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineScopeHnd);
		out.writeInt(lineNr);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineScopeHnd = in.readInt();
		lineNr = in.readInt();
	}
}