package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcMethodParam implements XDR {
	public RpcTcVarAccess varAccess;
	public RpcTcValue value;
	public boolean isValue;

	public RpcTcMethodParam () {
		varAccess = new RpcTcVarAccess();
		value = new RpcTcValue();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		varAccess.write(out);
		value.write(out);
		out.writeBool(isValue);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccess.read(in);
		value.read(in);
		isValue = in.readBool();
	}
}