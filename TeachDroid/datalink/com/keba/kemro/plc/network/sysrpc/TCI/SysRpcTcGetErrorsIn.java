package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetErrorsIn implements XDR {
	public int prjHandle;

	public SysRpcTcGetErrorsIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(prjHandle);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		prjHandle = in.readInt();
	}
}