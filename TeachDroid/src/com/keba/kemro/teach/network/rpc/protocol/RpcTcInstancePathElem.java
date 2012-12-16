package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcInstancePathElem implements XDR {
	public int structComponent;
	public int arrayIndex;

	public RpcTcInstancePathElem () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(structComponent);
		out.writeInt(arrayIndex);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		structComponent = in.readInt();
		arrayIndex = in.readInt();
	}
}