package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcReadNodeChangeIn implements XDR {
	public int startChangeCnt;

	public RpcTcReadNodeChangeIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(startChangeCnt);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		startChangeCnt = in.readInt();
	}
}