package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetNextExeUnitOut implements XDR {
	public int iterHnd;
	public int exeUnitHnd;
	public boolean retVal;

	public RpcTcGetNextExeUnitOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(iterHnd);
		out.writeInt(exeUnitHnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		iterHnd = in.readInt();
		exeUnitHnd = in.readInt();
		retVal = in.readBool();
	}
}