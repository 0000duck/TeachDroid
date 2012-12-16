package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcNewVarGroupOut implements XDR {
	public int varGroupHnd;
	public boolean retVal;

	public SysRpcTcNewVarGroupOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varGroupHnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varGroupHnd = in.readInt();
		retVal = in.readBool();
	}
}