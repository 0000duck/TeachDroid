package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcNodeKind implements XDR {
	public static final int rpcRootNode = 0;
	public static final int rpcGlobalNode = 1;
	public static final int rpcProjectNode = 2;
	public static final int rpcProgramNode = 3;
	public static final int rpcTypeNode = 4;
	public static final int rpcRoutineNode = 5;
	public static final int rpcVariableNode = 6;
	public static final int rpcConstantNode = 7;
	public static final int rpcFilterAllNode = 8;
	public static final int rpcFilterProgramUserNode = 9;
	public static final int rpcFilterTypeUserNode = 10;
	public static final int rpcFilterRoutineUserNode = 11;
	public static final int rpcFilterVariableUserNode = 12;
	public static final int rpcFilterConstantUserNode = 13;
	public static final int rpcFilterAllUserNode = 14;
	public static final int rpcSystemNode = 15;
	public int value;



	public SysRpcTcNodeKind () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}