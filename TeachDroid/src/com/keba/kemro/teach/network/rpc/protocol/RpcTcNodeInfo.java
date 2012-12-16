package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcNodeInfo implements XDR {
	public RpcTcNodeKind kind;
	public String elemName;
	public int incCnt;
	public int attr;
	public int upperHnd;
	public int declHnd;

	public RpcTcNodeInfo () {
		kind = new RpcTcNodeKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		kind.write(out);
		out.writeString(elemName);
		out.writeInt(incCnt);
		out.writeInt(attr);
		out.writeInt(upperHnd);
		out.writeInt(declHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		kind.read(in);
		elemName = in.readString();
		incCnt = in.readInt();
		attr = in.readInt();
		upperHnd = in.readInt();
		declHnd = in.readInt();
	}
}