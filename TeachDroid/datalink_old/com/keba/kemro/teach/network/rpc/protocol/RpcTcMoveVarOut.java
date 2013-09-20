package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcMoveVarOut implements XDR {
	public int newVarHnd;
	public boolean retVal;

	public RpcTcMoveVarOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(newVarHnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		newVarHnd = in.readInt();
		retVal = in.readBool();
	}
}