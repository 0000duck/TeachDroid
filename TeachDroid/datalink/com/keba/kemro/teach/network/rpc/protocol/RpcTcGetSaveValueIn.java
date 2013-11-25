package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetSaveValueIn implements XDR {
	public RpcTcVarAccess varAccess;

	public RpcTcGetSaveValueIn () {
		varAccess = new RpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		varAccess.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccess.read(in);
	}
}