package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcCopyActValueIn implements XDR {
	public int destExeUnitHnd;
	public RpcTcVarAccess destVarAccess;
	public int srcExeUnitHnd;
	public RpcTcVarAccess srcVarAccess;

	public RpcTcCopyActValueIn () {
		destVarAccess = new RpcTcVarAccess();
		srcVarAccess = new RpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(destExeUnitHnd);
		destVarAccess.write(out);
		out.writeInt(srcExeUnitHnd);
		srcVarAccess.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		destExeUnitHnd = in.readInt();
		destVarAccess.read(in);
		srcExeUnitHnd = in.readInt();
		srcVarAccess.read(in);
	}
}