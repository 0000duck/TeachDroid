package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcOpenVarAccessOut implements XDR {
	public SysRpcTcVarAccess varAccess;
	public SysRpcTcTypeKind type;
	public boolean retVal;

	public SysRpcTcOpenVarAccessOut () {
		varAccess = new SysRpcTcVarAccess();
		type = new SysRpcTcTypeKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		varAccess.write(out);
		type.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccess.read(in);
		type.read(in);
		retVal = in.readBool();
	}
}