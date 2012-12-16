package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetWatchPointCounterOut implements XDR {
	public int counter;
	public boolean retVal;

	public SysRpcTcGetWatchPointCounterOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(counter);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		counter = in.readInt();
		retVal = in.readBool();
	}
}