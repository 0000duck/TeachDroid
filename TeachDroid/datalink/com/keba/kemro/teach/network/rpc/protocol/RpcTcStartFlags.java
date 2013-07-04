package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcStartFlags implements XDR {
	public static final int rpcStartFlagEmpty = 0;
	public static final int rpcStartFlagInterrupt = 1;
	public static final int rpcStartFlagRestart = 2;
	public int value;



	public RpcTcStartFlags () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}