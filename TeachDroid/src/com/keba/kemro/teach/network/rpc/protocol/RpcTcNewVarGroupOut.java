package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcNewVarGroupOut implements XDR {
	public int varGroupHnd;
	public boolean retVal;

	public RpcTcNewVarGroupOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varGroupHnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varGroupHnd = in.readInt();
		retVal = in.readBool();
	}
}