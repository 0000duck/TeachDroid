package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcReplaceStatementIn implements XDR {
	public int routineHnd;
	public SysRpcTcPos begPos;
	public SysRpcTcPos endPos;
	public String stmtText;

	public SysRpcTcReplaceStatementIn () {
		begPos = new SysRpcTcPos();
		endPos = new SysRpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineHnd);
		begPos.write(out);
		endPos.write(out);
		out.writeString(stmtText);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineHnd = in.readInt();
		begPos.read(in);
		endPos.read(in);
		stmtText = in.readString();
	}
}