package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetRoutineInfoOut implements XDR {
	public RpcTcRoutineInfo info;
	public boolean retVal;

	public RpcTcGetRoutineInfoOut () {
		info = new RpcTcRoutineInfo();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		info.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		info.read(in);
		retVal = in.readBool();
	}
}