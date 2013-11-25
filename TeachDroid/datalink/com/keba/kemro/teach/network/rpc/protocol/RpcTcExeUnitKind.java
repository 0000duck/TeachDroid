package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcExeUnitKind implements XDR {
	public static final int rpcInvalidExeUnit = -1;
	public static final int rpcGlobalExeUnit = 0;
	public static final int rpcProjectExeUnit = 1;
	public static final int rpcProgramExeUnit = 2;
	public static final int rpcRoutineExeUnit = 3;
	public static final int rpcFilterAllExeUnit = 4;
	public static final int rpcFilterUserProgramExeUnit = 5;
	public static final int rpcFilterUserRoutineExeUnit = 6;
	public static final int rpcFilterAllUserExeUnit = 7;
	public static final int rpcSystemExeUnit = 8;
	public int value;



	public RpcTcExeUnitKind () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}