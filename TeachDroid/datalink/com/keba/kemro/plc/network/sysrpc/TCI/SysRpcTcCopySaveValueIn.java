package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcCopySaveValueIn implements XDR {
	public SysRpcTcVarAccess destVarAccess;
	public SysRpcTcVarAccess srcVarAccess;

	public SysRpcTcCopySaveValueIn () {
		destVarAccess = new SysRpcTcVarAccess();
		srcVarAccess = new SysRpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		destVarAccess.write(out);
		srcVarAccess.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		destVarAccess.read(in);
		srcVarAccess.read(in);
	}
}