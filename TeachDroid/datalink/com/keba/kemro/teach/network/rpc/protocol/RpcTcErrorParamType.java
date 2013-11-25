package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcErrorParamType implements XDR {
	public static final int rpcIntParam = 0;
	public static final int rpcFloatParam = 1;
	public static final int rpcStringParam = 2;
	public int value;



	public RpcTcErrorParamType () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}