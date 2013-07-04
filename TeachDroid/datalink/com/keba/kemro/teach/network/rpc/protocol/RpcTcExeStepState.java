package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcExeStepState implements XDR {
	public static final int rpcStepOff = 0;
	public static final int rpcStepBreak = 1;
	public static final int rpcStepInto = 2;
	public static final int rpcStepOver = 3;
	public static final int rpcStepOut = 4;
	public static final int rpcStepWait = 5;
	public static final int rpcStepGo = 6;
	public int value;



	public RpcTcExeStepState () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}