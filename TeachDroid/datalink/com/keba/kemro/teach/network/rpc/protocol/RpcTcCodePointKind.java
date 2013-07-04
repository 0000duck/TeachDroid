package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcCodePointKind implements XDR {
	public static final int rpcBreakPoint = 0;
	public static final int rpcWatchPoint = 1;
	public static final int rpcBreakPointMain = 2;
	public int value;



	public RpcTcCodePointKind () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}