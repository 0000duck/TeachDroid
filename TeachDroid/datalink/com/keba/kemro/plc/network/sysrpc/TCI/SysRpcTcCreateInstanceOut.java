package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcCreateInstanceOut implements XDR {
	public int tempVarHnd;
	public boolean retVal;

	public SysRpcTcCreateInstanceOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(tempVarHnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		tempVarHnd = in.readInt();
		retVal = in.readBool();
	}
}