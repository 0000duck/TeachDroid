package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcResetWatchPointCounterIn implements XDR {
	public int routineScopeHnd;
	public int lineNr;

	public SysRpcTcResetWatchPointCounterIn () {
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