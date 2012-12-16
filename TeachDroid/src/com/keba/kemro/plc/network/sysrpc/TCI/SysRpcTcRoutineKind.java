package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcRoutineKind implements XDR {
	public static final int rpcUnnamedRoutine = 0;
	public static final int rpcNamedRoutine = 1;
	public static final int rpcAtRoutine = 2;
	public int value;



	public SysRpcTcRoutineKind () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}