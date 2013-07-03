package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysFlag implements XDR {
	public static final int eRepSysFlagUndef = 0;
	public static final int eRepSysFlagQuit = 1;
	public static final int eRepSysFlagReset = 2;
	public static final int eRepSysFlagQuitAndReset = 4;
	public static final int eRepSysFlagQuitOrReset = 8;
	public static final int eRepSysFlagToQuit = 16;
	public static final int eRepSysFlagToReset = 32;
	public static final int eRepSysFlagToQuitAndReset = 64;
	public static final int eRepSysFlagIsQuit = 128;
	public int value;



	public TRepSysFlag () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}