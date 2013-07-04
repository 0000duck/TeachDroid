package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcReadVarGroupValuesIn implements XDR {
	public int varGroupHnd;
	public int startIdx;

	public SysRpcTcReadVarGroupValuesIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varGroupHnd);
		out.writeInt(startIdx);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varGroupHnd = in.readInt();
		startIdx = in.readInt();
	}
}