package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcExecuteMethodExtOut implements XDR {
	public SysRpcTcValue methodRetVal;
	public boolean retVal;

	public SysRpcTcExecuteMethodExtOut () {
		methodRetVal = new SysRpcTcValue();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		methodRetVal.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		methodRetVal.read(in);
		retVal = in.readBool();
	}
}