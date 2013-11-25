package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetExeUnitInfoOut implements XDR {
	public SysRpcTcExeUnitInfo info;
	public boolean retVal;

	public SysRpcTcGetExeUnitInfoOut () {
		info = new SysRpcTcExeUnitInfo();
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