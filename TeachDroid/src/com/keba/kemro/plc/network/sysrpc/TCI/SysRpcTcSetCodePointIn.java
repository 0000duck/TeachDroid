package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcSetCodePointIn implements XDR {
	public int routineScopeHnd;
	public int lineNr;
	public int exeUnitHnd;
	public SysRpcTcInstancePath instancePath;
	public SysRpcTcCodePointKind kind;

	public SysRpcTcSetCodePointIn () {
		instancePath = new SysRpcTcInstancePath();
		kind = new SysRpcTcCodePointKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineScopeHnd);
		out.writeInt(lineNr);
		out.writeInt(exeUnitHnd);
		instancePath.write(out);
		kind.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineScopeHnd = in.readInt();
		lineNr = in.readInt();
		exeUnitHnd = in.readInt();
		instancePath.read(in);
		kind.read(in);
	}
}