package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcWriteInitValueIn implements XDR {
	public int varHnd;

	public SysRpcTcWriteInitValueIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varHnd = in.readInt();
	}
}