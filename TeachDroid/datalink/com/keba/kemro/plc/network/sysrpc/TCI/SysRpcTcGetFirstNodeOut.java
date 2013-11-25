package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetFirstNodeOut implements XDR {
	public int iterHnd;
	public int nodeHnd;
	public boolean retVal;

	public SysRpcTcGetFirstNodeOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(iterHnd);
		out.writeInt(nodeHnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		iterHnd = in.readInt();
		nodeHnd = in.readInt();
		retVal = in.readBool();
	}
}