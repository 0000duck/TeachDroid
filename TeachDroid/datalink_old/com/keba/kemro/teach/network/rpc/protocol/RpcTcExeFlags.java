package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcExeFlags implements XDR {
	public static final int rpcExeFlagFlow = 1;
	public static final int rpcExeFlagRoutine = 2;
	public static final int rpcExeFlagMainFlowStepping = 65536;
	public int value;



	public RpcTcExeFlags () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}