package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcCopySaveValueIn implements XDR {
	public RpcTcVarAccess destVarAccess;
	public RpcTcVarAccess srcVarAccess;

	public RpcTcCopySaveValueIn () {
		destVarAccess = new RpcTcVarAccess();
		srcVarAccess = new RpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		destVarAccess.write(out);
		srcVarAccess.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		destVarAccess.read(in);
		srcVarAccess.read(in);
	}
}