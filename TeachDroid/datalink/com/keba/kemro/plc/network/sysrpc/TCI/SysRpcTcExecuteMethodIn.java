package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcExecuteMethodIn implements XDR {
	public String methodName;
	public int instanceExeUnitHnd;
	public SysRpcTcVarAccess instanceVarAccess;
	public int paramExeUnitHnd;
	public SysRpcTcVarAccess paramVarAccess;

	public SysRpcTcExecuteMethodIn () {
		instanceVarAccess = new SysRpcTcVarAccess();
		paramVarAccess = new SysRpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(methodName);
		out.writeInt(instanceExeUnitHnd);
		instanceVarAccess.write(out);
		out.writeInt(paramExeUnitHnd);
		paramVarAccess.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		methodName = in.readString();
		instanceExeUnitHnd = in.readInt();
		instanceVarAccess.read(in);
		paramExeUnitHnd = in.readInt();
		paramVarAccess.read(in);
	}
}