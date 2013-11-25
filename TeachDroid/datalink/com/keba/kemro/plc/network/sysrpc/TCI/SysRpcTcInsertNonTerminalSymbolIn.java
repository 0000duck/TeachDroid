package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcInsertNonTerminalSymbolIn implements XDR {
	public int editHnd;
	public SysRpcTcPos pos;
	public int kind;

	public SysRpcTcInsertNonTerminalSymbolIn () {
		pos = new SysRpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
		pos.write(out);
		out.writeInt(kind);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
		pos.read(in);
		kind = in.readInt();
	}
}