package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcOpenSyntaxEditorExtIn implements XDR {
	public int scopeHnd;
	public String passWord;
	public SysRpcTcPart part;

	public SysRpcTcOpenSyntaxEditorExtIn () {
		part = new SysRpcTcPart();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(scopeHnd);
		out.writeString(passWord);
		part.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		scopeHnd = in.readInt();
		passWord = in.readString();
		part.read(in);
	}
}