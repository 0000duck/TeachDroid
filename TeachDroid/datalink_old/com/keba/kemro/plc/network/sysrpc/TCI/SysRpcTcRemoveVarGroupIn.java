package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcRemoveVarGroupIn implements XDR {
	public int varGroupHnd;

	public SysRpcTcRemoveVarGroupIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varGroupHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varGroupHnd = in.readInt();
	}
}