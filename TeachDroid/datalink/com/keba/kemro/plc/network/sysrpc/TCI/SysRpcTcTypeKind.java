package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcTypeKind implements XDR {
	public static final int rpcNoType = 0;
	public static final int rpcBoolType = 1;
	public static final int rpcInt8Type = 2;
	public static final int rpcInt16Type = 3;
	public static final int rpcInt32Type = 4;
	public static final int rpcInt64Type = 5;
	public static final int rpcRealType = 6;
	public static final int rpcStringType = 7;
	public static final int rpcSubRangeType = 8;
	public static final int rpcEnumType = 9;
	public static final int rpcArrayType = 10;
	public static final int rpcStructType = 11;
	public static final int rpcUnitType = 12;
	public static final int rpcMapToType = 13;
	public static final int rpcRoutineType = 14;
	public static final int rpcAnyType = 15;
	public static final int rpcByteType = 20;
	public static final int rpcWordType = 21;
	public static final int rpcDWordType = 22;
	public static final int rpcLWordType = 23;
	public int value;



	public SysRpcTcTypeKind () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}