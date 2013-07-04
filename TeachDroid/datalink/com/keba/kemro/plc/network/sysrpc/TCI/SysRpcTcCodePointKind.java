package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcCodePointKind implements XDR {
	public static final int rpcBreakPoint = 0;
	public static final int rpcWatchPoint = 1;
	public static final int rpcBreakPointMain = 2;
	public int value;



	public SysRpcTcCodePointKind () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}