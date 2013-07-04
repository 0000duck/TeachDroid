package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetSaveValueOut implements XDR {
	public SysRpcTcValue value;
	public boolean retVal;

	public SysRpcTcGetSaveValueOut () {
		value = new SysRpcTcValue();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		value.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		value.read(in);
		retVal = in.readBool();
	}
}