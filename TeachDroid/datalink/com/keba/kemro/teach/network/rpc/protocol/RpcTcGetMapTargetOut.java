package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetMapTargetOut implements XDR {
	public RpcTcMapTarget target;
	public boolean retVal;

	public RpcTcGetMapTargetOut () {
		target = new RpcTcMapTarget();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		target.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		target.read(in);
		retVal = in.readBool();
	}
}