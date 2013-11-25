package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcRequestWriteAccessIn implements XDR {
	public boolean request;
	public boolean retVal;

	public SysRpcTcRequestWriteAccessIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeBool(request);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		request = in.readBool();
		retVal = in.readBool();
	}
}