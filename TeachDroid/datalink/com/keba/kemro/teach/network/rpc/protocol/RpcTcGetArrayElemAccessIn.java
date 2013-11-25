package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetArrayElemAccessIn implements XDR {
	public RpcTcVarAccess varAccess;
	public int index;

	public RpcTcGetArrayElemAccessIn () {
		varAccess = new RpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		varAccess.write(out);
		out.writeInt(index);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccess.read(in);
		index = in.readInt();
	}
}