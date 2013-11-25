package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcMoveStatementIn implements XDR {
	public int editHnd;
	public SysRpcTcPos pos;
	public SysRpcTcSymbolKind nonTerm;
	public SysRpcTcPos toPos;

	public SysRpcTcMoveStatementIn () {
		pos = new SysRpcTcPos();
		nonTerm = new SysRpcTcSymbolKind();
		toPos = new SysRpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
		pos.write(out);
		nonTerm.write(out);
		toPos.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
		pos.read(in);
		nonTerm.read(in);
		toPos.read(in);
	}
}