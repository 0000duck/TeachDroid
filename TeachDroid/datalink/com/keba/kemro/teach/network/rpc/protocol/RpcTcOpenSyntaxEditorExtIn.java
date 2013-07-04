package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcOpenSyntaxEditorExtIn implements XDR {
	public int scopeHnd;
	public String passWord;
	public RpcTcPart part;

	public RpcTcOpenSyntaxEditorExtIn () {
		part = new RpcTcPart();
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