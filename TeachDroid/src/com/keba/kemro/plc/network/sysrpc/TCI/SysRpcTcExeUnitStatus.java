package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcExeUnitStatus implements XDR {
	public SysRpcTcExeUnitState state;
	public SysRpcTcExeStepState step;
	public SysRpcTcExeFlags exeFlags;
	public int lineOrStatus;
	public int mainFlowLine;
	public int nrChilds;
	public int changeCnt;

	public SysRpcTcExeUnitStatus () {
		state = new SysRpcTcExeUnitState();
		step = new SysRpcTcExeStepState();
		exeFlags = new SysRpcTcExeFlags();
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