package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcProjState implements XDR {
	public static final int rpcProjIsLoading = 0;
	public static final int rpcProjIsUnloading = 1;
	public static final int rpcProjIsLoaded = 2;
	public int value;



	public SysRpcTcProjState () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}