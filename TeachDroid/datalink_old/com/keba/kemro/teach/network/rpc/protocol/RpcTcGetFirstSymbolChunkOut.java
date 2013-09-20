package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetFirstSymbolChunkOut implements XDR {
	public int iterHnd;
	public int nrOfSymbols;
	public RpcTcSymbol[] symbols;  //variable length with max length of rpcChunkLen
	public int symbols_count; //countains the number of elements
	public boolean retVal;

	public RpcTcGetFirstSymbolChunkOut () {
		symbols = new RpcTcSymbol[rpcChunkLen.value];
		for (int for_i = 0; for_i < rpcChunkLen.value; for_i++) {
			symbols[for_i] = new RpcTcSymbol();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(iterHnd);
		out.writeInt(nrOfSymbols);
		out.writeInt(symbols_count);
		for (int for_i = 0; for_i < symbols_count; for_i++) {
			symbols[for_i].write(out);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		iterHnd = in.readInt();
		nrOfSymbols = in.readInt();
		symbols_count = in.readInt();
		for (int for_i = 0; for_i < symbols_count; for_i++) {
			symbols[for_i].read(in);
		}
		retVal = in.readBool();
	}
}