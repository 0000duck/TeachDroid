package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcVarAttr implements XDR {
	public static final int rpcVarAttrIsOptional = 1;
	public static final int rpcVarAttrIsReferenced = 2;
	public static final int rpcVarAttrIsReadonly = 4;
	public static final int rpcVarAttrIsNoInit = 8;
	public static final int rpcVarAttrIsDynamic = 16;
	public int value;



	public SysRpcTcVarAttr () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}