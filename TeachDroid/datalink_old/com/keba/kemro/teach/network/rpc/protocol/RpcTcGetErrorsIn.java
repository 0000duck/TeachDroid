package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetErrorsIn implements XDR {
	public int prjHandle;

	public RpcTcGetErrorsIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(prjHandle);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		prjHandle = in.readInt();
	}
}