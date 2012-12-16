package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetEditorInfoOut implements XDR {
	public RpcTcEditInfo info;
	public boolean retVal;

	public RpcTcGetEditorInfoOut () {
		info = new RpcTcEditInfo();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		info.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		info.read(in);
		retVal = in.readBool();
	}
}