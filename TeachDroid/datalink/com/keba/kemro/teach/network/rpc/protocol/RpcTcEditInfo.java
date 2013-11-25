package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcEditInfo implements XDR {
	public int noLines;
	public int noChars;

	public RpcTcEditInfo () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(noLines);
		out.writeInt(noChars);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		noLines = in.readInt();
		noChars = in.readInt();
	}
}