package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysMsgParamKind implements XDR {
	public static final int eRepSysIntParam = 0;
	public static final int eRepSysFloatParam = 1;
	public static final int eRepSysStringParam = 2;
	public static final int eRepSysMemoryParam = 3;
	public static final int eRepSysWStringParam = 4;
	public static final int eRepSysUINT32Param = 5;
	public static final int eRepSysUINT64Param = 7;
	public static final int eRepSysRealParam = 6;
	public int value;



	public TRepSysMsgParamKind () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}