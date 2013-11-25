package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetStructElemAccessOut implements XDR {
	public SysRpcTcVarAccess elemAccess;
	public boolean retVal;

	public SysRpcTcGetStructElemAccessOut () {
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