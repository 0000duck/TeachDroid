package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcStepIn implements XDR {
	public int exeUnitHnd;
	public RpcTcExeStepState stepCmd;

	public RpcTcStepIn () {
		stepCmd = new RpcTcExeStepState();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		stepCmd.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		stepCmd.read(in);
	}
}