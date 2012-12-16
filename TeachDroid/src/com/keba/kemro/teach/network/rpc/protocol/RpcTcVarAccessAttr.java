package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcVarAccessAttr implements XDR {
	public static final int rpcVarAccIsUser = 1;
	public static final int rpcVarAccIsPrivate = 2;
	public static final int rpcVarAccIsReadonly = 4;
	public static final int rpcVarAccIsConst = 8;
	public int value;



	public RpcTcVarAccessAttr () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}