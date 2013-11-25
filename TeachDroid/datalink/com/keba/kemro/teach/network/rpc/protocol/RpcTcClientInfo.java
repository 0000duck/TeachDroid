package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcClientInfo implements XDR {
	public int hnd;
	public RpcTcClientType type;
	public String name;

	public RpcTcClientInfo () {
		type = new RpcTcClientType();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(hnd);
		type.write(out);
		out.writeString(name);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		hnd = in.readInt();
		type.read(in);
		name = in.readString();
	}
}