package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcMethodParam implements XDR {
	public SysRpcTcVarAccess varAccess;
	public SysRpcTcValue value;
	public boolean isValue;

	public SysRpcTcMethodParam () {
		varAccess = new SysRpcTcVarAccess();
		value = new SysRpcTcValue();
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