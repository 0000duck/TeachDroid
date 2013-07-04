package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetNextSymbolOut implements XDR {
	public int iterHnd;
	public RpcTcSymbol symbol;
	public boolean retVal;

	public RpcTcGetNextSymbolOut () {
		symbol = new RpcTcSymbol();
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