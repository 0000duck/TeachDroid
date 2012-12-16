package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetSaveValueIn implements XDR {
	public SysRpcTcVarAccess varAccess;

	public SysRpcTcGetSaveValueIn () {
		varAccess = new SysRpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		varAccess.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccess.read(in);
	}
}