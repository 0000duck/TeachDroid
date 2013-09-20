package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcConvertExeUnitHandleOut implements XDR {
	public int scopeHnd;
	public boolean retVal;

	public SysRpcTcConvertExeUnitHandleOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(scopeHnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		scopeHnd = in.readInt();
		retVal = in.readBool();
	}
}