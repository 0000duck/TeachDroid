package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcReadErrorOut implements XDR {
	public SysRpcTcErrorElem error;
	public boolean retVal;

	public SysRpcTcReadErrorOut () {
		error = new SysRpcTcErrorElem();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		error.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		error.read(in);
		retVal = in.readBool();
	}
}