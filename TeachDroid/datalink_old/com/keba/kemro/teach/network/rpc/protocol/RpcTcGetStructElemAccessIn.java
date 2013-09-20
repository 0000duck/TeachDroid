package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetStructElemAccessIn implements XDR {
	public RpcTcVarAccess varAccess;
	public int varHnd;

	public RpcTcGetStructElemAccessIn () {
		varAccess = new RpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		varAccess.write(out);
		out.writeInt(varHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccess.read(in);
		varHnd = in.readInt();
	}
}