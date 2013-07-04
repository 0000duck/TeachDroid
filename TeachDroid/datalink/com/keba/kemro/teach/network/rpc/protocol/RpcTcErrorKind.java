package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcErrorKind implements XDR {
	public static final int rpcInfo = 1;
	public static final int rpcWarning = 2;
	public static final int rpcError = 3;
	public static final int rpcErrorInfo = 4;
	public int value;



	public RpcTcErrorKind () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}