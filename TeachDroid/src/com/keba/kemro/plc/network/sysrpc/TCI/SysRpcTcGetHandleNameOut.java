package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetHandleNameOut implements XDR {
	public String name;
	public boolean retVal;

	public SysRpcTcGetHandleNameOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(name);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		name = in.readString();
		retVal = in.readBool();
	}
}