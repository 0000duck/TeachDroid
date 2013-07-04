package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcConstInfo implements XDR {
	public int typeHnd;
	public RpcTcValue value;

	public RpcTcConstInfo () {
		value = new RpcTcValue();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(typeHnd);
		value.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		typeHnd = in.readInt();
		value.read(in);
	}
}