package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetFirstSymbolChunkOut implements XDR {
	public int iterHnd;
	public int nrOfSymbols;
	public SysRpcTcSymbol[] symbols;  //variable length with max length of TCI.rpcChunkLen
	public int symbols_count; //countains the number of elements
	public boolean retVal;

	public SysRpcTcGetFirstSymbolChunkOut () {
		symbols = new SysRpcTcSymbol[TCI.rpcChunkLen];
		for (int for_i = 0; for_i < TCI.rpcChunkLen; for_i++) {
			symbols[for_i] = new SysRpcTcSymbol();
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