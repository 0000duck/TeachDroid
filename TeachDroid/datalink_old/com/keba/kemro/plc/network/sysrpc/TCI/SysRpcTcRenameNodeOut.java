package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcRenameNodeOut implements XDR {
	public boolean retVal;

	public SysRpcTcRenameNodeOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		retVal = in.readBool();
	}
}