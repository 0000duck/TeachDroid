package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcExecuteMethodOut implements XDR {
	public boolean methodRetVal;
	public boolean retVal;

	public SysRpcTcExecuteMethodOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeBool(methodRetVal);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		methodRetVal = in.readBool();
		retVal = in.readBool();
	}
}