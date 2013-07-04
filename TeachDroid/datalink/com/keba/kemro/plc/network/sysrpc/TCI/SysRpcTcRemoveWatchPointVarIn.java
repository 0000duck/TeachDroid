package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcRemoveWatchPointVarIn implements XDR {
	public int routineScopeHnd;
	public int lineNr;
	public int wpVarHnd;

	public SysRpcTcRemoveWatchPointVarIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineScopeHnd);
		out.writeInt(lineNr);
		out.writeInt(wpVarHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineScopeHnd = in.readInt();
		lineNr = in.readInt();
		wpVarHnd = in.readInt();
	}
}