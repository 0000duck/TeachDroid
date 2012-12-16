package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcExeUnitStatus implements XDR {
	public RpcTcExeUnitState state;
	public RpcTcExeStepState step;
	public RpcTcExeFlags exeFlags;
	public int lineOrStatus;
	public int mainFlowLine;
	public int nrChilds;
	public int changeCnt;

	public RpcTcExeUnitStatus () {
		state = new RpcTcExeUnitState();
		step = new RpcTcExeStepState();
		exeFlags = new RpcTcExeFlags();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		state.write(out);
		step.write(out);
		exeFlags.write(out);
		out.writeInt(lineOrStatus);
		out.writeInt(mainFlowLine);
		out.writeInt(nrChilds);
		out.writeInt(changeCnt);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		state.read(in);
		step.read(in);
		exeFlags.read(in);
		lineOrStatus = in.readInt();
		mainFlowLine = in.readInt();
		nrChilds = in.readInt();
		changeCnt = in.readInt();
	}
}