package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcStepListIn implements XDR {
	public int nrOfExeUnitHnd;
	public int[] exeUnitHnd;  //variable length with max length of TCI.rpcChunkLen
	public int exeUnitHnd_count; //countains the number of elements
	public SysRpcTcExeStepState stepCmd;

	public SysRpcTcStepListIn () {
		exeUnitHnd = new int[TCI.rpcChunkLen];
		stepCmd = new SysRpcTcExeStepState();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(nrOfExeUnitHnd);
		out.writeInt(exeUnitHnd_count);
		for (int for_i = 0; for_i < exeUnitHnd_count; for_i++) {
			out.writeInt(exeUnitHnd[for_i]);
		}
		stepCmd.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nrOfExeUnitHnd = in.readInt();
		exeUnitHnd_count = in.readInt();
		for (int for_i = 0; for_i < exeUnitHnd_count; for_i++) {
			exeUnitHnd[for_i] = in.readInt();
		}
		stepCmd.read(in);
	}
}