package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcSetMapTargetIn implements XDR {
	public int exeUnitHnd;
	public SysRpcTcVarAccess varAccess;
	public SysRpcTcMapTarget target;

	public SysRpcTcSetMapTargetIn () {
		varAccess = new SysRpcTcVarAccess();
		target = new SysRpcTcMapTarget();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		varAccess.write(out);
		target.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		varAccess.read(in);
		target.read(in);
	}
}