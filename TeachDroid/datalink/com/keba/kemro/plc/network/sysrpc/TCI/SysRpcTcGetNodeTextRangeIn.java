package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetNodeTextRangeIn implements XDR {
	public int editHnd;
	public int nodeHnd;

	public SysRpcTcGetNodeTextRangeIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
		out.writeInt(nodeHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
		nodeHnd = in.readInt();
	}
}