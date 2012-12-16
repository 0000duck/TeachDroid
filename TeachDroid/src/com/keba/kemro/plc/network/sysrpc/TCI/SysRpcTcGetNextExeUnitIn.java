package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetNextExeUnitIn implements XDR {
	public int exeUnitHnd;
	public SysRpcTcExeUnitKind kind;
	public int iterHnd;

	public SysRpcTcGetNextExeUnitIn () {
		kind = new SysRpcTcExeUnitKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		kind.write(out);
		out.writeInt(iterHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		kind.read(in);
		iterHnd = in.readInt();
	}
}