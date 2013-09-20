package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcExeUnitState implements XDR {
	public static final int rpcInvalid = -1;
	public static final int rpcWaiting = 0;
	public static final int rpcRunning = 1;
	public static final int rpcStepping = 2;
	public static final int rpcDefunct = 3;
	public static final int rpcInterrupted = 4;
	public int value;



	public SysRpcTcExeUnitState () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}