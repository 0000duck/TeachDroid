package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcNodeChange implements XDR {
	public int changeCnt;
	public int hdl;

	public RpcTcNodeChange () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(changeCnt);
		out.writeInt(hdl);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		changeCnt = in.readInt();
		hdl = in.readInt();
	}
}