package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcMapTarget implements XDR {
	public RpcTcMapKind kind;
	public int exeUnit;
	public RpcTcInstancePath instancePath;
	public int routineHdl;
	public String external;

	public RpcTcMapTarget () {
		kind = new RpcTcMapKind();
		instancePath = new RpcTcInstancePath();
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