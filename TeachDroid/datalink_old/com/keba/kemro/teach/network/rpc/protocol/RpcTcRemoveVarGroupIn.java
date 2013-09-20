package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcRemoveVarGroupIn implements XDR {
	public int varGroupHnd;

	public RpcTcRemoveVarGroupIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varGroupHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varGroupHnd = in.readInt();
	}
}