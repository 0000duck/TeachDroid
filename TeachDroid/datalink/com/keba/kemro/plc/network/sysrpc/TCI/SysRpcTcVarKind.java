package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcVarKind implements XDR {
	public static final int rpcParam = 0;
	public static final int rpcVar = 1;
	public static final int rpcConstParam = 2;
	public static final int rpcValueParam = 3;
	public static final int rpcVarConst = 4;
	public int value;



	public SysRpcTcVarKind () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}