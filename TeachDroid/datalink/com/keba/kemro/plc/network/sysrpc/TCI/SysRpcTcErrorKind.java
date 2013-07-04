package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcErrorKind implements XDR {
	public static final int rpcInfo = 1;
	public static final int rpcWarning = 2;
	public static final int rpcError = 3;
	public static final int rpcErrorInfo = 4;
	public int value;



	public SysRpcTcErrorKind () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}