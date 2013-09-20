package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetMemDumpIn implements XDR {
	public int exeUnitHnd;
	public SysRpcTcVarAccess varAccess;
	public int offset;
	public int bufferSize;

	public SysRpcTcGetMemDumpIn () {
		varAccess = new SysRpcTcVarAccess();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		varAccess.write(out);
		out.writeInt(offset);
		out.writeInt(bufferSize);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		varAccess.read(in);
		offset = in.readInt();
		bufferSize = in.readInt();
	}
}