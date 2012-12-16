package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetArrayElemAccessIn implements XDR {
	public SysRpcTcVarAccess varAccess;
	public int index;

	public SysRpcTcGetArrayElemAccessIn () {
		varAccess = new SysRpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		varAccess.write(out);
		out.writeInt(index);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccess.read(in);
		index = in.readInt();
	}
}