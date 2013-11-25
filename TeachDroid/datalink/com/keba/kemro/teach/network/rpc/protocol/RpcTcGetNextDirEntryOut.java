package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetNextDirEntryOut implements XDR {
	public int iterHnd;
	public String dirEntryPath;
	public boolean retVal;

	public RpcTcGetNextDirEntryOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(iterHnd);
		out.writeString(dirEntryPath);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		iterHnd = in.readInt();
		dirEntryPath = in.readString();
		retVal = in.readBool();
	}
}