package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetFirstLineIn implements XDR {
	public int editHnd;
	public int lineNr;

	public RpcTcGetFirstLineIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
		out.writeInt(lineNr);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
		lineNr = in.readInt();
	}
}