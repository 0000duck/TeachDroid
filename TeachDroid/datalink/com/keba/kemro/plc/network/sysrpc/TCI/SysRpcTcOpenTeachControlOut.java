package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcOpenTeachControlOut implements XDR {
	public int clientHnd;
	public String tcVersion;
	public boolean retVal;

	public SysRpcTcOpenTeachControlOut () {
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