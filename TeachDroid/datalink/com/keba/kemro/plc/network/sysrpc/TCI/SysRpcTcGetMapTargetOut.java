package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetMapTargetOut implements XDR {
	public SysRpcTcMapTarget target;
	public boolean retVal;

	public SysRpcTcGetMapTargetOut () {
		target = new SysRpcTcMapTarget();
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