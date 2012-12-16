package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcSetInstructionPointerIn implements XDR {
	public int exeUnitHnd;
	public int lineNr;

	public RpcTcSetInstructionPointerIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		out.writeInt(lineNr);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		lineNr = in.readInt();
	}
}