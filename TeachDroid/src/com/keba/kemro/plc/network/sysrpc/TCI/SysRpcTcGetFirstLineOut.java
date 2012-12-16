package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetFirstLineOut implements XDR {
	public int iterHnd;
	public String line;
	public boolean retVal;

	public SysRpcTcGetFirstLineOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(iterHnd);
		out.writeString(line);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		iterHnd = in.readInt();
		line = in.readString();
		retVal = in.readBool();
	}
}