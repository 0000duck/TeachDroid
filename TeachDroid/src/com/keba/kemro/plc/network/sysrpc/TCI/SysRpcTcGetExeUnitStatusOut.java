package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetExeUnitStatusOut implements XDR {
	public SysRpcTcExeUnitStatus status;
	public boolean retVal;

	public SysRpcTcGetExeUnitStatusOut () {
		status = new SysRpcTcExeUnitStatus();
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