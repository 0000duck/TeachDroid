package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetHandleNameIn implements XDR {
	public int hnd;

	public SysRpcTcGetHandleNameIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(hnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		hnd = in.readInt();
	}
}