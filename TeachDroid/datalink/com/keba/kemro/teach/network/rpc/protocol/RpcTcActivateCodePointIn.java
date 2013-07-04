package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcActivateCodePointIn implements XDR {
	public int routineScopeHnd;
	public int lineNr;
	public boolean active;

	public RpcTcActivateCodePointIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineScopeHnd);
		out.writeInt(lineNr);
		out.writeBool(active);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineScopeHnd = in.readInt();
		lineNr = in.readInt();
		active = in.readBool();
	}
}