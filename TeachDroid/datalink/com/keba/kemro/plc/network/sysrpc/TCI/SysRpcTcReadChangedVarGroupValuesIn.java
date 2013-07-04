package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcReadChangedVarGroupValuesIn implements XDR {
	public int varGroupHnd;
	public int startIdx;

	public SysRpcTcReadChangedVarGroupValuesIn () {
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