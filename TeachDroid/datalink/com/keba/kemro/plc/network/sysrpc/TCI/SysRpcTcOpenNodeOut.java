package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcOpenNodeOut implements XDR {
	public int nodeHnd;
	public SysRpcTcNodeKind kind;
	public boolean retVal;

	public SysRpcTcOpenNodeOut () {
		kind = new SysRpcTcNodeKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(nodeHnd);
		kind.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nodeHnd = in.readInt();
		kind.read(in);
		retVal = in.readBool();
	}
}