package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcReadErrorOut implements XDR {
	public RpcTcErrorElem error;
	public boolean retVal;

	public RpcTcReadErrorOut () {
		error = new RpcTcErrorElem();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		error.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		error.read(in);
		retVal = in.readBool();
	}
}