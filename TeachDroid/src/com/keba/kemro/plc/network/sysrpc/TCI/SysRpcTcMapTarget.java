package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcMapTarget implements XDR {
	public SysRpcTcMapKind kind;
	public int exeUnit;
	public SysRpcTcInstancePath instancePath;
	public int routineHdl;
	public String external;

	public SysRpcTcMapTarget () {
		kind = new SysRpcTcMapKind();
		instancePath = new SysRpcTcInstancePath();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		kind.write(out);
		out.writeInt(exeUnit);
		instancePath.write(out);
		out.writeInt(routineHdl);
		out.writeString(external);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		kind.read(in);
		exeUnit = in.readInt();
		instancePath.read(in);
		routineHdl = in.readInt();
		external = in.readString();
	}
}