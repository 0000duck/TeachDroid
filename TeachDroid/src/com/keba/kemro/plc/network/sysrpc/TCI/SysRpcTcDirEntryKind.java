package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcDirEntryKind implements XDR {
	public static final int rpcProjectEntry = 0;
	public static final int rpcUnitEntry = 1;
	public static final int rpcProgEntry = 2;
	public static final int rpcDataEntry = 3;
	public static final int rpcFilterAllTcEntry = 4;
	public static final int rpcNoTcEntry = 5;
	public static final int rpcUserProgEntry = 6;
	public static final int rpcUserDataEntry = 7;
	public static final int rpcFilterAllUserEntry = 8;
	public static final int rpcFilterAllProgEntry = 9;
	public static final int rpcFilterAllEntry = 10;
	public static final int rpcArchiveEntry = 11;
	public int value;



	public SysRpcTcDirEntryKind () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}