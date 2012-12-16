package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcNodeAttr implements XDR {
	public static final int rpcUserNodeAttr = 1;
	public static final int rpcHasAttributesAttr = 2;
	public static final int rpcIsReferencedAttr = 4;
	public static final int rpcIsAbstractAttr = 8;
	public static final int rpcIsDeprecatedAttr = 16;
	public static final int rpcIsExportVarAttr = 32;
	public static final int rpcNormalProgAttr = 64;
	public int value;



	public RpcTcNodeAttr () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}