package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetArrayElemAccessOut implements XDR {
	public SysRpcTcVarAccess elemAccess;
	public boolean retVal;

	public SysRpcTcGetArrayElemAccessOut () {
		elemAccess = new SysRpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		elemAccess.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		elemAccess.read(in);
		retVal = in.readBool();
	}
}