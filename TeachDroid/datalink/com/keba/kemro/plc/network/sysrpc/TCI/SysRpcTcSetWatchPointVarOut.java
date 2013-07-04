package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcSetWatchPointVarOut implements XDR {
	public int wpVarHnd;
	public boolean retVal;

	public SysRpcTcSetWatchPointVarOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(wpVarHnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		wpVarHnd = in.readInt();
		retVal = in.readBool();
	}
}