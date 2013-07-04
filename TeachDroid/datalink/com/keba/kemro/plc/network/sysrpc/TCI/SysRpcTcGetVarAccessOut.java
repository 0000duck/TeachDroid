package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetVarAccessOut implements XDR {
	public SysRpcTcVarAccess varAccess;
	public boolean retVal;

	public SysRpcTcGetVarAccessOut () {
		varAccess = new SysRpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		varAccess.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccess.read(in);
		retVal = in.readBool();
	}
}