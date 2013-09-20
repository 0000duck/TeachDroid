package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcCloseTeachControlIn implements XDR {
	public int closeClientHnd;

	public SysRpcTcCloseTeachControlIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(closeClientHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		closeClientHnd = in.readInt();
	}
}