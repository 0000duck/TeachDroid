package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcWriteAccessRequestPendingOut implements XDR {
	public boolean requestPending;
	public boolean retVal;

	public SysRpcTcWriteAccessRequestPendingOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeBool(requestPending);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		requestPending = in.readBool();
		retVal = in.readBool();
	}
}