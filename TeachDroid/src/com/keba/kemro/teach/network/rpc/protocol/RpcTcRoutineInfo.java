package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcRoutineInfo implements XDR {
	public int incCnt;
	public int dataSize;
	public int codeSize;
	public int nrOfStmts;
	public int retTypeHnd;
	public int nrFormalPar;
	public RpcTcRoutineKind kind;
	public int eventVarHnd;
	public boolean isPrivate;

	public RpcTcRoutineInfo () {
		kind = new RpcTcRoutineKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(incCnt);
		out.writeInt(dataSize);
		out.writeInt(codeSize);
		out.writeInt(nrOfStmts);
		out.writeInt(retTypeHnd);
		out.writeInt(nrFormalPar);
		kind.write(out);
		out.writeInt(eventVarHnd);
		out.writeBool(isPrivate);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		incCnt = in.readInt();
		dataSize = in.readInt();
		codeSize = in.readInt();
		nrOfStmts = in.readInt();
		retTypeHnd = in.readInt();
		nrFormalPar = in.readInt();
		kind.read(in);
		eventVarHnd = in.readInt();
		isPrivate = in.readBool();
	}
}