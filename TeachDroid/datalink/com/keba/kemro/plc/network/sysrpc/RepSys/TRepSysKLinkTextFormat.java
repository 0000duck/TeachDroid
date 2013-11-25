package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysKLinkTextFormat implements XDR {
	public static final int eRepSysKLinkUnKnown = 0;
	public static final int eRepSysKLinkAscii = 1;
	public static final int eRepSysKLinkAnsi = 2;
	public static final int eRepSysKLinkUniCode2Byte = 3;
	public static final int eRepSysKLinkUniCode4Byte = 4;
	public int value;



	public TRepSysKLinkTextFormat () {
	}
	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value); 
	}
	public void read (RPCInputStream in) throws RPCException, IOException {
		value = in.readInt(); 
	}
}