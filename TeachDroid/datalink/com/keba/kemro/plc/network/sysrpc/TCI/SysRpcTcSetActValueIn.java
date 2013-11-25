package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcSetActValueIn implements XDR {
	public int exeUnitHnd;
	public SysRpcTcVarAccess varAccess;
	public SysRpcTcValue value;

	public SysRpcTcSetActValueIn () {
		varAccess = new SysRpcTcVarAccess();
		value = new SysRpcTcValue();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		varAccess.write(out);
		value.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		varAccess.read(in);
		value.read(in);
	}
}