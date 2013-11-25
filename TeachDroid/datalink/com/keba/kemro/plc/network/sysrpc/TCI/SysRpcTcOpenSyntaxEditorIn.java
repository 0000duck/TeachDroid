package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcOpenSyntaxEditorIn implements XDR {
	public int scopeHnd;
	public SysRpcTcPart part;

	public SysRpcTcOpenSyntaxEditorIn () {
		part = new SysRpcTcPart();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(scopeHnd);
		part.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		scopeHnd = in.readInt();
		part.read(in);
	}
}