package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcCopyActValueIn implements XDR {
	public int destExeUnitHnd;
	public SysRpcTcVarAccess destVarAccess;
	public int srcExeUnitHnd;
	public SysRpcTcVarAccess srcVarAccess;

	public SysRpcTcCopyActValueIn () {
		destVarAccess = new SysRpcTcVarAccess();
		srcVarAccess = new SysRpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(destExeUnitHnd);
		destVarAccess.write(out);
		out.writeInt(srcExeUnitHnd);
		srcVarAccess.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		destExeUnitHnd = in.readInt();
		destVarAccess.read(in);
		srcExeUnitHnd = in.readInt();
		srcVarAccess.read(in);
	}
}