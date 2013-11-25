package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetSymbolTextOut implements XDR {
	public String text;
	public boolean retVal;

	public SysRpcTcGetSymbolTextOut () {
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