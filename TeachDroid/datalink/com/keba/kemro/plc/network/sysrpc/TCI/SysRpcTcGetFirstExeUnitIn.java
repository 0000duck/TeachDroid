package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetFirstExeUnitIn implements XDR {
	public int exeUnitHnd;
	public SysRpcTcExeUnitKind kind;

	public SysRpcTcGetFirstExeUnitIn () {
		kind = new SysRpcTcExeUnitKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		kind.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		kind.read(in);
	}
}