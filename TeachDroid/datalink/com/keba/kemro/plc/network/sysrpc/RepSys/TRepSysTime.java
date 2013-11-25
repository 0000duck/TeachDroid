package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysTime implements XDR {
	public int sec;
	public int usec;

	public TRepSysTime () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(sec);
		out.writeInt(usec);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		sec = in.readInt();
		usec = in.readInt();
	}
}