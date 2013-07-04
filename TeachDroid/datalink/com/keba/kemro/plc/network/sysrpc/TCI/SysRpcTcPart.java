package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcPart implements XDR {
	public static final int rpcVarPart = 0;
	public static final int rpcTypPart = 1;
	public static final int rpcAllPart = 2;
	public int value;



	public SysRpcTcPart () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}