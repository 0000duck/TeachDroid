package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetHandleOut implements XDR {
	public int hnd;
	public boolean retVal;

	public SysRpcTcGetHandleOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(hnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		hnd = in.readInt();
		retVal = in.readBool();
	}
}