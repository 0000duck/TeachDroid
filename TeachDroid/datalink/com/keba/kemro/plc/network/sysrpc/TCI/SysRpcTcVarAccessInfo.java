package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcVarAccessInfo implements XDR {
	public SysRpcTcTypeKind typeKind;
	public int attr;
	public int reserved;

	public SysRpcTcVarAccessInfo () {
		typeKind = new SysRpcTcTypeKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		typeKind.write(out);
		out.writeInt(attr);
		out.writeInt(reserved);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		typeKind.read(in);
		attr = in.readInt();
		reserved = in.readInt();
	}
}