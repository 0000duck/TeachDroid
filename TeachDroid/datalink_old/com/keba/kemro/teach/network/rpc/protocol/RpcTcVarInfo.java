package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcVarInfo implements XDR {
	public int incCnt;
	public int dataSize;
	public int typeHnd;
	public RpcTcVarKind kind;
	public int attr;
	public boolean isPrivate;
	public boolean isSave;
	public boolean isProjectSave;
	public int variantConstHnd;

	public RpcTcVarInfo () {
		kind = new RpcTcVarKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(incCnt);
		out.writeInt(dataSize);
		out.writeInt(typeHnd);
		kind.write(out);
		out.writeInt(attr);
		out.writeBool(isPrivate);
		out.writeBool(isSave);
		out.writeBool(isProjectSave);
		out.writeInt(variantConstHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		incCnt = in.readInt();
		dataSize = in.readInt();
		typeHnd = in.readInt();
		kind.read(in);
		attr = in.readInt();
		isPrivate = in.readBool();
		isSave = in.readBool();
		isProjectSave = in.readBool();
		variantConstHnd = in.readInt();
	}
}