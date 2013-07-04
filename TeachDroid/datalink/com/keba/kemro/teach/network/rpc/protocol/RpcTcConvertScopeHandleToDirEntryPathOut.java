package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcConvertScopeHandleToDirEntryPathOut implements XDR {
	public String dirEntryPath;
	public boolean retVal;

	public RpcTcConvertScopeHandleToDirEntryPathOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(dirEntryPath);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		dirEntryPath = in.readString();
		retVal = in.readBool();
	}
}