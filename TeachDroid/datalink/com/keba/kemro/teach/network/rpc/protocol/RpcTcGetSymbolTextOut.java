package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetSymbolTextOut implements XDR {
	public String text;
	public boolean retVal;

	public RpcTcGetSymbolTextOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(text);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		text = in.readString();
		retVal = in.readBool();
	}
}