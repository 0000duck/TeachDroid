package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcVarAccessInfo implements XDR {
	public RpcTcTypeKind typeKind;
	public int attr;
	public int reserved;

	public RpcTcVarAccessInfo () {
		typeKind = new RpcTcTypeKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		typeKind.write(out);
		out.writeInt(attr);
		out.writeInt(reserved);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		typeKind.read(in);
		attr = in.readInt();
		reserved = in.readInt();
	}
}