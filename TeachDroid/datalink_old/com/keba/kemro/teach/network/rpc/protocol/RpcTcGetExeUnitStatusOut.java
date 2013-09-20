package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetExeUnitStatusOut implements XDR {
	public RpcTcExeUnitStatus status;
	public boolean retVal;

	public RpcTcGetExeUnitStatusOut () {
		status = new RpcTcExeUnitStatus();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		status.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		status.read(in);
		retVal = in.readBool();
	}
}