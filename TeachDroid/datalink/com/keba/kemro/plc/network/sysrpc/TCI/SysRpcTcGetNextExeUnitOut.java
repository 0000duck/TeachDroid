package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetNextExeUnitOut implements XDR {
	public int iterHnd;
	public int exeUnitHnd;
	public boolean retVal;

	public SysRpcTcGetNextExeUnitOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(iterHnd);
		out.writeInt(exeUnitHnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		iterHnd = in.readInt();
		exeUnitHnd = in.readInt();
		retVal = in.readBool();
	}
}