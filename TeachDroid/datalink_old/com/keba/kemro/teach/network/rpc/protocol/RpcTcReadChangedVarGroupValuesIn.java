package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcReadChangedVarGroupValuesIn implements XDR {
	public int varGroupHnd;
	public int startIdx;

	public RpcTcReadChangedVarGroupValuesIn () {
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