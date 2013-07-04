package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetNextSymbolOut implements XDR {
	public int iterHnd;
	public SysRpcTcSymbol symbol;
	public boolean retVal;

	public SysRpcTcGetNextSymbolOut () {
		symbol = new SysRpcTcSymbol();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(iterHnd);
		symbol.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		iterHnd = in.readInt();
		symbol.read(in);
		retVal = in.readBool();
	}
}