package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcInsertStatementIn implements XDR {
	public int routineHnd;
	public SysRpcTcPos pos;
	public String stmtText;

	public SysRpcTcInsertStatementIn () {
		pos = new SysRpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineHnd);
		pos.write(out);
		out.writeString(stmtText);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineHnd = in.readInt();
		pos.read(in);
		stmtText = in.readString();
	}
}