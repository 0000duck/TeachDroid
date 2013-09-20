package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcNodeInfo implements XDR {
	public SysRpcTcNodeKind kind;
	public String elemName;
	public int incCnt;
	public int attr;
	public int upperHnd;
	public int declHnd;

	public SysRpcTcNodeInfo () {
		kind = new SysRpcTcNodeKind();
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