package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetStructElemAccessIn implements XDR {
	public SysRpcTcVarAccess varAccess;
	public int varHnd;

	public SysRpcTcGetStructElemAccessIn () {
		varAccess = new SysRpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		varAccess.write(out);
		out.writeInt(varHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccess.read(in);
		varHnd = in.readInt();
	}
}