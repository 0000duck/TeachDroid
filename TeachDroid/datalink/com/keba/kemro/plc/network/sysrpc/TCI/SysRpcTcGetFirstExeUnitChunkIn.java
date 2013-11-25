package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetFirstExeUnitChunkIn implements XDR {
	public int exeUnitHnd;
	public SysRpcTcExeUnitKind kind;

	public SysRpcTcGetFirstExeUnitChunkIn () {
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