package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcOpenTeachControlOut implements XDR {
	public int clientHnd;
	public String tcVersion;
	public boolean retVal;

	public RpcTcOpenTeachControlOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(clientHnd);
		out.writeString(tcVersion);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		clientHnd = in.readInt();
		tcVersion = in.readString();
		retVal = in.readBool();
	}
}