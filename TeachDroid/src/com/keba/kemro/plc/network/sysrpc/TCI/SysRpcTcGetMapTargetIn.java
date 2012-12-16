package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetMapTargetIn implements XDR {
	public int exeUnitHnd;
	public SysRpcTcVarAccess varAccess;

	public SysRpcTcGetMapTargetIn () {
		varAccess = new SysRpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		varAccess.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		varAccess.read(in);
	}
}