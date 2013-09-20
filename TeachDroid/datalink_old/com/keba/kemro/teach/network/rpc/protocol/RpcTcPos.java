package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcPos implements XDR {
	public int line;
	public int col;

	public RpcTcPos () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(line);
		out.writeInt(col);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		line = in.readInt();
		col = in.readInt();
	}
}