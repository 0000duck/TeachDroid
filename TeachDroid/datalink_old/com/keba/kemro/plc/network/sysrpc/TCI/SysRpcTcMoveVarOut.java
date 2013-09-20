package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcMoveVarOut implements XDR {
	public int newVarHnd;
	public boolean retVal;

	public SysRpcTcMoveVarOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(newVarHnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		newVarHnd = in.readInt();
		retVal = in.readBool();
	}
}